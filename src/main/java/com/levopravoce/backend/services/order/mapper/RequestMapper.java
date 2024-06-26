package com.levopravoce.backend.services.order.mapper;

import com.levopravoce.backend.entities.Request;
import com.levopravoce.backend.services.order.dto.RequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface RequestMapper {
  @Mapping(target = "name", source = "order.client.name")
  @Mapping(target = "destinationAddress", source = "order.destinationAddress")
  @Mapping(target = "originAddress", source = "order.originAddress")
  @Mapping(target = "distanceKm", source = "order.distanceMeters", qualifiedByName = "MetersToKm")
  @Mapping(target = "deliveryDate", source = "order.deliveryDate")
  RequestDTO toDTO(Request order);

  @Named("MetersToKm")
  default Double metersToKm(Double meters) {
    return meters / 1000;
  }
}
