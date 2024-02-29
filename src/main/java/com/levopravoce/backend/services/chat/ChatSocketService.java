package com.levopravoce.backend.services.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.levopravoce.backend.common.WebSocketUtils;
import com.levopravoce.backend.entities.Message;
import com.levopravoce.backend.repository.MessageRepository;
import com.levopravoce.backend.services.chat.dto.MessageDTO;
import com.levopravoce.backend.socket.WebSocketDestination;
import com.levopravoce.backend.socket.WebSocketHandler;
import com.levopravoce.backend.socket.WebSocketMessageService;
import com.levopravoce.backend.socket.dto.WebSocketEventDTO;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

@Service
@RequiredArgsConstructor
public class ChatSocketService implements WebSocketMessageService<MessageDTO> {

  private final MessageRepository messageRepository;
  private final WebSocketUtils webSocketUtils;
  private final ObjectMapper objectMapper;

  @Override
  public void handleMessage(WebSocketEventDTO eventDTO, WebSocketHandler webSocketHandler,
      Object message) {
    MessageDTO messageRequest = (MessageDTO) message;
    List<Long> userIds = messageRepository.getUsersIdsByGroup(messageRequest.getChannelId());
    if (eventDTO.getSender() != null) {
      Optional<String> errorMessage = validateMessage(messageRequest);
      if (errorMessage.isPresent()) {
        sendErrorMessage(webSocketHandler, eventDTO, "Erro ao mandar a mensagem",
            errorMessage.get());
        return;
      }

      byte[] messageBytes = switch (messageRequest.getType()) {
        case TEXT -> messageRequest.getMessage().getBytes();
        case IMAGE -> {
          Base64.Decoder decoder = Base64.getDecoder();
          yield decoder.decode(messageRequest.getMessage());
        }
      };

      this.messageRepository.save(Message.builder()
          .date(Optional
              .ofNullable(messageRequest.getTimestamp())
              .map(timestamp -> LocalDateTime.ofInstant(
                  Instant.ofEpochSecond(timestamp),
                  ZoneId.of("UTC")
              )).orElse(LocalDateTime.now()))
          .active(true)
          .message(messageBytes)
          .messageType(messageRequest.getType())
          .group(messageRepository.getGroupById(messageRequest.getChannelId()))
          .sender(eventDTO.getSender().toEntity())
          .build()
      );

      MessageDTO messageResponse = MessageDTO.builder()
          .message(messageRequest.getMessage())
          .type(messageRequest.getType())
          .sender(eventDTO.getSender().getEmail())
          .timestamp(messageRequest.getTimestamp())
          .channelId(messageRequest.getChannelId())
          .build();

      var userID = eventDTO.getSender().getId();
      sendMessage(userIds, userID, messageResponse, webSocketHandler);
      return;
    }

    sendErrorMessage(webSocketHandler, eventDTO, "Erro ao mandar a mensagem",
        "Usuário não encontrado");
  }

  @Override
  public boolean hasPermission(WebSocketEventDTO eventDTO, Object message) {
    if (message instanceof MessageDTO messageRequest) {
      if (messageRequest.getChannelId() == null) {
        return false;
      }
      return this.messageRepository.haveAccessForGroup(messageRequest.getChannelId(),
          eventDTO.getSender().getId());
    }
    return false;
  }

  @Override
  public WebSocketDestination getDestination() {
    return WebSocketDestination.CHAT;
  }

  @Override
  public Class<MessageDTO> getMessageType() {
    return MessageDTO.class;
  }

  private void sendMessage(
      List<Long> userIds,
      Long sendUserId,
      MessageDTO
          messageResponse,
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
                      session.sendMessage(
                          new TextMessage(objectMapper.writeValueAsBytes(messageResponse)));
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

  private void sendErrorMessage(
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

  private Optional<String> validateMessage(MessageDTO messageRequest) {
    if (messageRequest.getMessage() == null || messageRequest.getMessage().isEmpty()) {
      return Optional.of("Message cannot be empty");
    }
    if (messageRequest.getChannelId() == null) {
      return Optional.of("Group id cannot be empty");
    }

    if (messageRequest.getType() == null) {
      return Optional.of("Message type cannot be empty");
    }

    if (messageRequest.getTimestamp() == null) {
      return Optional.of("Timestamp cannot be empty");
    }

    return Optional.empty();
  }
}
