package com.levo_pra_voce.backend.services.authenticate.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
public class JwtResponseDTO {
	private final String token;
}
