package com.levo_pra_voce.backend.services.user;

import com.levo_pra_voce.backend.services.authenticate.dto.JwtResponseDTO;
import com.levo_pra_voce.backend.services.authenticate.dto.UserDTO;

public interface UserManagement {
  JwtResponseDTO save(UserDTO userDTO);

  JwtResponseDTO update(UserDTO userDTO);

  void delete(UserDTO userDTO);
}
