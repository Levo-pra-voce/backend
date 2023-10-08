package com.school.informationsecurity.resources.authenticate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.school.informationsecurity.services.authenticate.AuthenticateService;
import com.school.informationsecurity.services.authenticate.dto.JwtResponseDTO;
import com.school.informationsecurity.services.authenticate.dto.UserAuthenticationDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticateResource {
    private final AuthenticateService authenticateService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponseDTO> signin(@RequestBody UserAuthenticationDTO userAuthenticationDTO) {
      return ResponseEntity.ok(authenticateService.signin(userAuthenticationDTO));
    }

    @PostMapping("/signup")
    public ResponseEntity<JwtResponseDTO> signup(@RequestBody UserAuthenticationDTO userAuthenticationDTO) throws Exception {
      return ResponseEntity.ok(authenticateService.signup(userAuthenticationDTO));
    }
}
