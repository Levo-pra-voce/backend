package com.levo_pra_voce.backend.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {
    ENTREGADOR("ENTREGADOR"),
    CLIENTE("CLIENTE");

    private final String userType;
}
