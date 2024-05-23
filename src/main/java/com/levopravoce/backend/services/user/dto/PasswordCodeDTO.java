package com.levopravoce.backend.services.user.dto;

import lombok.Data;

@Data
public class PasswordCodeDTO {
  private String email;
  private String code;
  private String password;
}
