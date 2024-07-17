package com.levopravoce.backend.services.order.dto;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendUserDTO {
  private Long userId;
  private String name;
  private Double price;
  private Double averageRating;
  private String phone;
  private List<RatingDTO> ratings;
}
