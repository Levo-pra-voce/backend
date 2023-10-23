package com.levopravoce.backend.common;

import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;

import com.levopravoce.backend.entities.User;

public class SecurityUtils {
    public static String getCurrentUsername() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
            return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
        } 
        throw new RuntimeException("User not found");
    }

  public static Optional<User> getCurrentUser() {
    if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
      return Optional.of(
          (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
    return Optional.empty();
  }
}
