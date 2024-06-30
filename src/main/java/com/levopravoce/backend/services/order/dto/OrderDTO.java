package com.levopravoce.backend.services.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.levopravoce.backend.entities.Order;
import com.levopravoce.backend.entities.Order.OrderStatus;
import com.levopravoce.backend.services.authenticate.dto.UserDTO;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@Data
public class OrderDTO {
  private Long id;
  private Double height;
  private Double width;
  private Double maxWeight;
  @JsonFormat(pattern = "dd/MM/yyyy")
  private LocalDate deliveryDate;
  private Boolean haveSecurity;
  private Order.OrderStatus status;
  private Double value;
  private UserDTO deliveryman;
  private UserDTO client;
  private Double averageRating;
  private Double destinationLatitude;
  private Double destinationLongitude;
  private Double originLatitude;
  private Double originLongitude;
  private String destinationAddress;
  private String originAddress;
  private Double price;
  private String carPlate;

  public OrderDTO(Long id, Double height, Double width, Double maxWeight, LocalDate deliveryDate,
      Boolean haveSecurity, OrderStatus status, Double value, UserDTO deliveryman, UserDTO client,
      Double averageRating, Double destinationLatitude, Double destinationLongitude,
      Double originLatitude, Double originLongitude, String destinationAddress,
      String originAddress,
      Double price,
      String carPlate
  ) {
    this.id = id;
    this.height = height;
    this.width = width;
    this.maxWeight = maxWeight;
    this.deliveryDate = deliveryDate;
    this.haveSecurity = haveSecurity;
    this.status = status;
    this.value = value;
    this.deliveryman = deliveryman;
    this.client = client;
    this.destinationLatitude = destinationLatitude;
    this.destinationLongitude = destinationLongitude;
    this.originLatitude = originLatitude;
    this.originLongitude = originLongitude;
    this.destinationAddress = destinationAddress;
    this.originAddress = originAddress;
    this.carPlate = carPlate;
    this.price = haveSecurity ? 220.0 : 200.0;
    this.averageRating = 5.0;
  }
//    private VehicleDTO vehicle;
//    private PaymentDTO payment;
}
