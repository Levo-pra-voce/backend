package com.levo_pra_voce.backend.resources.authenticate;

import com.levo_pra_voce.backend.entities.UserType;
import com.levo_pra_voce.backend.services.authenticate.AuthenticateService;
import com.levo_pra_voce.backend.services.authenticate.dto.JwtResponseDTO;
import com.levo_pra_voce.backend.services.authenticate.dto.UserDTO;
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
