package com.levo_pra_voce.backend.services.authenticate;

import com.levo_pra_voce.backend.entities.User;
import com.levo_pra_voce.backend.entities.UserType;
import com.levo_pra_voce.backend.repository.UserRepository;
import com.levo_pra_voce.backend.security.JwtTokenUtil;
import com.levo_pra_voce.backend.services.authenticate.dto.JwtResponseDTO;
import com.levo_pra_voce.backend.services.authenticate.dto.UserDTO;
import com.levo_pra_voce.backend.services.user.UserManagement;
import com.levo_pra_voce.backend.services.user.UserManagementDeciderService;
import lombok.RequiredArgsConstructor;
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
