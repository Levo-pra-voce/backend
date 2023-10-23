package com.levopravoce.backend.authenticate;

import static org.junit.jupiter.api.Assertions.*;

import com.levopravoce.backend.common.AuthUtils;
import com.levopravoce.backend.common.SecurityUtils;
import com.levopravoce.backend.config.CustomTestConfiguration;
import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.user.mock.UserMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@Import(CustomTestConfiguration.class)
@ContextConfiguration(classes = {AuthUtils.class})
public class AuthUtilsTest {
  @Autowired private AuthUtils authUtils;

  @WithMockUser(value = UserMock.CLIENT_EMAIL)
  @DisplayName("Verificao se a configuração de segurança está funcionando, com @TestConfiguration")
  @Test
  public void givenAuthenticatedUser_whenGetCurrentUser_thenSuccess() {
    User user = SecurityUtils.getCurrentUser().orElseThrow();
    assertEquals(user.getEmail(), UserMock.CLIENT_USER.getEmail());
  }
}
