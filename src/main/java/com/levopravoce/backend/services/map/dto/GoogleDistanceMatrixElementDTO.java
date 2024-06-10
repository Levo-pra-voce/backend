package com.levopravoce.backend.services.map.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GoogleDistanceMatrixElementDTO {
    private String status;
    private GoogleDistanceMatrixValueDTO duration;
    private GoogleDistanceMatrixValueDTO distance;
}
