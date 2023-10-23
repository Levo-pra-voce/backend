package com.levopravoce.backend.services.authenticate.dto;

import com.levopravoce.backend.entities.UserType;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
public class JwtResponseDTO {
	private final String token;
	private final UserType userType;
}
