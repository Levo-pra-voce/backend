package com.levopravoce.backend.services.delivery.dto;

import com.levopravoce.backend.entities.Vehicle;
import com.levopravoce.backend.services.authenticate.dto.GenericUserDTO;
import lombok.Data;

import java.util.List;

@Data
public class UserDeliveryDTO extends GenericUserDTO {
    private List<Vehicle> vehicles;
}
