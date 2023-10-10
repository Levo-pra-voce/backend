package com.levo_pra_voce.backend.services.delivery;

import com.levo_pra_voce.backend.entities.Address;
import com.levo_pra_voce.backend.entities.Status;
import com.levo_pra_voce.backend.entities.User;
import com.levo_pra_voce.backend.entities.UserType;
import com.levo_pra_voce.backend.repository.UserRepository;
import com.levo_pra_voce.backend.security.JwtTokenUtil;
import com.levo_pra_voce.backend.services.authenticate.dto.JwtResponseDTO;
import com.levo_pra_voce.backend.services.delivery.dto.UserDeliveryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtService;

    public JwtResponseDTO createUser(UserDeliveryDTO dto) {

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
                .userType(UserType.ENTREGADOR)
                .addresses(List.of(address))
                .vehicles(Objects.requireNonNull(dto.getVehicles()))
                .build();
        userRepository.save(user);
        String jwt = jwtService.generateToken(user);
        return JwtResponseDTO.builder().token(jwt).userType(UserType.ENTREGADOR).build();
    }
}
