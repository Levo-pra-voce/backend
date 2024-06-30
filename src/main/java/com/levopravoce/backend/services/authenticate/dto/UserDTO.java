package com.levopravoce.backend.services.authenticate.dto;

import com.levopravoce.backend.entities.UserType;
import com.levopravoce.backend.entities.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
public class UserDTO {
  private Long id;
  private String name;
  private String email;
  private String password;
  private String phone;
  private String cpf;
  private String cnh;
  private String city;
  private String state;
  private String zipCode;
  private String country;
  private String status;
  private String complement;
  private String neighborhood;
  private String street;
  private UserType userType;
  private Vehicle vehicle;
  private String token;
  private String addressNumber;
  private Double priceBase;
  private Double pricePerKm;
  private Boolean acceptTerms;
  private Byte[] profilePicture;
  private Double averageRating;

  public UserDTO(Long id, String name, String email, String password, String phone, String cpf,
      String cnh, String city, String state, String zipCode, String country, String status,
      String complement, String neighborhood, String street, UserType userType, Vehicle vehicle,
      String token, String addressNumber, Double priceBase, Double pricePerKm, Boolean acceptTerms,
      Byte[] profilePicture, Double averageRating) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.phone = phone;
    this.cpf = cpf;
    this.cnh = cnh;
    this.city = city;
    this.state = state;
    this.zipCode = zipCode;
    this.country = country;
    this.status = status;
    this.complement = complement;
    this.neighborhood = neighborhood;
    this.street = street;
    this.userType = userType;
    this.vehicle = vehicle;
    this.token = token;
    this.addressNumber = addressNumber;
    this.priceBase = priceBase;
    this.pricePerKm = pricePerKm;
    this.acceptTerms = acceptTerms;
    this.profilePicture = profilePicture;
    this.averageRating = 5.0;
  }
}
