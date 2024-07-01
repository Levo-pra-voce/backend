package com.levopravoce.backend.security;

import com.levopravoce.backend.entities.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenUtil jwtService;
  private final UserDetailsService userService;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    String authHeader = firstNonNull(
        getJwtFromCookie(request),
        request.getHeader("Authorization"),
        request.getParameter("jwt")
    );

    if (authHeader != null && !authHeader.startsWith("Bearer ")) {
      authHeader = "Bearer " + authHeader;
    }

    final String jwt;
    final String userEmail;
    if (authHeader == null) {
      filterChain.doFilter(request, response);
      return;
    }
    jwt = authHeader.substring(7);
    try {
      userEmail = jwtService.getUsernameFromToken(jwt);
      if (userEmail != null && !userEmail.isEmpty()
          && SecurityContextHolder.getContext().getAuthentication() == null) {
        User userDetails = (User) userService
            .loadUserByUsername(userEmail);
        if (jwtService.isTokenValid(jwt, userDetails)) {
          Date expirationDate = jwtService.getExpirationDateFromToken(jwt);
          userDetails.setExpirationDate(expirationDate);
          SecurityContext context = SecurityContextHolder.createEmptyContext();
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          context.setAuthentication(authToken);
          SecurityContextHolder.setContext(context);
          Cookie jwtCookie = new Cookie("jwt", jwt);
          jwtCookie.setHttpOnly(true);
          response.addCookie(jwtCookie);
        }
      }
    } catch (ExpiredJwtException e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Token expired");
      return;
    } catch (JwtException e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Invalid token");
      return;
    }
    filterChain.doFilter(request, response);
  }

  private String getJwtFromCookie(HttpServletRequest request) {
    if (request.getCookies() == null) {
      return null;
    }

    for (Cookie cookie : request.getCookies()) {
      if (cookie.getName().equals("jwt") && cookie.isHttpOnly()) {
        return cookie.getValue();
      }
    }
    return null;
  }

  private <T> T firstNonNull(T... values) {
    for (T value : values) {
      if (value != null) {
        return value;
      }
    }
    return null;
  }
}
