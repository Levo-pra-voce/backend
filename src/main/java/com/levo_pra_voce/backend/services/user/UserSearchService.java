package com.levo_pra_voce.backend.services.user;

import com.levo_pra_voce.backend.common.SecurityUtils;
import com.levo_pra_voce.backend.repository.UserRepository;
import com.levo_pra_voce.backend.services.authenticate.dto.UserDTO;
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
                    .userType(user.getUserType())
                    .build())
        .toList();
  }
}
