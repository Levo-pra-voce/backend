package com.levo_pra_voce.backend.services.authenticate.dto;

import com.levo_pra_voce.backend.entities.UserType;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
@Builder
public class UserAuthenticationDTO {
    @NonNull
    private String password;
    @NonNull
    private String email;
    private String firstName;
    private String lastName;
    private UserType userType;
}
