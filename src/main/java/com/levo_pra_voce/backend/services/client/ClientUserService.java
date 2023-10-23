package com.levo_pra_voce.backend.services.client;

import com.levo_pra_voce.backend.common.UserUtils;
import com.levo_pra_voce.backend.entities.Status;
import com.levo_pra_voce.backend.entities.User;
import com.levo_pra_voce.backend.entities.UserType;
import com.levo_pra_voce.backend.repository.UserRepository;
import com.levo_pra_voce.backend.security.JwtTokenUtil;
import com.levo_pra_voce.backend.services.authenticate.dto.JwtResponseDTO;
import com.levo_pra_voce.backend.services.authenticate.dto.UserDTO;
import com.levo_pra_voce.backend.services.user.UserManagement;
import java.util.List;
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

    if (userRepository.existsByEmailOrCpf(userDTO.getEmail(), userDTO.getCpf())) {
      throw new IllegalArgumentException("Email already exists");
    }

    User user =
        User.builder()
            .firstName(userDTO.getFirstName())
            .lastName(userDTO.getLastName())
            .email(userDTO.getEmail())
            .password(passwordEncoder.encode(userDTO.getPassword()))
            .contact(userDTO.getContact())
            .status(Status.ACTIVE)
            .userType(UserType.CLIENTE)
            .addresses(List.of(userUtils.buildAddressByUserDTO(userDTO)))
            .build();

    userRepository.save(user);

    String jwt = jwtService.generateToken(user);
    return JwtResponseDTO.builder().token(jwt).userType(UserType.CLIENTE).build();
  }

  @Override
  public JwtResponseDTO update(UserDTO userDTO) {

    User user =
        userRepository
            .findByEmail(userDTO.getEmail())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    user.setFirstName(userDTO.getFirstName());
    user.setLastName(userDTO.getLastName());
    user.setContact(userDTO.getContact());

    userRepository.save(user);

    String jwt = jwtService.generateToken(user);
    return JwtResponseDTO.builder().token(jwt).userType(UserType.CLIENTE).build();
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
