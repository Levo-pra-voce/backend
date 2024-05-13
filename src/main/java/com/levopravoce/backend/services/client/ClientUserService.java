package com.levopravoce.backend.services.client;

import com.levopravoce.backend.common.SecurityUtils;
import com.levopravoce.backend.common.UserUtils;
import com.levopravoce.backend.entities.Address;
import com.levopravoce.backend.entities.Status;
import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.entities.UserType;
import com.levopravoce.backend.repository.UserRepository;
import com.levopravoce.backend.security.JwtTokenUtil;
import com.levopravoce.backend.services.authenticate.dto.JwtResponseDTO;
import com.levopravoce.backend.services.authenticate.dto.UserDTO;
import com.levopravoce.backend.services.user.UserManagement;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ClientUserService implements UserManagement {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenUtil jwtService;
  private final UserUtils userUtils;

  @Override
  public JwtResponseDTO save(UserDTO userDTO) {
    userUtils.validateUserFields(userDTO);

    if (userRepository.existsByEmail(userDTO.getEmail())) {
      throw new IllegalArgumentException("Email já foi cadastrado");
    }

    if (userRepository.existsByCpf(userDTO.getCpf())) {
      throw new IllegalArgumentException("CPF já foi cadastrado");
    }

    Address address = userUtils.buildAddressByUserDTO(userDTO);

    User user =
        User.builder()
            .name(userDTO.getName())
            .email(userDTO.getEmail())
            .password(passwordEncoder.encode(userDTO.getPassword()))
            .contact(userDTO.getPhone())
            .status(Status.ACTIVE)
            .userType(UserType.CLIENTE)
            .addresses(List.of(address))
            .build();

    user = userRepository.save(user);

    String jwt = jwtService.generateToken(user);
    return JwtResponseDTO.builder().token(jwt).userType(UserType.CLIENTE).build();
  }

  @Override
  public UserDTO update(User currentUser, UserDTO updatedUser) {
    if (!Objects.isNull(updatedUser.getName())) {
      userUtils.validateName(updatedUser.getName());
      currentUser.setName(updatedUser.getName());
    }

    if (!Objects.isNull(updatedUser.getPhone())) {
      userUtils.validatePhone(updatedUser.getPhone());
      currentUser.setContact(updatedUser.getPhone());
    }

    Address lastAddress = currentUser.getAddresses().stream().findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não encontrado"));

    if (!Objects.equals(updatedUser.getComplement(), lastAddress.getComplement())) {
      boolean complementIsEmpty = updatedUser.getComplement() == null || updatedUser.getComplement().isEmpty();
      lastAddress.setComplement(complementIsEmpty ? null : updatedUser.getComplement());
    }

    if (updatedUser.getZipCode() != null && !Objects.equals(updatedUser.getZipCode(), lastAddress.getZipCode())) {
      Address address = userUtils.buildAddressByUserDTO(updatedUser);
      currentUser.setAddresses(List.of(address));
    }

    var savedUser = userRepository.save(currentUser);
    return savedUser.toDTO();
  }

  @Override
  public void delete(UserDTO userDTO) {
    User user =
        userRepository
            .findByEmail(userDTO.getEmail())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

    user.setStatus(Status.INACTIVE);

    userRepository.save(user);
  }
}
