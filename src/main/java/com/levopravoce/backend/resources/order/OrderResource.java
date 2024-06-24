package com.levopravoce.backend.resources.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.levopravoce.backend.common.SecurityUtils;
import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.services.order.OrderService;
import com.levopravoce.backend.services.order.dto.OrderDTO;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderResource {

  private final OrderService orderService;

  @PostMapping
  public OrderDTO createOrder(
      @RequestBody OrderDTO orderDTO
  ) {
    return this.orderService.createOrder(orderDTO);
  }

  @GetMapping("/deliveries-pending")
  public List<OrderDTO> getDeliveriesPending() {
    User currentUser = SecurityUtils.getCurrentUserThrow();
    return this.orderService.getDeliveriesPending(currentUser);
  }

  @GetMapping("/{id}")
  public OrderDTO getOrderById(@PathVariable Long id) {
    User currentUser = SecurityUtils.getCurrentUserThrow();
    return this.orderService.getOrderById(currentUser, id);
  }

  @GetMapping("/last-progress")
  public ResponseEntity<OrderDTO> getLastProgress() {
    User currentUser = SecurityUtils.getCurrentUserThrow();
    Optional<OrderDTO> orderDTO = this.orderService.getLatestOrderInProgress(currentUser);
    return orderDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
  }

  @PutMapping("/finish")
  public void finishOrder() throws JsonProcessingException {
    User currentUser = SecurityUtils.getCurrentUserThrow();
    this.orderService.finishOrder(currentUser);
  }

  @GetMapping("/payment")
  public ResponseEntity<Void> payment() throws JsonProcessingException {
    User currentUser = SecurityUtils.getCurrentUserThrow();
    this.orderService.payment(currentUser);
    return ResponseEntity.noContent().build();
  }
}
