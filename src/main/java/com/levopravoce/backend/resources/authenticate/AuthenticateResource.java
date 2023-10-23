package com.levopravoce.backend.resources.authenticate;

import com.levopravoce.backend.entities.UserType;
import com.levopravoce.backend.services.authenticate.AuthenticateService;
import com.levopravoce.backend.services.authenticate.dto.JwtResponseDTO;
import com.levopravoce.backend.services.authenticate.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticateResource {
    private final AuthenticateService authenticateService;

  @GetMapping("/signin")
  public ResponseEntity<JwtResponseDTO> signin(@RequestBody UserDTO userAuthenticationDTO) {
      return ResponseEntity.ok(authenticateService.signin(userAuthenticationDTO));
    }

  @PostMapping("/signup/{userType}")
  public ResponseEntity<JwtResponseDTO> signup(
      @PathVariable UserType userType, @RequestBody UserDTO userAuthenticationDTO) {
    return ResponseEntity.ok(authenticateService.signup(userType, userAuthenticationDTO));
  }
}
