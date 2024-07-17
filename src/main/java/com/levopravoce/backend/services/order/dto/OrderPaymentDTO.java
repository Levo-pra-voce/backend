package com.levopravoce.backend.services.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderPaymentDTO {
  private Boolean isPaid;
  private Long orderId;
}
