package com.levopravoce.backend.services.order.utils;

import com.levopravoce.backend.services.order.dto.OrderDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class OrderUtils {

    public void validateNewOrder(OrderDTO orderDTO) {
        validateHeight(orderDTO.getHeight());
        validateWidth(orderDTO.getWidth());
        validadeMaxWeight(orderDTO.getMaxWeight());
        validateDeliveryDate(orderDTO.getDeliveryDate());
    }

    public void validateHeight(Double height) {
        if (height == null || height <= 0) {
            throw new IllegalArgumentException("Altura inválida");
        }
    }

    public void validateWidth(Double width) {
        if (width == null || width <= 0) {
            throw new IllegalArgumentException("Largura inválida");
        }
    }

    public void validadeMaxWeight(Double maxWeight) {
        if (maxWeight == null || maxWeight <= 0) {
            throw new IllegalArgumentException("Peso máximo inválido");
        }
    }

    public void validateDeliveryDate(LocalDate deliveryDate) {
        if (deliveryDate == null || deliveryDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data de entrega inválida");
        }
    }
}
