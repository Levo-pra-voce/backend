package com.levopravoce.backend.resources.user;

import com.levopravoce.backend.common.SecurityUtils;
import com.levopravoce.backend.services.authenticate.dto.UserDTO;
import com.levopravoce.backend.services.user.UserSearchService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
