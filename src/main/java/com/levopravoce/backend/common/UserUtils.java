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
      throw new IllegalArgumentException("CEP é obrigatório");
    }

    var viaCepResponse = restTemplate.getForObject(VIA_CEP_URL + userDTO.getZipCode() + "/json",
        ViaCepResponseDTO.class);

    if (viaCepResponse == null || viaCepResponse.getCep() == null) {
      throw new IllegalArgumentException("CEP inválido");
    }

    return Address.builder()
        .complement(userDTO.getComplement())
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

  public void validateNewCommonUser(UserDTO userDTO) {
    validateAcceptTerms(userDTO.getAcceptTerms());
    validateName(userDTO.getName());
    validateEmail(userDTO.getEmail());
    validatePassword(userDTO.getPassword());
    validatePhone(userDTO.getPhone());
    validateCpf(userDTO.getCpf());
  }

  public void validatePriceBase(Double priceBase) {
    if (priceBase == null || priceBase < 0) {
      throw new IllegalArgumentException("Preço base é obrigatório e deve ser maior");
    }
  }

  public void validatePricePerKm(Double pricePerKm) {
    if (pricePerKm == null || pricePerKm < 0) {
      throw new IllegalArgumentException("Preço por km é obrigatório e deve ser maior");
    }
  }

  public void validateAcceptTerms(Boolean acceptTerms) {
    if (acceptTerms == null || !acceptTerms) {
      throw new IllegalArgumentException("Termos de uso devem ser aceitos");
    }
  }

  public void validateName(String name) {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Nome é obrigatório");
    }
  }

  public void validateEmail(String email) {
    if (email == null || email.isEmpty()) {
      throw new IllegalArgumentException("Email é obrigatório");
    }

    if (!email.matches(EMAIL_REGEX)) {
      throw new IllegalArgumentException("Email inválido");
    }
  }

  public void validatePassword(String password) {
    if (password == null || password.isEmpty()) {
      throw new IllegalArgumentException("Senha é obrigatória");
    }

    if (passwordIsInvalid(password)) {
      throw new IllegalArgumentException("Senha deve conter ao menos 8 caracteres, uma letra maiúscula, uma letra minúscula e um número");
    }
  }

  public void validateCnh(String cnh) {
    if (cnh == null || cnh.isEmpty()) {
      throw new IllegalArgumentException("CNH é obrigatória");
    }

    if (cnh.length() != 11) {
      throw new IllegalArgumentException("CNH deve conter 11 dígitos");
    }
  }

  public void validatePhone(String phone) {
    if (phone == null || phone.isEmpty()) {
      throw new IllegalArgumentException("Telefone é obrigatório");
    }

    if (phone.contains(" ")) {
      throw new IllegalArgumentException("Telefone não pode conter espaços");
    }
  }

  public void validateCpf(String cpf) {
    if (cpf == null || cpf.isEmpty()) {
      throw new IllegalArgumentException("CPF é obrigatório");
    }

    if (cpf.length() != 11) {
      throw new IllegalArgumentException("CPF deve conter 11 dígitos");
    }

    if (!cpf.matches("[0-9]+")) {
      throw new IllegalArgumentException("CPF deve conter apenas números");
    }
  }

  public boolean passwordIsInvalid(String password) {
    return password == null || !password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");
  }
}
