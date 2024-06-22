package com.levopravoce.backend.socket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WebSocketDestination {
  ORDER_MAP("/order-map");

  private final String value;
}
