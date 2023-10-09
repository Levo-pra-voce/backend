package com.levo_pra_voce.backend.services.authenticate;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Objects;

import com.levo_pra_voce.backend.entities.User;
import com.levo_pra_voce.backend.services.authenticate.dto.JwtResponseDTO;
import com.levo_pra_voce.backend.services.authenticate.dto.UserAuthenticationDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.levo_pra_voce.backend.entities.Status;
import com.levo_pra_voce.backend.repository.UserRepository;
import com.levo_pra_voce.backend.security.JwtTokenUtil;

import lombok.RequiredArgsConstructor;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

@Service
@RequiredArgsConstructor
public class AuthenticateService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtService;
    private final AuthenticationManager authenticationManager;

    public JwtResponseDTO signup(UserAuthenticationDTO dto) {

        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail()).password(passwordEncoder.encode(dto.getPassword()))
                .status(Status.ACTIVE)
                .userType(Objects.requireNonNull(dto.getUserType()))
                .build();
        userRepository.save(user);
        String jwt = jwtService.generateToken(user);
        return JwtResponseDTO.builder().token(jwt).userType(dto.getUserType()).build();
    }

    public JwtResponseDTO signin(UserAuthenticationDTO dto) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        String jwt = jwtService.generateToken(user);
        return JwtResponseDTO.builder().token(jwt).userType(user.getUserType()).build();
    }
}
