package com.levopravoce.backend.services.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.levopravoce.backend.common.SecurityUtils;
import com.levopravoce.backend.common.UserUtils;
import com.levopravoce.backend.entities.Order;
import com.levopravoce.backend.entities.Order.OrderStatus;
import com.levopravoce.backend.entities.Rating;
import com.levopravoce.backend.entities.Request;
import com.levopravoce.backend.entities.Request.RequestStatus;
import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.entities.UserType;
import com.levopravoce.backend.entities.Vehicle;
import com.levopravoce.backend.repository.OrderRepository;
import com.levopravoce.backend.repository.RequestRepository;
import com.levopravoce.backend.repository.UserRepository;
import com.levopravoce.backend.services.map.GoogleMapsService;
import com.levopravoce.backend.services.map.dto.LatLngDTO;
import com.levopravoce.backend.services.order.dto.OrderDTO;
import com.levopravoce.backend.services.order.dto.OrderPaymentDTO;
import com.levopravoce.backend.services.order.dto.OrderTrackingDTO;
import com.levopravoce.backend.services.order.dto.OrderTrackingStatus;
import com.levopravoce.backend.services.order.dto.RatingDTO;
import com.levopravoce.backend.services.order.dto.RecommendUserDTO;
import com.levopravoce.backend.services.order.dto.RequestDTO;
import com.levopravoce.backend.services.order.mapper.OrderMapper;
import com.levopravoce.backend.services.order.mapper.RequestMapper;
import com.levopravoce.backend.services.order.utils.OrderUtils;
import com.levopravoce.backend.services.order.utils.RequestUtils;
import com.levopravoce.backend.socket.WebSocketDestination;
import com.levopravoce.backend.socket.WebSocketHandler;
import com.levopravoce.backend.socket.dto.MessageSocketDTO;
import com.levopravoce.backend.socket.dto.MessageType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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
  private final RequestMapper requestMapper;
  private final RequestRepository requestRepository;
  private final RequestUtils requestUtils;
  private final UserUtils userUtils;

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
    order.setDeliveryDate(orderDTO.getDeliveryDate().atTime(0, 10, 0));
    return orderMapper.toDTO(orderRepository.save(order));
  }

  public List<OrderDTO> getDeliveriesPending(User currentUser) {
    List<Order> orders = orderRepository.findByStatusPendingOrInProgress(currentUser.getId());
    return orders.stream().map(orderMapper::toDTO).toList();
  }

  public OrderDTO getOrderById(User currentUser, Long id) {
    Order order = orderRepository.findById(id).orElseThrow();
    Vehicle vehicle = currentUser.getVehicles().stream().filter(Vehicle::isActive).findFirst()
        .orElse(null);
    OrderDTO orderDTO = orderMapper.toDTO(order);
    orderDTO.setPrice(userUtils.calcPrice(vehicle.getPriceBase(), vehicle.getPricePerKm(),
        order.getDistanceMeters(), order.getHaveSecurity()));
    return orderDTO;
  }

  public List<RecommendUserDTO> getAllDeliveryManToOrder() {
    User currentUser = SecurityUtils.getCurrentUser().orElseThrow();
    Order order = orderRepository.findLastOrderInPending(currentUser.getId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Não há pedidos pendentes."));

    return userRepository.findAll()
        .stream()
        .filter(user -> Objects.equals(user.getUserType(), UserType.ENTREGADOR))
        .map(user -> {
              Vehicle userVehicle = user.getVehicles().stream().filter(Vehicle::isActive).findFirst()
                  .orElse(null);
              return RecommendUserDTO
                  .builder()
                  .userId(user.getId())
                  .name(user.getName())
                  .phone(user.getContact())
                  .price(userUtils.calcPrice(userVehicle.getPriceBase(), userVehicle.getPricePerKm(),
                      order.getDistanceMeters(), order.getHaveSecurity()))
                  .averageRating(orderUtils.calculateAverageRating(
                      Optional.ofNullable(user.getRatings()).orElse(new ArrayList<>())))
                  .ratings(Optional.ofNullable(user.getRatings()).orElse(new ArrayList<>())
                      .stream()
                      .map(rating -> RatingDTO.builder()
                          .comment(rating.getComment())
                          .creationDate(rating.getCreationDate())
                          .note(rating.getNote())
                          .build())
                      .toList()
                  )
                  .build();
            }
        )
        .sorted(Comparator.comparing(RecommendUserDTO::getPrice))
        .toList();
  }

  public Optional<OrderDTO> getLatestOrderInProgress(User currentUser) {
    Optional<Order> order = orderRepository.findLastOrderInProgress(currentUser.getId());
    return order.map(orderMapper::toDTO);
  }

  public Optional<OrderDTO> getLatestOrderInPending(User currentUser) {
    Optional<Order> order = orderRepository.findLastOrderInPending(currentUser.getId());
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
    OrderPaymentDTO orderPaymentDTO = OrderPaymentDTO.builder()
        .isPaid(true)
        .orderId(order.getId())
        .build();
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
    try {
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
            orderRepository.saveAndFlush(order);
          },
          () -> {
            throw new RuntimeException("Entregador não encontrado.");
          }
      );
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
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

  public void rejectRequestOrder(User currentUser, Long orderId) {
    Request request = requestRepository.findByDeliveryManAndOrder(currentUser.getId(), orderId)
        .orElseThrow(
            () -> new RuntimeException("Pedido não encontrado.")
        );
    requestUtils.validatePendingStatus(request.getStatus());
    request.setStatus(RequestStatus.RECUSADO);
    requestRepository.save(request);
  }

  public void acceptRequestOrder(User currentUser, Long orderId) {
    Request request = requestRepository.findByDeliveryManAndOrder(currentUser.getId(), orderId)
        .orElseThrow(
            () -> new RuntimeException("Pedido não encontrado.")
        );
    requestUtils.validatePendingStatus(request.getStatus());
    request.setStatus(RequestStatus.ACEITO);
    requestRepository.save(request);

    Order order = orderRepository.findById(orderId).orElseThrow();
    User deliveryman = request.getDeliveryman();
    Vehicle vehicle = deliveryman.getVehicles().stream().filter(Vehicle::isActive).findFirst()
        .orElse(null);
    order.setStatus(OrderStatus.ACEITO);
    order.setValue(userUtils.calcPrice(vehicle.getPriceBase(), vehicle.getPricePerKm(),
        order.getDistanceMeters(), order.getHaveSecurity()));
    order.setDeliveryman(request.getDeliveryman());
    order.setVehicle(
        request.getDeliveryman().getVehicles().stream().filter(Vehicle::isActive).findFirst()
            .orElse(null));
    orderRepository.save(order);
  }

  public List<RequestDTO> getAssignOrders(User currentUser) {
    List<Request> requests = requestRepository.findAllByDeliveryManAndOrder(currentUser.getId());
    return requests.stream().map(request -> {
      RequestDTO requestDTO = requestMapper.toDTO(request);
      Vehicle vehicle = request.getDeliveryman().getVehicles().stream().filter(Vehicle::isActive)
          .findFirst().orElse(null);
      Order order = request.getOrder();
      requestDTO.setPrice(userUtils.calcPrice(vehicle.getPriceBase(), vehicle.getPricePerKm(),
          order.getDistanceMeters(), order.getHaveSecurity()));
      return requestDTO;
    }).toList();
  }

  public void startOrder(User currentUser, Long id) {
    Order order = orderRepository.findById(id).orElseThrow();
    if (!Objects.equals(order.getDeliveryman().getId(), currentUser.getId())) {
      throw new RuntimeException("Você não tem permissão para iniciar este pedido.");
    }
    order.setStatus(OrderStatus.EM_PROGRESSO);
    orderRepository.save(order);

    List<WebSocketSession> webSocketsSessions = getWebSocketsSessionsByOrder(order);

    OrderTrackingDTO orderTrackingDTO = OrderTrackingDTO.builder().orderId(order.getId())
        .status(OrderTrackingStatus.STARTED).build();

    try {
      String orderTrackingJson = objectMapper.writeValueAsString(orderTrackingDTO);
      MessageSocketDTO messageSocketDTO = MessageSocketDTO.builder()
          .type(MessageType.TEXT)
          .message(orderTrackingJson)
          .timestamp(System.currentTimeMillis())
          .sender(currentUser.getUsername())
          .receiver(order.getDeliveryman().getUsername())
          .destination(WebSocketDestination.ORDER_MAP)
          .build();
      webSocketsSessions.forEach(webSocketSession -> {
        try {
          TextMessage textMessage = new TextMessage(
              objectMapper.writeValueAsString(messageSocketDTO));
          webSocketSession.sendMessage(textMessage);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public void reviewOrder(Long orderId, RatingDTO ratingDTO) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new RuntimeException("Pedido não encontrado."));
    if (order.getStatus() != OrderStatus.ENTREGADO && order.getStatus() != OrderStatus.FEITO_PAGAMENTO) {
      throw new RuntimeException("Pedido não foi entregue.");
    }
    Rating rating = Rating.builder()
        .order(order)
        .client(order.getClient())
        .deliveryMan(order.getDeliveryman())
        .comment(ratingDTO.getComment())
        .note(ratingDTO.getNote())
        .creationDate(LocalDateTime.now())
        .build();

    User deliveryMan = order.getDeliveryman();
    List<Rating> ratings = Optional.ofNullable(deliveryMan.getRatings()).orElse(new ArrayList<>());
    ratings.add(rating);
    deliveryMan.setRatings(ratings);
    userRepository.save(deliveryMan);
  }
}
