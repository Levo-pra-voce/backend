package com.levopravoce.backend.services.map.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GoogleDistanceMatrixResponseApiDTO {
    @JsonProperty("rows")
    private List<GoogleDistanceMatrixRowsDTO> rows;
    @JsonProperty("origin_addresses")
    private List<String> originAddresses = new ArrayList<>();
    @JsonProperty("destination_addresses")
    private List<String> destinationAddresses = new ArrayList<>();
}
