package com.levopravoce.backend.services.chat;

import com.levopravoce.backend.common.SecurityUtils;
import com.levopravoce.backend.services.chat.dto.ChatResponseDTO;
import com.levopravoce.backend.services.chat.dto.MessageResponseDTO;
import com.levopravoce.backend.entities.Message;
import com.levopravoce.backend.repository.MessageRepository;
import com.levopravoce.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    public ChatResponseDTO getAllMessagesBySenderAndReceiver(Long groupId) {
        List<MessageResponseDTO> messageResponseDTOS =
                this.messageRepository.getAllMessagesBySender(groupId)
                        .stream()
                        .map(this::convertToMessageResponseDTO)
                        .toList();

        ChatResponseDTO chatResponseDTO = new ChatResponseDTO();

        chatResponseDTO.setMessages(messageResponseDTOS);

        Optional<String> userName = this.userRepository.getNameByEmail(SecurityUtils.getCurrentUsername());
        userName.ifPresent(chatResponseDTO::setName);

        return chatResponseDTO;
    }

    private MessageResponseDTO convertToMessageResponseDTO(Message message) {
        MessageResponseDTO messageResponseDTO = new MessageResponseDTO();
        messageResponseDTO.setMessage(new String(message.getMessage()));
        messageResponseDTO.setSender(message.getSender().getEmail());
        messageResponseDTO.setName(message.getSender().getName());
        return messageResponseDTO;
    }
}
