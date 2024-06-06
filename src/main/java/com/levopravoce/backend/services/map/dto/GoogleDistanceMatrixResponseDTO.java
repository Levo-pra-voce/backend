package com.levopravoce.backend.services.map.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GoogleDistanceMatrixResponseDTO {
    private String durationLabel;
    private Long durationValueSeconds;
    private String distanceLabel;
    private Long distanceValueMeters;
    private String originAddress;
    private String destinationAddress;
}
