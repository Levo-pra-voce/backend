package com.levopravoce.backend.services.chat;

import com.levopravoce.backend.common.SecurityUtils;
import com.levopravoce.backend.entities.Message;
import com.levopravoce.backend.repository.MessageRepository;
import com.levopravoce.backend.services.chat.dto.MessageDTO;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatService {

  private final MessageRepository messageRepository;

  public List<MessageDTO> getAllByChannelId(Long channelId) {
    haveAccessForGroup(channelId, SecurityUtils.getCurrentUserId().orElseThrow());

    return messageRepository
        .getAllByChannelId(channelId)
        .stream()
        .map(Message::toMessageDTO).toList();
  }

  public List<MessageDTO> getAllByChannelIdAndTimestamp(Long channelId, Long timestamp) {
    haveAccessForGroup(channelId, SecurityUtils.getCurrentUserId().orElseThrow());

    return messageRepository
        .getAllByChannelIdAndTimestamp(channelId, LocalDateTime.ofInstant(
            Instant.ofEpochSecond(timestamp),
            ZoneId.of("UTC")
        ))
        .stream()
        .map(Message::toMessageDTO).toList();
  }


  private void haveAccessForGroup(Long groupId, Long userId) {
    if (!messageRepository.haveAccessForGroup(groupId, userId)) {
      throw new RuntimeException("User does not have access to this channel");
    }
  }
}
