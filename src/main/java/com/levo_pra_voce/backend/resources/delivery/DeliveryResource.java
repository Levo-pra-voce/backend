package com.levo_pra_voce.backend.resources.delivery;

import com.levo_pra_voce.backend.services.authenticate.dto.JwtResponseDTO;
import com.levo_pra_voce.backend.services.delivery.DeliveryService;
import com.levo_pra_voce.backend.services.delivery.dto.UserDeliveryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryResource {

    private final DeliveryService deliveryService;
    @PostMapping("/create/user")
    public JwtResponseDTO createUser(@RequestBody UserDeliveryDTO userDeliveryDTO) {
        return this.deliveryService.createUser(userDeliveryDTO);
    }
}
