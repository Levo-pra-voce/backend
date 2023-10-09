package com.levo_pra_voce.backend.services.authenticate.dto;

import com.levo_pra_voce.backend.entities.UserType;
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
