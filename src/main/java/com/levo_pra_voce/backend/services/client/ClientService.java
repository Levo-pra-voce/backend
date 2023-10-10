package com.levo_pra_voce.backend.services.client;

import com.levo_pra_voce.backend.entities.Address;
import com.levo_pra_voce.backend.entities.Status;
import com.levo_pra_voce.backend.entities.User;
import com.levo_pra_voce.backend.entities.UserType;
import com.levo_pra_voce.backend.repository.UserRepository;
import com.levo_pra_voce.backend.security.JwtTokenUtil;
import com.levo_pra_voce.backend.services.authenticate.dto.JwtResponseDTO;
import com.levo_pra_voce.backend.services.client.dto.UserClientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtService;
    public JwtResponseDTO createUser(UserClientDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        final Address address = Address.builder()
                .complement(dto.getComplement())
                .city(dto.getCity())
                .state(dto.getState())
                .zipCode(dto.getZipCode())
                .creationDate(LocalDateTime.now())
                .neighborhood(dto.getNeighborhood())
                .active(true)
                .build();
        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail()).password(passwordEncoder.encode(dto.getPassword()))
                .status(Status.ACTIVE)
                .contact(dto.getContact())
                .userType(UserType.CLIENTE)
                .addresses(List.of(address))
                .build();
        userRepository.save(user);
        String jwt = jwtService.generateToken(user);
        return JwtResponseDTO.builder().token(jwt).userType(UserType.CLIENTE).build();
    }
}
