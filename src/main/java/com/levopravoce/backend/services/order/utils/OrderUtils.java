package com.levopravoce.backend.services.order.utils;

import com.levopravoce.backend.services.order.dto.OrderDTO;
import org.springframework.stereotype.Component;

@Component
public class OrderUtils {

    public void validateFields(OrderDTO orderDTO) {
        if (orderDTO.getHeight() <= 0 || orderDTO.getWidth() <= 0 || orderDTO.getMaxWeight() <= 0 || orderDTO.getValue() <= 0) {
            throw new IllegalArgumentException("Invalid order data");
        }
    }
}
