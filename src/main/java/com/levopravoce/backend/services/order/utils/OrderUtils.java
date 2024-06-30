package com.levopravoce.backend.services.order.utils;

import com.levopravoce.backend.entities.Order;
import com.levopravoce.backend.services.order.dto.OrderDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class OrderUtils {

  public void validateNewOrder(OrderDTO orderDTO) {
    validateHeight(orderDTO.getHeight());
    validateWidth(orderDTO.getWidth());
    validadeMaxWeight(orderDTO.getMaxWeight());
    validateDeliveryDate(orderDTO.getDeliveryDate());
    validateOriginLatitude(orderDTO.getOriginLatitude());
    validateOriginLongitude(orderDTO.getOriginLongitude());
    validateDestinationLatitude(orderDTO.getDestinationLatitude());
    validateDestinationLongitude(orderDTO.getDestinationLongitude());
  }

  public void validateHeight(Double height) {
    if (height == null || height <= 0) {
      throw new IllegalArgumentException("Altura inválida");
    }
  }

  public void validateWidth(Double width) {
    if (width == null || width <= 0) {
      throw new IllegalArgumentException("Largura inválida");
    }
  }

  public void validadeMaxWeight(Double maxWeight) {
    if (maxWeight == null || maxWeight <= 0) {
      throw new IllegalArgumentException("Peso máximo inválido");
    }
  }

  public void validateDeliveryDate(LocalDate deliveryDate) {
    if (deliveryDate == null) {
      throw new IllegalArgumentException("Data de entrega inválida");
    }
    if (deliveryDate.isBefore(LocalDate.now())) {
      throw new IllegalArgumentException("Data de entrega não pode ser anterior a data atual");
    }
  }

  public void validateOriginLatitude(Double originLatitude) {
    if (originLatitude == null || originLatitude < -90 || originLatitude > 90) {
      throw new IllegalArgumentException("Latitude de origem inválida");
    }
  }

  public void validateOriginLongitude(Double originLongitude) {
    if (originLongitude == null) {
      throw new IllegalArgumentException("Longitude de origem inválida");
    }
  }

  public void validateDestinationLatitude(Double destinationLatitude) {
    if (destinationLatitude == null) {
      throw new IllegalArgumentException("Latitude de destino inválida");
    }
  }

  public void validateDestinationLongitude(Double destinationLongitude) {
    if (destinationLongitude == null) {
      throw new IllegalArgumentException("Longitude de destino inválida");
    }
  }

  public void verifyIfOrderAlreadyHaveDeliveryMan(Order order, Long deliveryManId) {
    Optional.ofNullable(order.getRequests()).orElse(List.of()).forEach(request -> {
      if (Objects.equals(request.getDeliveryman().getId(), deliveryManId)) {
        throw new IllegalArgumentException("Entregador já atribuído ao pedido.");
      }
    });
  }
}
