package com.school.informationsecurity.services.user;

import com.school.informationsecurity.common.SecurityUtils;
import org.springframework.stereotype.Service;

import com.school.informationsecurity.repository.UserRepository;
import com.school.informationsecurity.services.user.dto.UserDTO;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDTO getUser(String email) {
        return userRepository.findByEmail(email).map(user -> UserDTO.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build()).orElseThrow(() -> new RuntimeException("User not found")
        );
    }

    public List<UserDTO> getUserList() {
        return userRepository.findAll()
                .stream()
                .filter(user ->
                        Optional.ofNullable(user.getRole()).isPresent() && !user.getEmail().equals(SecurityUtils.getCurrentUsername())
                )
                .map(user -> UserDTO.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build())
            .toList();
    }
}
