package com.levopravoce.backend.common;

import com.levopravoce.backend.entities.Vehicle;
import com.levopravoce.backend.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class VehicleUtils {
  private final VehicleRepository vehicleRepository;

  public void validateNewVehicle(Vehicle vehicle) {
    validatePlate(vehicle.getPlate());
    validateModel(vehicle.getModel());
    validateColor(vehicle.getColor());
    validateHeight(vehicle.getHeight());
    validateWidth(vehicle.getWidth());
    validateMaxWeight(vehicle.getMaxWeight());
    validateRenavam(vehicle.getRenavam());
    validatePriceBase(vehicle.getPriceBase());
    validatePricePerKm(vehicle.getPricePerKm());
  }

  public void validateUpdateVehicle(Vehicle vehicle) {
    validateModel(vehicle.getModel());
    validateColor(vehicle.getColor());
    validateHeight(vehicle.getHeight());
    validateWidth(vehicle.getWidth());
    validateMaxWeight(vehicle.getMaxWeight());
    validateRenavam(vehicle.getRenavam());
    validatePriceBase(vehicle.getPriceBase());
    validatePricePerKm(vehicle.getPricePerKm());
  }

  public void validatePlate(String plate) {
    if (plate == null || plate.isEmpty()) {
      throw new IllegalArgumentException("Placa é obrigatória");
    }

    if (plate.length() != 7) {
      throw new IllegalArgumentException("Placa deve ter 7 caracteres");
    }

    Long total = vehicleRepository.getTotalByPlate(plate);

    if (total > 0) {
      throw new IllegalArgumentException("Placa já cadastrada");
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

    if (!NumberUtils.isDigits(renavam)) {
      throw new IllegalArgumentException("Renavam deve conter apenas números");
    }

    Long total = vehicleRepository.getTotalByRenavam(renavam);
    if (total > 0) {
      throw new IllegalArgumentException("Renavam já cadastrado");
    }
  }

  public void validatePricePerKm(Double price) {
    if (price == null) {
      throw new IllegalArgumentException("Preço cobrado por quilômetro rodado é obrigatório");
    }
    if (price < 0) {
      throw new IllegalArgumentException(
          "Preço cobrado por quilômetro rodado tem que ser maior que zero");
    }
  }

  public void validatePriceBase(Double price) {
    if (price == null) {
      throw new IllegalArgumentException("Preço base por viagem é obrigatório");
    }
    if (price < 0) {
      throw new IllegalArgumentException("Preço base por viagem tem que ser maior que zero");
    }
  }
}
