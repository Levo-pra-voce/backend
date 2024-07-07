package com.levopravoce.backend.services.order.dto;

import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class RecommendUserDTO {
  private Long userId;
  private String name;
  private Double price;
  private Double averageRating;
  private String phone;

  public RecommendUserDTO(Long userId, String name, Double price, Double averageRating,
      String phone) {
    this.userId = userId;
    this.name = name;
    this.price = price;
    this.averageRating = 5.0;
    this.phone = phone;
  }
}
