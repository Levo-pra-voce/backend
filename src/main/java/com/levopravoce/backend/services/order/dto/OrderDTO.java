package com.levopravoce.backend.services.order.dto;

import com.levopravoce.backend.entities.Order;
import com.levopravoce.backend.services.authenticate.dto.UserDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderDTO {
    private Long id;
    private double height;
    private double width;
    private double maxWeight;
    private LocalDate deliveryDate;
    private Boolean haveSecurity;
    private Order.OrderStatus status;
    private double value;
    private UserDTO deliveryman;
    private UserDTO client;
//    private VehicleDTO vehicle;
//    private PaymentDTO payment;
}
