package com.levopravoce.backend.services.delivery;

import com.levopravoce.backend.common.UserUtils;
import com.levopravoce.backend.common.VehicleUtils;
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
  private final VehicleUtils vehicleUtils;

  @Override
  public JwtResponseDTO save(UserDTO userDTO) {
    userUtils.validateCommonUserFields(userDTO);
    userUtils.validateCnh(userDTO.getCnh());
    if (userRepository.existsByEmail(userDTO.getEmail())) {
      throw new IllegalArgumentException("Email já foi cadastrado");
    }

    if (userRepository.existsByCpf(userDTO.getCpf())) {
      throw new IllegalArgumentException("CPF já foi cadastrado");
    }

    if (userRepository.existsByCnh(userDTO.getCnh())) {
      throw new IllegalArgumentException("CNH já foi cadastrada");
    }

    if (userDTO.getVehicle() == null) {
      throw new IllegalArgumentException("Veiculo não informado");
    }

    vehicleUtils.validateVehicle(userDTO.getVehicle());

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

    userRepository.save(user);
  }

  @Override
  public void delete(User user) {
    user.setStatus(Status.INACTIVE);

    userRepository.save(user);
  }
}
