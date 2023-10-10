package com.levo_pra_voce.backend.services.delivery.dto;

import com.levo_pra_voce.backend.entities.Vehicle;
import com.levo_pra_voce.backend.services.authenticate.dto.GenericUserDTO;
import lombok.Data;

import java.util.List;

@Data
public class UserDeliveryDTO extends GenericUserDTO {
    private List<Vehicle> vehicles;
}
