package com.levopravoce.backend.resources.map;

import com.levopravoce.backend.services.map.GoogleMapsService;
import com.levopravoce.backend.services.map.dto.GoogleDistanceMatrixRequestDTO;
import com.levopravoce.backend.services.map.dto.GoogleDistanceMatrixResponseDTO;
import com.levopravoce.backend.services.map.dto.LatLngDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/map")
public class MapResource {
    private final GoogleMapsService googleMapsService;

    @GetMapping("/distance")
    public GoogleDistanceMatrixResponseDTO getDistance(GoogleDistanceMatrixRequestDTO requestDTO) {
        return googleMapsService.getDistance(
                LatLngDTO.builder()
                        .lat(requestDTO.getOriginLat())
                        .lng(requestDTO.getOriginLng())
                        .build(),
                LatLngDTO.builder()
                        .lat(requestDTO.getDestinationLat())
                        .lng(requestDTO.getDestinationLng())
                        .build()
        );
    }
}
