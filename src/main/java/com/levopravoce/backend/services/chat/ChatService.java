package com.levopravoce.backend.services.chat;

import com.levopravoce.backend.repository.MessageRepository;
import com.levopravoce.backend.services.chat.dto.MessageRequestDTO;
import com.levopravoce.backend.services.chat.dto.MessageResponseDTO;
import com.levopravoce.backend.socket.WebSocketDestination;
import com.levopravoce.backend.socket.WebSocketHandler;
import com.levopravoce.backend.socket.WebSocketMessageService;
import com.levopravoce.backend.socket.dto.WebSocketEventDTO;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

@Service
@RequiredArgsConstructor
public class ChatService implements WebSocketMessageService<MessageRequestDTO> {

  private final MessageRepository messageRepository;

  @Override
  public void handleMessage(WebSocketEventDTO eventDTO, WebSocketHandler webSocketHandler,
      Object message) {
    MessageRequestDTO messageRequest = (MessageRequestDTO) message;
    List<Long> userIds = messageRepository.getUsersIdsByGroup(messageRequest.getGroupId());
    MessageResponseDTO messageResponse = MessageResponseDTO.builder()
        .message(messageRequest.getMessage())
        .build();

    sendMessage(userIds, messageResponse, webSocketHandler);
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
      MessageResponseDTO messageResponse,
      WebSocketHandler webSocketHandler
  ) {
    userIds.forEach(
        userId -> {
          var userSessions = webSocketHandler.userToActiveSessions.get(userId);
          if (userSessions != null) {
            userSessions.forEach(
                session -> {
                  if (session.isOpen() && messageResponse.getMessage() != null) {
                    try {
                      session.sendMessage(new TextMessage(messageResponse.getMessage()));
                    } catch (IOException e) {
                      throw new RuntimeException(e);
                    }
                  }
                }
            );
          }
        }
    );
  }
}
