package com.levo_pra_voce.backend.common;

import com.levo_pra_voce.backend.entities.Address;
import com.levo_pra_voce.backend.services.authenticate.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserUtils {
  private final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

  public Address buildAddressByUserDTO(UserDTO userDTO) {
    return Address.builder()
        .complement(userDTO.getComplement())
        .city(userDTO.getCity())
        .state(userDTO.getState())
        .zipCode(userDTO.getZipCode())
        .creationDate(LocalDateTime.now())
        .neighborhood(userDTO.getNeighborhood())
        .active(true)
        .build();
  }

  public void validateUserFields(UserDTO userDTO) {
    if (userDTO.getFirstName() == null || userDTO.getFirstName().isEmpty()) {
      throw new IllegalArgumentException("First name is required");
    }

    if (userDTO.getLastName() == null || userDTO.getLastName().isEmpty()) {
      throw new IllegalArgumentException("Last name is required");
    }

    if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty()) {
      throw new IllegalArgumentException("Email is required");
    }

    if (!userDTO.getEmail().matches(EMAIL_REGEX)) {
      throw new IllegalArgumentException("Email is invalid");
    }

    if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
      throw new IllegalArgumentException("Password is required");
    }

    if (userDTO.getPhone() == null || userDTO.getPhone().isEmpty()) {
      throw new IllegalArgumentException("Phone is required");
    }

    if (userDTO.getPhone().contains(" ")) {
      throw new IllegalArgumentException("Phone must not contain spaces");
    }

    if (userDTO.getCpf() == null || userDTO.getCpf().isEmpty()) {
      throw new IllegalArgumentException("CPF is required");
    }

    if (userDTO.getCpf().length() != 11) {
      throw new IllegalArgumentException("CPF must have 11 digits");
    }

    if (userDTO.getAddress() == null || userDTO.getAddress().isEmpty()) {
      throw new IllegalArgumentException("Address is required");
    }
  }
}
