package com.levopravoce.backend.services.user;

import com.levopravoce.backend.services.authenticate.dto.JwtResponseDTO;
import com.levopravoce.backend.services.authenticate.dto.UserDTO;

public interface UserManagement {
  JwtResponseDTO save(UserDTO userDTO);

  JwtResponseDTO update(UserDTO userDTO);

  void delete(UserDTO userDTO);
}
