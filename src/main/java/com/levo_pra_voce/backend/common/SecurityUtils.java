package com.levo_pra_voce.backend.common;

import org.springframework.security.core.context.SecurityContextHolder;

import com.levo_pra_voce.backend.entities.User;

public class SecurityUtils {
    public static String getCurrentUsername() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
            return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
        } 
        throw new RuntimeException("User not found");
    }
}
