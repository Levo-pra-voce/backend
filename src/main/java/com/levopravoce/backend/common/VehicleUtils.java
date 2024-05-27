package com.levopravoce.backend.common;

import com.levopravoce.backend.entities.Vehicle;
import org.springframework.stereotype.Component;

@Component

public class VehicleUtils {

  public void validateVehicle(Vehicle vehicle) {
    validatePlate(vehicle.getPlate());
    validateModel(vehicle.getModel());
    validateColor(vehicle.getColor());
    validateHeight(vehicle.getHeight());
    validateWidth(vehicle.getWidth());
    validateMaxWeight(vehicle.getMaxWeight());
    validateRenavam(vehicle.getRenavam());
  }

  public void validatePlate(String plate) {
    if (plate == null || plate.isEmpty()) {
      throw new IllegalArgumentException("Placa é obrigatória");
    }
  }

  public void validateModel(String model) {
    if (model == null || model.isEmpty()) {
      throw new IllegalArgumentException("Modelo é obrigatório");
    }
  }

  public void validateColor(String color) {
    if (color == null || color.isEmpty()) {
      throw new IllegalArgumentException("Cor é obrigatória");
    }
  }

  public void validateHeight(Double height) {
    if (height == null) {
      throw new IllegalArgumentException("Altura é obrigatória");
    }

    if (height <= 0) {
      throw new IllegalArgumentException("Altura deve ser maior que zero");
    }
  }

  public void validateWidth(Double width) {
    if (width == null) {
      throw new IllegalArgumentException("Largura é obrigatória");
    }

    if (width <= 0) {
      throw new IllegalArgumentException("Largura deve ser maior que zero");
    }
  }

  public void validateMaxWeight(Double maxWeight) {
    if (maxWeight == null) {
      throw new IllegalArgumentException("Peso máximo é obrigatório");
    }

    if (maxWeight <= 0) {
      throw new IllegalArgumentException("Peso máximo deve ser maior que zero");
    }
  }

  public void validateRenavam(String renavam) {
    if (renavam == null || renavam.isEmpty()) {
      throw new IllegalArgumentException("Renavam é obrigatório");
    }

    if (renavam.length() != 11) {
      throw new IllegalArgumentException("Renavam deve ter 11 caracteres");
    }
  }
}
