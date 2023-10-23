package com.levopravoce.backend.services.authenticate.dto;

import com.levopravoce.backend.entities.UserType;
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
