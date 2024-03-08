package com.levopravoce.backend.socket;

import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.socket.dto.WebSocketEventDTO;

public interface WebSocketMessageService<T> {

  void handleMessage(WebSocketEventDTO eventDTO, WebSocketHandler webSocketHandler,
      User currentSessionUser, Object message);

  boolean hasPermission(WebSocketEventDTO eventDTO, Object message);

  WebSocketDestination getDestination();

  Class<T> getMessageType();
}
