package com.levopravoce.backend.services.request.utils;

import com.levopravoce.backend.services.request.dto.RequestDTO;
import org.springframework.stereotype.Component;

@Component
public class RequestUtils {
    public void validateNewRequest(RequestDTO requestDTO) {
        validateOrderId(requestDTO.getOrderId());
        validateStatus(requestDTO.getStatus());
        validateDeliveryman(requestDTO.getDeliverymanId());
    }

    public void validateOrderId(Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("ID do pedido inválido");
        }
    }

    public void validateStatus(String status) {
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Status inválido");
        }
    }

    public void validateDeliveryman(Long deliveryman) {
        if (deliveryman == null) {
            throw new IllegalArgumentException("Entregador inválido");
        }
    }
}
