package com.levopravoce.backend.socket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WebSocketDestination {
  CHAT("/chat"),
  NOTIFICATION("/notification");

  private final String value;
}
