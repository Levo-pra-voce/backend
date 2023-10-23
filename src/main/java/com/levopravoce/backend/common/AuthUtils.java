package com.levopravoce.backend.common;

import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {
  public boolean emailIsSame(String email) {
    return Optional.ofNullable(email).filter(e -> e.equals(SecurityUtils.getCurrentUsername())).isPresent();
  }
}
