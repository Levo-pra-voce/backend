package com.levopravoce.backend.resources.authenticate;

import com.levopravoce.backend.entities.UserType;
import com.levopravoce.backend.services.authenticate.AuthenticateService;
import com.levopravoce.backend.services.authenticate.dto.JwtResponseDTO;
import com.levopravoce.backend.services.authenticate.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticateResource {
    private final AuthenticateService authenticateService;

  @PostMapping("/login")
  public ResponseEntity<JwtResponseDTO> signin(@RequestBody UserDTO userAuthenticationDTO) {
      return ResponseEntity.ok(authenticateService.signin(userAuthenticationDTO));
    }

  @PostMapping("/register/{userType}")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<JwtResponseDTO> signup(
      @PathVariable UserType userType, @RequestBody UserDTO userAuthenticationDTO) {
    return ResponseEntity.ok(authenticateService.signup(userType, userAuthenticationDTO));
  }
}
