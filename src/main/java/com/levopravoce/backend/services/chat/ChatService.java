package com.levopravoce.backend.services.chat;

import com.levopravoce.backend.common.SecurityUtils;
import com.levopravoce.backend.entities.Message;
import com.levopravoce.backend.repository.MessageRepository;
import com.levopravoce.backend.services.chat.dto.MessageResponseDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatService {

  private final MessageRepository messageRepository;

  public List<MessageResponseDTO> getAllByChannelId(Long channelId) {
    if (!messageRepository.haveAccessForGroup(channelId,
        SecurityUtils.getCurrentUserId().orElseThrow())) {
      throw new RuntimeException("User does not have access to this channel");
    }

    return messageRepository
        .getAllByChannelId(channelId)
        .stream()
        .map(Message::toMessageResponseDTO).toList();
  }
}
