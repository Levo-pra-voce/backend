package com.levopravoce.backend.common;

import com.levopravoce.backend.entities.Address;
import com.levopravoce.backend.services.authenticate.dto.UserDTO;
import com.levopravoce.backend.services.delivery.dto.ViaCepResponseDTO;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class UserUtils {

  private static final String VIA_CEP_URL = "https://viacep.com.br/ws/";
  private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

  private final RestTemplate restTemplate;

  public Address buildAddressByUserDTO(UserDTO userDTO) {
    if (userDTO.getZipCode() == null) {
      throw new IllegalArgumentException("Zip code is required");
    }

    var viaCepResponse = restTemplate.getForObject(VIA_CEP_URL + userDTO.getZipCode() + "/json",
        ViaCepResponseDTO.class);

    if (viaCepResponse == null) {
      throw new IllegalArgumentException("Zip code not found");
    }

    return Address.builder()
        .complement(viaCepResponse.getComplemento())
        .city(viaCepResponse.getLocalidade())
        .state(viaCepResponse.getUf())
        .street(viaCepResponse.getLogradouro())
        .number(userDTO.getAddressNumber())
        .zipCode(viaCepResponse.getCep().replace("-", ""))
        .creationDate(LocalDateTime.now())
        .neighborhood(viaCepResponse.getBairro())
        .active(true)
        .build();
  }

  public void validateUserFields(UserDTO userDTO) {
    if (userDTO.getName() == null || userDTO.getName().isEmpty()) {
      throw new IllegalArgumentException("Name is required");
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
