package com.levopravoce.backend.services.request.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class RequestDTO {
    private Long id;
    private Long orderId;
    private Long deliverymanId;
    private String status;

    public RequestDTO(Long id, Long orderId, Long deliverymanId, String status) {
        this.id = id;
        this.orderId = orderId;
        this.deliverymanId = deliverymanId;
        this.status = status;
    }
}
