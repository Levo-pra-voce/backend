package com.levopravoce.backend.services.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.levopravoce.backend.common.SecurityUtils;
import com.levopravoce.backend.entities.Order;
import com.levopravoce.backend.entities.Order.OrderStatus;
import com.levopravoce.backend.entities.Request;
import com.levopravoce.backend.entities.Request.RequestStatus;
import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.entities.UserType;
import com.levopravoce.backend.entities.Vehicle;
import com.levopravoce.backend.repository.OrderRepository;
import com.levopravoce.backend.repository.UserRepository;
import com.levopravoce.backend.services.authenticate.dto.UserDTO;
import com.levopravoce.backend.services.map.GoogleMapsService;
import com.levopravoce.backend.services.map.dto.LatLngDTO;
import com.levopravoce.backend.services.order.dto.OrderDTO;
import com.levopravoce.backend.services.order.dto.OrderPaymentDTO;
import com.levopravoce.backend.services.order.dto.OrderTrackingDTO;
import com.levopravoce.backend.services.order.dto.OrderTrackingStatus;
import com.levopravoce.backend.services.order.mapper.OrderMapper;
import com.levopravoce.backend.services.order.utils.OrderUtils;
import com.levopravoce.backend.socket.WebSocketDestination;
import com.levopravoce.backend.socket.WebSocketHandler;
import com.levopravoce.backend.socket.dto.MessageSocketDTO;
import com.levopravoce.backend.socket.dto.MessageType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final WebSocketHandler webSocketHandler;
  private final ObjectMapper objectMapper;
  private final GoogleMapsService googleMapsService;
  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;
  private final OrderUtils orderUtils;
  private final UserRepository userRepository;

  public OrderDTO createOrder(OrderDTO orderDTO) {
    orderUtils.validateNewOrder(orderDTO);

    User currentUser = SecurityUtils.getCurrentUser().orElseThrow();
    if (!Objects.equals(currentUser.getUserType(), UserType.CLIENTE)) {
      throw new RuntimeException("Apenas clientes podem criar pedidos.");
    }

    boolean existsByStatusInProgressOrPending = orderRepository.existsByStatusInProgressOrPending(
        currentUser.getId());
    if (existsByStatusInProgressOrPending) {
      throw new RuntimeException("Você já possui um pedido em andamento ou pendente.");
    }

    var result = googleMapsService.getDistance(
        LatLngDTO.builder().lat(orderDTO.getOriginLatitude()).lng(orderDTO.getOriginLongitude())
            .build(), LatLngDTO.builder().lat(orderDTO.getDestinationLatitude())
            .lng(orderDTO.getDestinationLongitude()).build());
    if (result == null) {
      throw new IllegalArgumentException("Erro ao buscar distância da API do Google Maps.");
    }
    if (result.getDistanceValueMeters() == null) {
      throw new IllegalArgumentException("Origem e destino são inválidos.");
    }

    Order order = orderMapper.toEntity(orderDTO);
    order.setDistanceMeters(result.getDistanceValueMeters());
    order.setDurationSeconds(result.getDurationValueSeconds());
    order.setDestinationAddress(result.getDestinationAddress());
    order.setOriginAddress(result.getOriginAddress());
    order.setClient(currentUser);
    order.setVehicle(
        currentUser.getVehicles().stream().filter(Vehicle::isActive).findFirst().orElse(null));
    order.setHaveSecurity(Optional.ofNullable(orderDTO.getHaveSecurity()).orElse(false));
    order.setStatus(OrderStatus.ESPERANDO);
    return orderMapper.toDTO(orderRepository.save(order));
  }

  public List<OrderDTO> getDeliveriesPending(User currentUser) {
    if (!Objects.equals(currentUser.getUserType(), UserType.ENTREGADOR)) {
      throw new RuntimeException("Apenas entregadores podem visualizar pedidos pendentes.");
    }

    List<Order> orders = orderRepository.findByStatusPendingOrInProgress(currentUser.getId());
    return orders.stream().map(orderMapper::toDTO).toList();
  }

  public OrderDTO getOrderById(User currentUser, Long id) {
    Order order = orderRepository.findById(id).orElseThrow();

    if (!currentUser.getId().equals(order.getDeliveryman().getId())) {
      throw new RuntimeException("Você não tem permissão para visualizar este pedido.");
    }

    return orderMapper.toDTO(order);
  }

  public List<UserDTO> getAllDeliveryMan() {
    return userRepository.findAll()
        .stream()
        .filter(user -> Objects.equals(user.getUserType(), UserType.ENTREGADOR))
        .map(User::toDTO)
        .toList();
  }

  public Optional<OrderDTO> getLatestOrderInProgress(User currentUser) {
    Optional<Order> order = orderRepository.findLastOrderInProgress(currentUser.getId());
    return order.map(orderMapper::toDTO);
  }

  public void finishOrder(User currentUser) throws JsonProcessingException {
    Order order = orderRepository.findLastOrderInProgress(currentUser.getId()).orElseThrow();
    order.setDeliveryDate(LocalDateTime.now());
    order.setStatus(OrderStatus.ENTREGADO);
    orderRepository.save(order);

    List<WebSocketSession> webSocketsSessions = getWebSocketsSessionsByOrder(order);
    OrderTrackingDTO orderTrackingDTO = OrderTrackingDTO.builder().orderId(order.getId())
        .status(OrderTrackingStatus.FINISHED).build();
    String orderTrackingJson = objectMapper.writeValueAsString(orderTrackingDTO);
    MessageSocketDTO messageSocketDTO = mountMessageSocketDTOByOrder(orderTrackingJson,
        WebSocketDestination.ORDER_MAP, order);
    String messageJson = objectMapper.writeValueAsString(messageSocketDTO);
    webSocketsSessions.forEach(webSocketSession -> {
      try {
        TextMessage textMessage = new TextMessage(messageJson);
        webSocketSession.sendMessage(textMessage);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  public void payment(User currentUser) throws JsonProcessingException {
    String notPermissionMessage = "Você não tem permissão para realizar o pagamento.";
    Order order = orderRepository.findLastOrderInProgress(currentUser.getId()).orElseThrow(
        () -> new RuntimeException(notPermissionMessage)
    );
    boolean haveWritePermission = Objects.equals(order.getClient().getId(), currentUser.getId());
    if (!haveWritePermission) {
      throw new RuntimeException(notPermissionMessage);
    }
    order.setStatus(OrderStatus.FEITO_PAGAMENTO);
    orderRepository.save(order);
    List<WebSocketSession> webSocketsSessions = getWebSocketsSessionsByOrder(order);
    OrderPaymentDTO orderPaymentDTO = OrderPaymentDTO.builder().isPaid(true).build();
    String orderPaymentJson = objectMapper.writeValueAsString(orderPaymentDTO);
    MessageSocketDTO messageSocketDTO = mountMessageSocketDTOByOrder(orderPaymentJson,
        WebSocketDestination.ORDER_PAYMENT, order);
    String messageJson = objectMapper.writeValueAsString(messageSocketDTO);
    webSocketsSessions.forEach(webSocketSession -> {
      try {
        webSocketSession.sendMessage(new TextMessage(messageJson));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  private MessageSocketDTO mountMessageSocketDTOByOrder(String json,
      WebSocketDestination destination, Order order) {
    return MessageSocketDTO.builder()
        .type(MessageType.TEXT)
        .message(json)
        .destination(destination)
        .timestamp(System.currentTimeMillis())
        .sender(order.getDeliveryman().getUsername())
        .receiver(order.getClient().getUsername())
        .build();
  }

  private List<WebSocketSession> getWebSocketsSessionsByOrder(Order order) {
    List<WebSocketSession> webSocketsSessions = new ArrayList<>();
    webSocketsSessions.addAll(Optional.ofNullable(
            webSocketHandler.userToActiveSessions.get(order.getClient().getId()))
        .orElse(new ArrayList<>()));
    webSocketsSessions.addAll(Optional.ofNullable(
            webSocketHandler.userToActiveSessions.get(order.getDeliveryman().getId()))
        .orElse(new ArrayList<>()));
    return webSocketsSessions;
  }

  public void assignDeliveryman(User currentUser, Long deliveryManId) {
    Order order = orderRepository.findLastOrderInPending(currentUser.getId()).orElseThrow(
        () -> new RuntimeException("Você não possui uma entrega pendente.")
    );
    orderUtils.verifyIfOrderAlreadyHaveDeliveryMan(order, deliveryManId);
    Optional<User> deliveryManOptional = userRepository.findById(deliveryManId);
    deliveryManOptional.ifPresentOrElse(
        deliveryMan -> {
          Request request = Request
              .builder()
              .deliveryman(deliveryMan)
              .order(order)
              .status(RequestStatus.SOLICITADO)
              .build();
          order.getRequests().add(request);
          orderRepository.save(order);
        },
        () -> {
          throw new RuntimeException("Entregador não encontrado.");
        }
    );
  }

  public void cancelOrder(User currentUser, Long orderId) {
    Order order = orderRepository.findById(orderId).orElseThrow(
        () -> new RuntimeException("Entrega não encontrada.")
    );
    if (!Objects.equals(order.getClient().getId(), currentUser.getId())) {
      throw new RuntimeException("Você não tem permissão para cancelar este pedido.");
    }
    if (order.getStatus() == OrderStatus.ENTREGADO) {
      throw new RuntimeException("Pedido já foi entregue.");
    }

    order.setStatus(OrderStatus.CANCELADO);
    orderRepository.save(order);
  }

  public void acceptCurrentOrder(User currentUser) {
    Order order = orderRepository.findLastOrderInPending(currentUser.getId()).orElseThrow(
        () -> new RuntimeException("Você não possui uma entrega pendente.")
    );
    if (!Objects.equals(currentUser.getId(), order.getDeliveryman().getId())) {
      throw new RuntimeException("Você não tem permissão para aceitar este pedido.");
    }
    order.setStatus(OrderStatus.EM_PROGRESSO);
    orderRepository.save(order);
  }
}
