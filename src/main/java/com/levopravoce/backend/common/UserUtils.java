package com.levopravoce.backend.common;

import com.levopravoce.backend.entities.Address;
import com.levopravoce.backend.services.authenticate.dto.UserDTO;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

  private final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

  public Address buildAddressByUserDTO(UserDTO userDTO) {

    if (
        userDTO.getComplement() == null ||
            userDTO.getComplement().isEmpty() ||
            userDTO.getCity() == null ||
            userDTO.getCity().isEmpty() ||
            userDTO.getState() == null ||
            userDTO.getState().isEmpty() ||
            userDTO.getZipCode() == null ||
            userDTO.getZipCode().isEmpty() ||
            userDTO.getNeighborhood() == null ||
            userDTO.getNeighborhood().isEmpty()
    ) {
      throw new IllegalArgumentException("Address fields are required");
    }

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

    if (userDTO.getPassword().length() < 6) {
      throw new IllegalArgumentException("Password must have at least 6 characters");
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

    if (!userDTO.getCpf().matches("[0-9]+")) {
      throw new IllegalArgumentException("CPF must have only numbers");
    }
  }
}
