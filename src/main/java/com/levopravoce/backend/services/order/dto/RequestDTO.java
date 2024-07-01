package com.levopravoce.backend.services.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RequestDTO {
  private Long orderId;
  private Long deliveryManId;
  private String name;
  private String destinationAddress;
  private String originAddress;
  private Double distanceKm;
  @JsonFormat(pattern = "dd/MM/yyyy")
  private LocalDateTime deliveryDate;
  private Double price = 200.0;
  private Double averageRating = 5.0;

  public RequestDTO(Long orderId, Long deliveryManId, String name, String destinationAddress, String originAddress, Double distanceKm,
      LocalDateTime deliveryDate, Double price, Double averageRating) {
    this.orderId = orderId;
    this.deliveryManId = deliveryManId;
    this.name = name;
    this.destinationAddress = destinationAddress;
    this.originAddress = originAddress;
    this.distanceKm = distanceKm;
    this.deliveryDate = deliveryDate;
    this.price = Optional.ofNullable(price).orElse(200.0);
    this.averageRating = Optional.ofNullable(averageRating).orElse(5.0);
  }
}
