package com.school.informationsecurity.services.authenticate.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAuthenticationDTO {
    private String password;
    private String email;
    private String firstName;
    private String lastName;
}
