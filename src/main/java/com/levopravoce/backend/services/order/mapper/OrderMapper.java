package com.levopravoce.backend.services.order.mapper;

import com.levopravoce.backend.entities.Order;
import com.levopravoce.backend.services.order.dto.OrderDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toEntity(OrderDTO orderDTO);
    OrderDTO toDTO(Order order);
}
