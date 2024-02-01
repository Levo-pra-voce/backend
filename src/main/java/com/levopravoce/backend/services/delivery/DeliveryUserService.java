package com.levopravoce.backend.services.delivery;

import com.levopravoce.backend.common.UserUtils;
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
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class DeliveryUserService implements UserManagement {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenUtil jwtService;
  private final UserUtils userUtils;

  @Override
  public JwtResponseDTO save(UserDTO userDTO) {

    userUtils.validateUserFields(userDTO);

    if (userDTO.getVehicle() == null) {
      throw new IllegalArgumentException("Vehicle is required");
    }

    if (userRepository.existsByEmailOrCpf(userDTO.getEmail(), userDTO.getCpf())) {
      throw new IllegalArgumentException("Email already exists");
    }

    User user =
        User.builder()
            .name(userDTO.getName())
            .email(userDTO.getEmail())
            .password(passwordEncoder.encode(userDTO.getPassword()))
            .contact(userDTO.getContact())
            .status(Status.ACTIVE)
            .userType(UserType.ENTREGADOR)
            .addresses(List.of(userUtils.buildAddressByUserDTO(userDTO)))
            .vehicles(List.of(Objects.requireNonNull(userDTO.getVehicle())))
            .build();

    userRepository.save(user);
    String jwt = jwtService.generateToken(user);
    return JwtResponseDTO.builder().token(jwt).userType(UserType.ENTREGADOR).build();
  }

  @Override
  public JwtResponseDTO update(UserDTO userDTO) {
    userUtils.validateUserFields(userDTO);

    User user =
        userRepository
            .findByEmail(userDTO.getEmail())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    user.setName(userDTO.getName());
    user.setContact(userDTO.getContact());

    userRepository.save(user);

    String jwt = jwtService.generateToken(user);
    return JwtResponseDTO.builder().token(jwt).userType(UserType.ENTREGADOR).build();
  }

  @Override
  public void delete(UserDTO userDTO) {
    User user =
        userRepository
            .findByEmail(userDTO.getEmail())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    user.setStatus(Status.INACTIVE);

    userRepository.save(user);
  }
}
