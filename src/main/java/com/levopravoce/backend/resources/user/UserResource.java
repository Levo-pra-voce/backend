package com.levopravoce.backend.resources.user;

import com.levopravoce.backend.common.SecurityUtils;
import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.services.authenticate.dto.UserDTO;
import com.levopravoce.backend.services.user.UserManagementDeciderService;
import com.levopravoce.backend.services.user.UserPasswordService;
import com.levopravoce.backend.services.user.UserSearchService;

import java.util.List;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserResource {

    private final UserSearchService userSearchService;
    private final UserPasswordService userPasswordService;
    private final UserManagementDeciderService userManagementDeciderService;

    @GetMapping("/me")
    public UserDTO getUser() {
        return userSearchService.getUser(SecurityUtils
            .getCurrentUser()
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
            )
        );
    }

    @GetMapping("/all")
    public List<UserDTO> getUserList() {
        return userSearchService.getUserList();
    }

    @PostMapping("/restore-password/{email}")
    public void restorePassword(@PathVariable String email) throws MessagingException {
        userPasswordService.restorePassword(email);
    }

    @GetMapping("/exist-code/{code}")
    public ResponseEntity<Void> existCode(@PathVariable String code) {
        boolean existCode = userPasswordService.existCode(code);
        if (!existCode) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Code not found");
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-password")
    public void changePassword(String code, String password) {
        userPasswordService.changePassword(code, password);
    }

    @PutMapping
    public void update(@RequestBody UserDTO userDTO) {
        var currentUser = SecurityUtils.getCurrentUser().orElseThrow();
        var userService = userManagementDeciderService.getServiceByType(currentUser.getUserType());
        userService.update(currentUser, userDTO);
    }
}
