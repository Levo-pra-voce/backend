package com.levopravoce.backend.services.map.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GoogleDistanceMatrixRowsDTO {
    private List<GoogleDistanceMatrixElementDTO> elements;
}
