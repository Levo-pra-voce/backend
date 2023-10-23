package com.levopravoce.backend.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {
    ENTREGADOR("ENTREGADOR"),
    CLIENTE("CLIENTE");

    private final String userType;
}
