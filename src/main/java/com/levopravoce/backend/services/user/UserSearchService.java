package com.levopravoce.backend.services.user;

import com.levopravoce.backend.common.SecurityUtils;
import com.levopravoce.backend.repository.UserRepository;
import com.levopravoce.backend.services.authenticate.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSearchService {
  private final UserRepository userRepository;

  public UserDTO getUser(String email) {
    return userRepository
        .findByEmail(email)
        .map(
            user ->
                UserDTO.builder()
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .vehicles(user.getVehicles())
                    .status(user.getStatus().name())
                    .userType(user.getUserType())
                    .build())
        .orElseThrow(() -> new RuntimeException("User not found"));
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
