package com.levopravoce.backend.services.user;

import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.services.authenticate.dto.JwtResponseDTO;
import com.levopravoce.backend.services.authenticate.dto.UserDTO;

public interface UserManagement {

  JwtResponseDTO save(UserDTO userDTO);

  void update(User currentUser, UserDTO updatedUser);

  void delete(User currentUser);
}
