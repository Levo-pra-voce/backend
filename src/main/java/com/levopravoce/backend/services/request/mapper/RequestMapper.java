package com.levopravoce.backend.services.request.mapper;

import com.levopravoce.backend.entities.Request;
import com.levopravoce.backend.services.request.dto.RequestDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    @Mapping(target = "status", source = "status")
    Request toEntity(RequestDTO requestDTO);

    @InheritInverseConfiguration
    RequestDTO toDTO(Request request);
}
