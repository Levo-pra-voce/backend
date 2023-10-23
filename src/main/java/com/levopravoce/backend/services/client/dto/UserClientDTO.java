package com.levopravoce.backend.services.client.dto;

import com.levopravoce.backend.services.authenticate.dto.GenericUserDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserClientDTO extends GenericUserDTO {
}
