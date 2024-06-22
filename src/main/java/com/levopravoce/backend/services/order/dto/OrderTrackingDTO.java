package com.levopravoce.backend.services.order.dto;

import lombok.Data;

@Data
public class OrderTrackingDTO {
  private Long orderId;
  private Double latitude;
  private Double longitude;
}
