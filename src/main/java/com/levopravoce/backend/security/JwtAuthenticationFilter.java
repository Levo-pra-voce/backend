package com.levopravoce.backend.security;

import com.levopravoce.backend.entities.User;
import java.io.IOException;

import java.util.Date;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtService;
    private final UserDetailsService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = ObjectUtils.firstNonNull(
            getJwtFromCookie(request),
            request.getHeader("Authorization"),
            request.getParameter("jwt")
        );
        
        if (authHeader != null && !authHeader.startsWith("Bearer ")) {
            authHeader = "Bearer " + authHeader;
        }

        final String jwt;
        final String userEmail;
        if (authHeader == null || StringUtils.isEmpty(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.getUsernameFromToken(jwt);
        if (StringUtils.isNotEmpty(userEmail)
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
}
