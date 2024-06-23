package com.levopravoce.backend.resources.order;

import com.levopravoce.backend.common.SecurityUtils;
import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.services.authenticate.dto.UserDTO;
import com.levopravoce.backend.services.order.OrderService;
import com.levopravoce.backend.services.order.dto.OrderDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/id")
    public OrderDTO getOrderById(@PathVariable  Long id) {
        User currentUser = SecurityUtils.getCurrentUserThrow();
        return this.orderService.getOrderById(currentUser, id);
    }

    @GetMapping("/deliverymans")
    public List<UserDTO> getDeliverymans() {
        return this.orderService.getAllDeliveryMan();
    }
}
