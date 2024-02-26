package com.levopravoce.backend.services.chat;

import com.levopravoce.backend.common.WebSocketUtils;
import com.levopravoce.backend.repository.MessageRepository;
import com.levopravoce.backend.services.chat.dto.MessageRequestDTO;
import com.levopravoce.backend.services.chat.dto.MessageResponseDTO;
import com.levopravoce.backend.socket.WebSocketDestination;
import com.levopravoce.backend.socket.WebSocketHandler;
import com.levopravoce.backend.socket.WebSocketMessageService;
import com.levopravoce.backend.socket.dto.WebSocketEventDTO;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

@Service
@RequiredArgsConstructor
public class ChatService implements WebSocketMessageService<MessageRequestDTO> {

  private final MessageRepository messageRepository;
  private final WebSocketUtils webSocketUtils;

  @Override
  public void handleMessage(WebSocketEventDTO eventDTO, WebSocketHandler webSocketHandler,
      Object message) {
    MessageRequestDTO messageRequest = (MessageRequestDTO) message;
    List<Long> userIds = messageRepository.getUsersIdsByGroup(messageRequest.getGroupId());
    MessageResponseDTO messageResponse = MessageResponseDTO.builder()
        .message(messageRequest.getMessage())
        .build();

    if (eventDTO.getSender() != null) {
      var userID = eventDTO.getSender().getId();
      sendMessage(userIds, userID, messageResponse, webSocketHandler);
      return;
    }

    sendErrorMessage(webSocketHandler, eventDTO, "Erro ao mandar a mensagem",
        "Usuário não encontrado");
  }

  @Override
  public boolean hasPermission(WebSocketEventDTO eventDTO, Object message) {
    MessageRequestDTO messageRequest = (MessageRequestDTO) message;
    if (messageRequest.getGroupId() == null) {
      return false;
    }
    return this.messageRepository.haveAccessForGroup(messageRequest.getGroupId(),
        eventDTO.getSender().getId());
  }

  @Override
  public WebSocketDestination getDestination() {
    return WebSocketDestination.CHAT;
  }

  @Override
  public Class<MessageRequestDTO> getMessageType() {
    return MessageRequestDTO.class;
  }

  public void sendMessage(
      List<Long> userIds,
      Long sendUserId,
      MessageResponseDTO messageResponse,
      WebSocketHandler webSocketHandler
  ) {
    userIds.stream().filter(
        userId -> !Objects.equals(userId, sendUserId)
    ).forEach(
        userId -> {
          var userSessions = webSocketHandler.userToActiveSessions.get(userId);
          if (userSessions != null) {
            userSessions.forEach(
                session -> {
                  if (session.isOpen() && messageResponse.getMessage() != null) {
                    try {
                      session.sendMessage(new TextMessage(messageResponse.getMessage()));
                    } catch (IOException e) {
                      webSocketUtils.sendErrorMessage(session, "Erro ao mandar a mensagem",
                          e.getMessage());
                    }
                  }
                }
            );
          }
        }
    );
  }

  public void sendErrorMessage(
      WebSocketHandler webSocketHandler,
      WebSocketEventDTO eventDTO,
      String message,
      String error
  ) {
    var userSessions = webSocketHandler.userToActiveSessions.get(eventDTO.getSender().getId());
    if (userSessions != null) {
      userSessions.forEach(
          session -> webSocketUtils.sendErrorMessage(session, message, error)
      );
    }
  }
}
