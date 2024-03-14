package com.levopravoce.backend.services.authenticate;

import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.entities.UserType;
import com.levopravoce.backend.repository.UserRepository;
import com.levopravoce.backend.security.JwtTokenUtil;
import com.levopravoce.backend.services.authenticate.dto.JwtResponseDTO;
import com.levopravoce.backend.services.authenticate.dto.UserDTO;
import com.levopravoce.backend.services.user.UserManagement;
import com.levopravoce.backend.services.user.UserManagementDeciderService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticateService {
  private final UserRepository userRepository;
  private final JwtTokenUtil jwtService;
  private final AuthenticationManager authenticationManager;
  private final UserManagementDeciderService userManagementDeciderService;

  @SneakyThrows
  public JwtResponseDTO signin(UserDTO dto) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
    User user =
        userRepository
            .findByEmail(dto.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Invalid email"));
    String jwt = jwtService.generateToken(user);
    return JwtResponseDTO.builder().token(jwt).userType(user.getUserType()).build();
  }

  public JwtResponseDTO signup(UserType userType, UserDTO dto) {
    UserManagement userManagement = userManagementDeciderService.getServiceByType(userType);
    return userManagement.save(dto);
  }

  public JwtResponseDTO update(UserType userType, UserDTO dto) {
    UserManagement userManagement = userManagementDeciderService.getServiceByType(userType);
    return userManagement.update(dto);
  }

  public void delete(UserType userType, UserDTO dto) {
    UserManagement userManagement = userManagementDeciderService.getServiceByType(userType);
    userManagement.delete(dto);
  }
}
