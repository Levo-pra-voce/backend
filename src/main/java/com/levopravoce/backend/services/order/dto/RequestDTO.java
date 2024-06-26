package com.levopravoce.backend.services.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestDTO {
  private String name;
  private String destinationAddress;
  private String originAddress;
  private Double distanceKm;
  @JsonFormat(pattern = "dd/MM/yyyy")
  private LocalDateTime deliveryDate;
  private Double price;
}
