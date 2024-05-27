package com.levopravoce.backend.services.relatory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RelatoryDTO {
  private Double value;
  private String clientName;
  private String deliveryDate;
}
