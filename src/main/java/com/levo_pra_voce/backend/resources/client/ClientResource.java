package com.levo_pra_voce.backend.resources.client;

import com.levo_pra_voce.backend.services.authenticate.dto.JwtResponseDTO;
import com.levo_pra_voce.backend.services.client.ClientService;
import com.levo_pra_voce.backend.services.client.dto.UserClientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/client")
public class ClientResource {

    private final ClientService clientService;

    @PostMapping("/create/user")
    public JwtResponseDTO createUser(@RequestBody UserClientDTO userClientDTO) {
        return this.clientService.createUser(userClientDTO);
    }
}
