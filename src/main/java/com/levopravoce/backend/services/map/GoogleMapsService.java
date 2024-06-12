package com.levopravoce.backend.services.map;

import com.levopravoce.backend.services.map.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GoogleMapsService {
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/distancematrix/json";
    private final RestTemplate restTemplate;

    @Value("${google.map.api.key}")
    private String apiKey;

    public GoogleDistanceMatrixResponseDTO getDistance(LatLngDTO origin, LatLngDTO destination) {
        var url = BASE_URL + "?origins=" + origin.getLat() + "," + origin.getLng() +
                "&destinations=" + destination.getLat() + "," + destination.getLng() +
                "&key=" + apiKey
                + "&language=pt-BR";
        GoogleDistanceMatrixResponseApiDTO response = restTemplate.getForObject(url, GoogleDistanceMatrixResponseApiDTO.class);
        if (response == null) {
            throw new IllegalArgumentException("Erro ao buscar distância");
        }
        GoogleDistanceMatrixRowsDTO firstrow = Optional.ofNullable(response.getRows()).orElse(List.of()).stream().findFirst().orElseThrow();
        GoogleDistanceMatrixElementDTO firstElement = Optional.ofNullable(firstrow.getElements()).orElse(List.of()).stream().findFirst().orElseThrow();
        return GoogleDistanceMatrixResponseDTO.builder()
                .distanceLabel(Optional.ofNullable(firstElement.getDistance()).orElse(new GoogleDistanceMatrixValueDTO()).getText())
                .distanceValueMeters(Optional.ofNullable(firstElement.getDistance()).orElse(new GoogleDistanceMatrixValueDTO()).getValue())
                .durationLabel(Optional.ofNullable(firstElement.getDuration()).orElse(new GoogleDistanceMatrixValueDTO()).getText())
                .durationValueSeconds(Optional.ofNullable(firstElement.getDuration()).orElse(new GoogleDistanceMatrixValueDTO()).getValue())
                .originAddress(Optional.ofNullable(response.getOriginAddresses()).orElse(List.of()).stream().findFirst().orElse(null))
                .destinationAddress(Optional.ofNullable(response.getDestinationAddresses()).orElse(List.of()).stream().findFirst().orElse(null))
                .build();
    }
}
