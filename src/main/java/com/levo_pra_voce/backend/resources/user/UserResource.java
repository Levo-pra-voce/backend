package com.levo_pra_voce.backend.resources.user;

import com.levo_pra_voce.backend.common.SecurityUtils;
import com.levo_pra_voce.backend.services.authenticate.dto.UserDTO;
import com.levo_pra_voce.backend.services.user.UserSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserResource {

  private final UserSearchService userSearchService;

    @GetMapping("/me")
    public UserDTO getUser() {
    return userSearchService.getUser(SecurityUtils.getCurrentUsername());
    }

    @GetMapping("/all")
    public List<UserDTO> getUserList() {
    return userSearchService.getUserList();
    }
}
