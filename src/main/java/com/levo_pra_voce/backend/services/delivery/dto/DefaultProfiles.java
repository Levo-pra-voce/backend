package com.levo_pra_voce.backend.services.delivery.dto;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DefaultProfiles {
    CLIENTE("CLIENTE"),
    ENTREGADOR("ENTREGADOR");

    private final String name;
}
