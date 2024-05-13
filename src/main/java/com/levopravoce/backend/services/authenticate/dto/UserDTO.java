package com.levopravoce.backend.services.authenticate.dto;

import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.entities.UserType;
import com.levopravoce.backend.entities.Vehicle;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
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
  private byte[] profilePicture;

  public User toEntity() {
    return User.builder()
        .id(id)
        .name(name)
        .email(email)
        .password(password)
        .cpf(cpf)
        .cnh(cnh)
        .contact(phone)
        .build();
  }
}
