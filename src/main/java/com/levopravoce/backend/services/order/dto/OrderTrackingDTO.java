package com.levopravoce.backend.services.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class OrderTrackingDTO {
  private Long orderId;
  private Double latitude;
  private Double longitude;
  private OrderTrackingStatus status;
}

