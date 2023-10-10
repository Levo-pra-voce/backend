package com.levo_pra_voce.backend.services.client.dto;

import com.levo_pra_voce.backend.services.authenticate.dto.GenericUserDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserClientDTO extends GenericUserDTO {
}
