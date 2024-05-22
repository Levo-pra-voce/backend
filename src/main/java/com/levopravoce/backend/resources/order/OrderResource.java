package com.levopravoce.backend.resources.order;

import com.levopravoce.backend.services.order.OrderService;
import com.levopravoce.backend.services.order.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
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
    public void createOrder(
            @RequestBody OrderDTO orderDTO
    ) {
        this.orderService.createOrder(orderDTO);
    }
}
