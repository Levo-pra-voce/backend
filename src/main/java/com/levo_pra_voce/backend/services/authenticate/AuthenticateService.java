package com.levo_pra_voce.backend.services.authenticate;

import com.levo_pra_voce.backend.entities.User;
import com.levo_pra_voce.backend.repository.UserRepository;
import com.levo_pra_voce.backend.security.JwtTokenUtil;
import com.levo_pra_voce.backend.services.authenticate.dto.JwtResponseDTO;
import com.levo_pra_voce.backend.services.authenticate.dto.UserAuthenticationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticateService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenUtil jwtService;
  private final AuthenticationManager authenticationManager;

  public JwtResponseDTO signin(UserAuthenticationDTO dto) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
    User user =
        userRepository
            .findByEmail(dto.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Invalid email"));
    String jwt = jwtService.generateToken(user);
    return JwtResponseDTO.builder().token(jwt).userType(user.getUserType()).build();
  }
}
