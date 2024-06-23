package com.levopravoce.backend.services.request.dto;

import lombok.Data;

@Data
public class RequestDTO {
    private Long id;
    private Long orderId;
    private Long deliverymanId;
    private String status;
}
