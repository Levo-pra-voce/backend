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
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
      throw new IllegalArgumentException("Veiculo não informado");
    }

    if (userRepository.existsByEmail(userDTO.getEmail())) {
      throw new IllegalArgumentException("Email já foi cadastrado");
    }

    if (userRepository.existsByCpf(userDTO.getCpf())) {
      throw new IllegalArgumentException("CPF já foi cadastrado");
    }

    User user =
        User.builder()
            .cpf(userDTO.getCpf())
            .cnh(userDTO.getCnh())
            .name(userDTO.getName())
            .email(userDTO.getEmail())
            .password(passwordEncoder.encode(userDTO.getPassword()))
            .contact(userDTO.getPhone())
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
  public void update(User user, UserDTO updatedUser) {
    userUtils.validateName(updatedUser.getName());
    userUtils.validatePhone(updatedUser.getPhone());

    user.setName(updatedUser.getName());
    user.setContact(updatedUser.getPhone());

    var savedUser = userRepository.save(user);

    savedUser.toDTO();
  }

  @Override
  public void delete(User user) {
    user.setStatus(Status.INACTIVE);

    userRepository.save(user);
  }
}
