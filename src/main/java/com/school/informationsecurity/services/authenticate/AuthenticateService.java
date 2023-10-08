package com.school.informationsecurity.services.authenticate;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.school.informationsecurity.entities.Status;
import com.school.informationsecurity.entities.User;
import com.school.informationsecurity.repository.UserRepository;
import com.school.informationsecurity.security.JwtTokenUtil;
import com.school.informationsecurity.services.authenticate.dto.JwtResponseDTO;
import com.school.informationsecurity.services.authenticate.dto.UserAuthenticationDTO;

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

    public JwtResponseDTO signup(UserAuthenticationDTO dto) throws Exception {

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");

        kpg.initialize(2048);

        KeyPair kp = kpg.genKeyPair();
        Key publicKey = kp.getPublic();
        Key privateKey = kp.getPrivate();

        String passwordHash = passwordEncoder.encode(dto.getPassword());

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

        Key keyPassword = this.generateKey(passwordHash);

        cipher.init(Cipher.ENCRYPT_MODE, keyPassword);

        byte[] encryptedPrivateKey = cipher.doFinal(privateKey.getEncoded());

        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail()).password(passwordEncoder.encode(dto.getPassword()))
                .status(Status.ACTIVE)
                .publicKey(publicKey.getEncoded())
                .privateKey(encryptedPrivateKey)
                .role(Role.USER)
                .build();
        userRepository.save(user);
        String jwt = jwtService.generateToken(user);
        return JwtResponseDTO.builder().token(jwt).build();
    }

    public JwtResponseDTO signin(UserAuthenticationDTO dto) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        String jwt = jwtService.generateToken(user);
        return JwtResponseDTO.builder().token(jwt).build();
    }

    private Key generateKey(String value) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(value.toCharArray(), "salt".getBytes(StandardCharsets.UTF_8), 65536, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        return new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), "AES");
    }
}
