package com.levo_pra_voce.backend.services.user.dto;

import com.levo_pra_voce.backend.entities.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private UserType userType;
}
