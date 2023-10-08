package com.school.informationsecurity.resources.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.school.informationsecurity.common.SecurityUtils;
import com.school.informationsecurity.services.user.UserService;
import com.school.informationsecurity.services.user.dto.UserDTO;

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
