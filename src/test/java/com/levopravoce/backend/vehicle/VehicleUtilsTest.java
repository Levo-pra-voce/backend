package com.levopravoce.backend.vehicle;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.levopravoce.backend.common.VehicleUtils;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VehicleUtilsTest {
  private final VehicleUtils vehicleUtils;

  @Test
  @DisplayName("verifica se a color está vazio ou nulo")
  public void givenVehicleWithEmptyColorAndNullColor() {
    String color = "";
    assertThrows(IllegalArgumentException.class, () -> vehicleUtils.validateColor(color));
    assertThrows(IllegalArgumentException.class, () -> vehicleUtils.validateColor(null));
  }

  @Test
  @DisplayName("verifica se a placa está vazio ou nulo")
  public void givenVehicleWithEmptyPlateAndNullPlate() {
    String plate = "";
    assertThrows(IllegalArgumentException.class, () -> vehicleUtils.validatePlate(plate));
    assertThrows(IllegalArgumentException.class, () -> vehicleUtils.validatePlate(null));
  }

  @Test
  @DisplayName("verifica se a placa possui 7 digitos")
  public void givenVehicleWithHaveSevenDigitsPlate() {
    String sixDigitsPlate = "123456";
    assertThrows(IllegalArgumentException.class, () -> vehicleUtils.validatePlate(sixDigitsPlate));
    String sevenDigitsPlate = "1234567";
    vehicleUtils.validatePlate(sevenDigitsPlate);
  }

  @Test
  @DisplayName("verifica se o modelo está vazio ou nulo")
  public void givenVehicleWithEmptyModeloAndNullModelo() {
    String model = "";
    assertThrows(IllegalArgumentException.class, () -> vehicleUtils.validateModel(model));
  }

  @Test
  @DisplayName("verifica se a altura é nula ou negativa")
  public void givenVehicleWithEmptyHeightAndNullHeight() {
    Double height = -10.0;
    assertThrows(IllegalArgumentException.class, () -> vehicleUtils.validateHeight(height));
    assertThrows(IllegalArgumentException.class, () -> vehicleUtils.validateHeight(null));
  }

  @Test
  @DisplayName("verifica se a largura é nula ou negativa")
  public void givenVehicleWithEmptyWidthAndNullWidth() {
    Double width = -10.0;
    assertThrows(IllegalArgumentException.class, () -> vehicleUtils.validateWidth(width));
    assertThrows(IllegalArgumentException.class, () -> vehicleUtils.validateWidth(null));
  }

  @Test
  @DisplayName("verifica se o peso maximo é nulo ou negativo")
  public void givenVehicleWithEmptyMaxWeightAndNullMaxWeight() {
    Double width = -10.0;
    assertThrows(IllegalArgumentException.class, () -> vehicleUtils.validateMaxWeight(width));
    assertThrows(IllegalArgumentException.class, () -> vehicleUtils.validateMaxWeight(null));
  }

  @Test
  @DisplayName("verifica se o renavam é nulo ou vazio")
  public void givenVehicleWithEmptyRenavamAndNullRenavam() {
    String renavam = "";
    assertThrows(IllegalArgumentException.class, () -> vehicleUtils.validateRenavam(renavam));
    assertThrows(IllegalArgumentException.class, () -> vehicleUtils.validateRenavam(null));
  }

  @Test
  @DisplayName("verifica se o renavam possui somente digitos")
  public void givenVehicleWithOnlyDigitsRenavam() {
    String renavam = "177011805d1";
    assertThrows(IllegalArgumentException.class, () -> vehicleUtils.validateRenavam(renavam));
  }

  @Test
  @DisplayName("verifica se o renavam possui 11 digitos")
  public void givenVehicleWithDifferentElevenDigitsRenavam() {
    String invalidRenavamSize = "7701180521";
    assertThrows(IllegalArgumentException.class, () -> vehicleUtils.validateRenavam(invalidRenavamSize));
    String validRenavamSize = "17701180521";
    vehicleUtils.validateRenavam(validRenavamSize);
  }
}

