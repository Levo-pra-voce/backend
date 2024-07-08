package com.levopravoce.backend.services.order.mapper;

import com.levopravoce.backend.entities.Order;
import com.levopravoce.backend.entities.Vehicle;
import com.levopravoce.backend.services.order.dto.OrderDTO;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface OrderMapper {

  @Mapping(target = "height", source = "height")
  @Mapping(target = "width", source = "width")
  @Mapping(target = "maxWeight", source = "maxWeight")
  @Mapping(target = "haveSecurity", source = "haveSecurity")
  @Mapping(target = "value", source = "price")
  @Mapping(target = "status", source = "status")
  @Mapping(target = "destinationAddress", source = "destinationAddress")
  @Mapping(target = "originAddress", source = "originAddress")
  Order toEntity(OrderDTO orderDTO);

  @Mapping(target = "height", source = "height")
  @Mapping(target = "width", source = "width")
  @Mapping(target = "maxWeight", source = "maxWeight")
  @Mapping(target = "haveSecurity", source = "haveSecurity")
  @Mapping(target = "price", source = "value")
  @Mapping(target = "status", source = "status")
  @Mapping(target = "destinationAddress", source = "destinationAddress")
  @Mapping(target = "originAddress", source = "originAddress")
  @Mapping(source = "order", target = "carPlate", qualifiedByName = "setCarPlate")
  @Mapping(source = "deliveryDate", target = "deliveryDate")
  OrderDTO toDTO(Order order);

  @Named("setCarPlate")
  default String setCarPlate(Order order) {
    return Optional.ofNullable(order.getVehicle())
        .map(Vehicle::getPlate).orElse(null);
  }
}
