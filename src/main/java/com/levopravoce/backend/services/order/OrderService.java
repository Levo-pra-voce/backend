package com.levopravoce.backend.services.order;

import com.levopravoce.backend.common.SecurityUtils;
import com.levopravoce.backend.entities.Order;
import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.entities.UserType;
import com.levopravoce.backend.repository.OrderRepository;
import com.levopravoce.backend.services.order.dto.OrderDTO;
import com.levopravoce.backend.services.order.mapper.OrderMapper;
import com.levopravoce.backend.services.order.utils.OrderUtils;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;
  private final OrderUtils orderUtils;

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

    Order order = orderMapper.toEntity(orderDTO);
    order.setClient(currentUser);
    return orderMapper.toDTO(orderRepository.save(order));
  }

  public List<OrderDTO> getDeliveriesPending(
      User currentUser
  ) {
    if (!Objects.equals(currentUser.getUserType(), UserType.ENTREGADOR)) {
      throw new RuntimeException("Apenas entregadores podem visualizar pedidos pendentes.");
    }

    List<Order> orders = orderRepository.findByStatusPendingOrInProgress(currentUser.getId());
    List<OrderDTO> orderDTOS = orders.stream().map(orderMapper::toDTO).toList();
    return orderDTOS;
  }

  public OrderDTO getOrderById(User currentUser, Long id) {
    Order order = orderRepository.findById(id).orElseThrow();

    if (!currentUser.getId().equals(order.getDeliveryman().getId())) {
      throw new RuntimeException("Você não tem permissão para visualizar este pedido.");
    }

    return orderMapper.toDTO(order);
  }

  public OrderDTO getLatestOrderInProgress(User currentUser) {
    Order order = orderRepository.findFirstByOrderInProgress(currentUser.getId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404),
            "Nenhum pedido em andamento."));

    return orderMapper.toDTO(order);
  }
}
