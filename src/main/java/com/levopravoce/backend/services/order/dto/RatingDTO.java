package com.levopravoce.backend.services.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {
  private String comment;
  private Integer note;
  @JsonFormat(pattern = "dd/MM/yyyy")
  private LocalDateTime creationDate;
}
