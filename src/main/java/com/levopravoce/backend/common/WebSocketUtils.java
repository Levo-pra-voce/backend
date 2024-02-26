package com.levopravoce.backend.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@RequiredArgsConstructor
@Component
public class WebSocketUtils {

  private final ObjectMapper objectMapper;

  public void sendErrorMessage(WebSocketSession session, String message, String error) {
    try {
      session.sendMessage(
          new TextMessage(objectMapper.writeValueAsString(new ErrorMessageDTO(message, error))));
    } catch (IOException e) {
      throw new RuntimeException("Error sending error message to session: " + session.getId(), e);
    }
  }

  public record ErrorMessageDTO(
      String message,
      String error
  ) {

  }
}
