package com.levo_pra_voce.backend.resources.authenticate;

import com.levo_pra_voce.backend.services.authenticate.dto.JwtResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.levo_pra_voce.backend.services.authenticate.AuthenticateService;
import com.levo_pra_voce.backend.services.authenticate.dto.UserAuthenticationDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticateResource {
    private final AuthenticateService authenticateService;

    @GetMapping("/signin")
    public ResponseEntity<JwtResponseDTO> signin(@RequestBody UserAuthenticationDTO userAuthenticationDTO) {
      return ResponseEntity.ok(authenticateService.signin(userAuthenticationDTO));
    }

    @PostMapping("/signup")
    public ResponseEntity<JwtResponseDTO> signup(@RequestBody UserAuthenticationDTO userAuthenticationDTO) {
      return ResponseEntity.ok(authenticateService.signup(userAuthenticationDTO));
    }
}
