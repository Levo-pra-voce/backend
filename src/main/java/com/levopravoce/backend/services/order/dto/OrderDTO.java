package com.levopravoce.backend.services.order.dto;

import com.levopravoce.backend.entities.Order;
import com.levopravoce.backend.services.authenticate.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDTO {
    private Long id;
    private Double height;
    private Double width;
    private Double maxWeight;
    private LocalDate deliveryDate;
    private Boolean haveSecurity;
    private Order.OrderStatus status;
    private Double value;
    private UserDTO deliveryman;
    private UserDTO client;
//    private VehicleDTO vehicle;
//    private PaymentDTO payment;
}
