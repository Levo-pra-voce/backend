package com.levopravoce.backend.services.order.mapper;

import com.levopravoce.backend.entities.Order;
import com.levopravoce.backend.services.order.dto.OrderDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "height", source = "height")
    @Mapping(target = "width", source = "width")
    @Mapping(target = "maxWeight", source = "maxWeight")
    @Mapping(target = "haveSecurity", source = "haveSecurity")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "destinationAddress", source = "destinationAddress")
    @Mapping(target = "originAddress", source = "originAddress")
    Order toEntity(OrderDTO orderDTO);
    @InheritInverseConfiguration
    OrderDTO toDTO(Order order);
}
