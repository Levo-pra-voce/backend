package com.levopravoce.backend.resources.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.levopravoce.backend.common.SecurityUtils;
import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.services.authenticate.dto.UserDTO;
import com.levopravoce.backend.services.order.OrderService;
import com.levopravoce.backend.services.order.dto.OrderDTO;
import com.levopravoce.backend.services.order.dto.RecommendUserDTO;
import com.levopravoce.backend.services.order.dto.RequestDTO;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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
  @PreAuthorize("authentication.principal.userType.name() == 'CLIENTE'")
  public OrderDTO createOrder(
      @RequestBody OrderDTO orderDTO
  ) {
    return this.orderService.createOrder(orderDTO);
  }

  @GetMapping("/deliveries-pending")
  @PreAuthorize("authentication.principal.userType.name() == 'ENTREGADOR'")
  public List<OrderDTO> getDeliveriesPending() {
    User currentUser = SecurityUtils.getCurrentUserThrow();
    return this.orderService.getDeliveriesPending(currentUser);
  }

  @GetMapping("/{id}")
  public OrderDTO getOrderById(@PathVariable Long id) {
    User currentUser = SecurityUtils.getCurrentUserThrow();
    return this.orderService.getOrderById(currentUser, id);
  }

  @GetMapping("/deliverymans")
  @PreAuthorize("authentication.principal.userType.name() == 'CLIENTE'")
  public List<RecommendUserDTO> getDeliverymans() {
    return this.orderService.getAllDeliveryManToOrder();
  }

  @GetMapping("/last-progress")
  public ResponseEntity<OrderDTO> getLastProgress() {
    User currentUser = SecurityUtils.getCurrentUserThrow();
    Optional<OrderDTO> orderDTO = this.orderService.getLatestOrderInProgress(currentUser);
    return orderDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
  }

  @GetMapping("/last-pending")
  public ResponseEntity<OrderDTO> getLastPending() {
    User currentUser = SecurityUtils.getCurrentUserThrow();
    Optional<OrderDTO> orderDTO = this.orderService.getLatestOrderInPending(currentUser);
    return orderDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
  }

  @PutMapping("/finish")
  @PreAuthorize("authentication.principal.userType.name() == 'ENTREGADOR'")
  public void finishOrder() throws JsonProcessingException {
    User currentUser = SecurityUtils.getCurrentUserThrow();
    this.orderService.finishOrder(currentUser);
  }

  @PostMapping("/assign-deliveryman/{id}")
  @PreAuthorize("authentication.principal.userType.name() == 'CLIENTE'")
  public ResponseEntity<Void> assignDeliveryman(
      @PathVariable("id") Long deliveryManId
  ) {
    User currentUser = SecurityUtils.getCurrentUserThrow();
    this.orderService.assignDeliveryman(currentUser, deliveryManId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/accept/{id}")
  @PreAuthorize("authentication.principal.userType.name() == 'ENTREGADOR'")
  public ResponseEntity<Void> acceptOrder(
      @PathVariable Long id
  ) {
    User currentUser = SecurityUtils.getCurrentUserThrow();
    this.orderService.acceptRequestOrder(currentUser, id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/reject/{id}")
  @PreAuthorize("authentication.principal.userType.name() == 'ENTREGADOR'")
  public ResponseEntity<Void> rejectOrder(
      @PathVariable Long id
  ) {
    User currentUser = SecurityUtils.getCurrentUserThrow();
    this.orderService.rejectRequestOrder(currentUser, id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
    User currentUser = SecurityUtils.getCurrentUserThrow();
    this.orderService.cancelOrder(currentUser, id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/payment")
  @PreAuthorize("authentication.principal.userType.name() == 'CLIENTE'")
  public ResponseEntity<Void> payment() throws JsonProcessingException {
    User currentUser = SecurityUtils.getCurrentUserThrow();
    this.orderService.payment(currentUser);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/assign-orders")
  @PreAuthorize("authentication.principal.userType.name() == 'ENTREGADOR'")
  public List<RequestDTO> getAssignOrders() {
    User currentUser = SecurityUtils.getCurrentUserThrow();
    return this.orderService.getAssignOrders(currentUser);
  }
}
