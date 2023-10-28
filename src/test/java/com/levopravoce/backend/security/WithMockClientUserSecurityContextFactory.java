package com.levopravoce.backend.security;

import static com.levopravoce.backend.user.mock.UserMock.CLIENT_USER;

import com.levopravoce.backend.security.annotation.WithMockClientUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockClientUserSecurityContextFactory
    implements WithSecurityContextFactory<WithMockClientUser> {
  @Override
  public SecurityContext createSecurityContext(WithMockClientUser customUser) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();

    UserDetails userDetails = CLIENT_USER;
    Authentication auth =
        new UsernamePasswordAuthenticationToken(
            CLIENT_USER, CLIENT_USER.getPassword(), CLIENT_USER.getAuthorities());
    context.setAuthentication(auth);
    return context;
  }
}
