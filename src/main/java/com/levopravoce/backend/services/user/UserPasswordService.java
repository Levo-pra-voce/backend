package com.levopravoce.backend.services.user;

import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class UserPasswordService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void changePassword(User currentUser, String newPassword) {
    String oldPasswordHash = currentUser.getPassword();

    if (passwordEncoder.matches(newPassword, oldPasswordHash)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Senha não pode ser igual");
    }

    currentUser.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(currentUser);
  }
}
