package com.levo_pra_voce.backend.services.authenticate.dto;

import com.levo_pra_voce.backend.entities.UserType;
import com.levo_pra_voce.backend.entities.Vehicle;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserDTO {
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private String phone;
  private String cpf;
  private String address;
  private String city;
  private String contact;
  private String state;
  private String zipCode;
  private String country;
  private String status;
  private String complement;
  private String neighborhood;
  private List<Vehicle> vehicles;
  private UserType userType;
}
