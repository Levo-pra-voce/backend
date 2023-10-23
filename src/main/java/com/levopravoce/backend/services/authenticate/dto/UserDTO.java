package com.levopravoce.backend.services.authenticate.dto;

import com.levopravoce.backend.entities.Vehicle;
import java.util.List;
import lombok.Builder;
import lombok.Data;

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
}
