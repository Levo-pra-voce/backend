package com.levopravoce.backend.services.user;

import com.levopravoce.backend.common.SecurityUtils;
import com.levopravoce.backend.entities.Address;
import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.repository.UserRepository;
import com.levopravoce.backend.security.JwtTokenUtil;
import com.levopravoce.backend.services.authenticate.dto.UserDTO;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSearchService {

  private final UserRepository userRepository;
  private final JwtTokenUtil jwtService;

  public UserDTO getUser(String email) {

    User user = userRepository
        .findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found"));

    String jwt = jwtService.generateToken(user);

    return UserDTO.builder()
        .email(user.getEmail())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .vehicles(user.getVehicles())
        .status(user.getStatus().name())
        .userType(user.getUserType())
        .street(Optional.ofNullable(user.getAddresses())
            .orElse(List.of())
            .stream().findFirst()
            .map(Address::getStreet).orElse(null))
        .token(jwt)
        .build();
  }

  public List<UserDTO> getUserList() {
    return userRepository.findAll().stream()
        .filter(
            user ->
                Optional.ofNullable(user.getUserType()).isPresent()
                    && !user.getEmail().equals(SecurityUtils.getCurrentUsername()))
        .map(
            user ->
                UserDTO.builder()
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .build())
        .toList();
  }
}
