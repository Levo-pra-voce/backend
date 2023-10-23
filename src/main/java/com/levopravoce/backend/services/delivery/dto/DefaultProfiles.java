package com.levopravoce.backend.services.delivery.dto;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DefaultProfiles {
    CLIENTE("CLIENTE"),
    ENTREGADOR("ENTREGADOR");

    private final String name;
}
