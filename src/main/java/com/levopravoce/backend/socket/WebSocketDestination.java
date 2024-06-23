package com.levopravoce.backend.socket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WebSocketDestination {
  ORDER_MAP("/order-map"),
  ORDER_PAYMENT("/order-payment"),
  ;

  private final String value;
}
