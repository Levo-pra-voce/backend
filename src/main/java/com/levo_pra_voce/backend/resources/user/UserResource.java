package com.levo_pra_voce.backend.resources.user;

import com.levo_pra_voce.backend.common.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.levo_pra_voce.backend.services.user.UserService;
import com.levo_pra_voce.backend.services.user.dto.UserDTO;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserResource {

    private final UserService userService;

    @GetMapping("/me")
    public UserDTO getUser() {
        return userService.getUser(SecurityUtils.getCurrentUsername());
    }

    @GetMapping("/all")
    public List<UserDTO> getUserList() {
        return userService.getUserList();
    }
}
