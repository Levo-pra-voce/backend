package com.levopravoce.backend.resources.request;

import com.levopravoce.backend.common.SecurityUtils;
import com.levopravoce.backend.entities.Order;
import com.levopravoce.backend.services.request.RequestService;
import com.levopravoce.backend.services.request.dto.RequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/request")
@RequiredArgsConstructor
public class RequestResouces {
    private final RequestService requestService;

    @PostMapping
    public RequestDTO createRequest(
           @RequestBody RequestDTO requestDTO
    ) {
        return this.requestService.createRequest(requestDTO);
    }

    @PostMapping("/update")
    public RequestDTO updateRequest(
           @RequestBody RequestDTO requestDTO
    ) {
        return this.requestService.updateRequest(requestDTO);
    }
}
