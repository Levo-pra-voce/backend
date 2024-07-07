package com.levopravoce.backend.services.delivery;

import com.levopravoce.backend.common.UserUtils;
import com.levopravoce.backend.common.VehicleUtils;
import com.levopravoce.backend.entities.Status;
import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.entities.UserType;
import com.levopravoce.backend.entities.Vehicle;
import com.levopravoce.backend.repository.UserRepository;
import com.levopravoce.backend.repository.VehicleRepository;
import com.levopravoce.backend.security.JwtTokenUtil;
import com.levopravoce.backend.services.authenticate.dto.JwtResponseDTO;
import com.levopravoce.backend.services.authenticate.dto.UserDTO;
import com.levopravoce.backend.services.user.UserManagement;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryUserService implements UserManagement {

  private final UserRepository userRepository;
  private final VehicleRepository vehicleRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenUtil jwtService;
  private final UserUtils userUtils;
  private final VehicleUtils vehicleUtils;

  @Override
  public JwtResponseDTO save(UserDTO userDTO) {
    userUtils.validateNewCommonUser(userDTO);
    userUtils.validateCnh(userDTO.getCnh());
    userUtils.validatePriceBase(userDTO.getPriceBase());
    userUtils.validatePricePerKm(userDTO.getPricePerKm());
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

    vehicleUtils.validateNewVehicle(userDTO.getVehicle());
    User user =
        User.builder()
            .cpf(userDTO.getCpf())
            .cnh(userDTO.getCnh())
            .name(userDTO.getName())
            .email(userDTO.getEmail())
            .password(passwordEncoder.encode(userDTO.getPassword()))
            .contact(userDTO.getPhone())
            .status(Status.ACTIVE)
            .creationDate(LocalDateTime.now())
            .userType(UserType.ENTREGADOR)
            .addresses(List.of(userUtils.buildAddressByUserDTO(userDTO)))
            .vehicles(List.of(Objects.requireNonNull(userDTO.getVehicle())))
            .build();

    userRepository.save(user);
    String jwt = jwtService.generateToken(user);
    return JwtResponseDTO.builder().token(jwt).userType(UserType.ENTREGADOR).build();
  }

  @Transactional
  @Override
  public void update(User currentUser, UserDTO updatedUserDTO) {
    if (updatedUserDTO.getVehicle() == null) {
      throw new IllegalArgumentException("Veiculo não informado");
    }

    if (!Objects.isNull(updatedUserDTO.getName())) {
      userUtils.validateName(updatedUserDTO.getName());
      currentUser.setName(updatedUserDTO.getName());
    }

    if (!Objects.isNull(updatedUserDTO.getPhone())) {
      userUtils.validatePhone(updatedUserDTO.getPhone());
      currentUser.setContact(updatedUserDTO.getPhone());
    }

    vehicleUtils.validateNewVehicle(updatedUserDTO.getVehicle());
    vehicleRepository.disableAllVehiclesByUserId(currentUser.getId());
    List<Vehicle> vehicles = currentUser.getVehicles();
    Vehicle updatedVehicle = updatedUserDTO.getVehicle();
    updatedVehicle.setCreationDate(LocalDateTime.now());
    if (vehicles == null) {
      currentUser.setVehicles(List.of(updatedVehicle));
    } else {
      updatedVehicle.setUser(currentUser);
      updatedVehicle.setActive(true);
      this.vehicleRepository.save(updatedVehicle);
      vehicles.forEach(vehicle -> vehicle.setActive(false));
    }
    userRepository.save(currentUser);
  }

  @Override
  public void delete(User user) {
    user.setStatus(Status.INACTIVE);
    userRepository.save(user);
  }
}
