package com.levopravoce.backend.services.order.dto;

import com.levopravoce.backend.entities.Order;
import com.levopravoce.backend.services.authenticate.dto.UserDTO;
import lombok.*;

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
    private Boolean haveSecurity = false;
    private Order.OrderStatus status;
    private Double value;
    private UserDTO deliveryman;
    private UserDTO client;
    private Double originLatitude;
    private Double originLongitude;
    private Double destinationLatitude;
    private Double destinationLongitude;
    private Long distanceMeters;
    private Long durationSeconds;
}
