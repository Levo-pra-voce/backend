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
        validateLatLng(origin);
        validateLatLng(destination);
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
                .distanceLabel(firstElement.getDistance().getText())
                .distanceValueMeters(firstElement.getDistance().getValue())
                .durationLabel(firstElement.getDuration().getText())
                .durationValueSeconds(firstElement.getDuration().getValue())
                .originAddress(Optional.ofNullable(response.getOriginAddresses()).orElse(List.of()).stream().findFirst().orElse(null))
                .originAddress(Optional.ofNullable(response.getDestinationAddresses()).orElse(List.of()).stream().findFirst().orElse(null))
                .build();
    }

    private void validateLatLng(LatLngDTO latLngDTO) {
        if (latLngDTO == null) {
            throw new IllegalArgumentException("Latitude e longitude são obrigatórios");
        }

        if (latLngDTO.getLat() == null || latLngDTO.getLng() == null) {
            throw new IllegalArgumentException("Latitude e longitude são obrigatórios");
        }
    }
}
