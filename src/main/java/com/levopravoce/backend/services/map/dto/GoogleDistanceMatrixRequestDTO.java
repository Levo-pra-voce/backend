package com.levopravoce.backend.services.map.dto;

import lombok.Data;

@Data
public class GoogleDistanceMatrixRequestDTO {
    private Double originLat;
    private Double originLng;
    private Double destinationLat;
    private Double destinationLng;
}
