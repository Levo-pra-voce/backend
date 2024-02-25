package com.levopravoce.backend.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.socket.dto.WebSocketEventDTO;
import com.levopravoce.backend.socket.dto.WebSocketEventDTO.HEADERS;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@RequiredArgsConstructor
@Component
public class WebSocketHandler extends AbstractWebSocketHandler {

  public final Map<Long, List<WebSocketSession>> userToActiveSessions = new HashMap<>();
  private final ApplicationContext applicationContext;
  private final Map<String, WebSocketMessageService<?>> webSocketMessageServiceMap = new HashMap<>();
  private final ObjectMapper objectMapper;

  @PostConstruct
  public void init() {
    var webSocketMessageServicesEntries = applicationContext.getBeansOfType(
        WebSocketMessageService.class);
    webSocketMessageServicesEntries.forEach((key, value) -> {
      WebSocketDestination enumDestination = value.getDestination();
      this.webSocketMessageServiceMap.put(enumDestination.getValue(), value);
    });
  }

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
    Principal principal = session.getPrincipal();
    if (principal == null) {
      throw new RuntimeException("No principal found for session: " + session.getId());
    }

    if (!(principal instanceof UsernamePasswordAuthenticationToken)) {
      throw new RuntimeException("Not authenticated user: " + principal);
    }

    User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
    if (!user.isCredentialsNonExpired()) {
      deleteUserSession(session, user);
      throw new RuntimeException("User credentials are expired: " + principal);
    }

    String payload = message.getPayload();
    WebSocketEventDTO webSocketEventDTO = objectMapper.readValue(payload,
        WebSocketEventDTO.class);

    if (!webSocketEventDTO.isValid()) {
      session.sendMessage(new TextMessage("Invalid message"));
      return;
    }

    Map<String, String> headers = webSocketEventDTO.getHeaders();
    WebSocketMessageService<?> webSocketMessageService = webSocketMessageServiceMap.get(
        headers.get(HEADERS.DESTINATION.getValue()));

    if (webSocketMessageService == null) {
      throw new RuntimeException("No handler found for destination: " + headers.get("destination"));
    }

    webSocketEventDTO.setSender(user);

    Object messageDTO = this.objectMapper.readValue(webSocketEventDTO.getBody(),
        webSocketMessageService.getMessageType());
    if (!webSocketMessageService.hasPermission(webSocketEventDTO, messageDTO)) {
      throw new RuntimeException("No permission for user: " + principal);
    }

    webSocketMessageService.handleMessage(webSocketEventDTO, this, messageDTO);
  }

  @Override
  public void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
    throw new UnsupportedOperationException("Binary messages are not supported");
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    Principal principal = session.getPrincipal();
    if (principal == null) {
      session.close();
      return;
    }

    UsernamePasswordAuthenticationToken userToken = (UsernamePasswordAuthenticationToken) principal;
    if (!(userToken.getPrincipal() instanceof User user)) {
      session.close();
      return;
    }

    updateOrAddUserSession(session, user);

    super.afterConnectionEstablished(session);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    Principal principal = session.getPrincipal();
    if (principal == null) {
      return;
    }

    UsernamePasswordAuthenticationToken userToken = (UsernamePasswordAuthenticationToken) principal;
    User user = (User) userToken.getPrincipal();

    deleteUserSession(session, user);

    super.afterConnectionClosed(session, status);
  }

  private void deleteUserSession(WebSocketSession session, User user) {
    List<WebSocketSession> userSessions = userToActiveSessions.get(user.getId());
    if (userSessions != null) {
      userSessions.remove(session);
      userToActiveSessions.put(user.getId(), userSessions);
    }
  }

  private void updateOrAddUserSession(WebSocketSession session, User user) {
    List<WebSocketSession> userSessions = userToActiveSessions.get(user.getId());
    if (userSessions == null) {
      userSessions = new ArrayList<>(List.of(session));
      userToActiveSessions.put(user.getId(), userSessions);
    } else {
      userSessions.add(session);
      userToActiveSessions.put(user.getId(), userSessions);
    }
  }
}
