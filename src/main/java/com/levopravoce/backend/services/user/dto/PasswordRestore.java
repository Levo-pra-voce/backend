package com.levopravoce.backend.services.user.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PasswordRestore {
    private LocalDateTime restoreTime;
    private String email;
}
