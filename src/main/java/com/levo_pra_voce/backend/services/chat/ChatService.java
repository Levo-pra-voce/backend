package com.levo_pra_voce.backend.services.chat;

import com.levo_pra_voce.backend.common.SecurityUtils;
import com.levo_pra_voce.backend.services.chat.dto.ChatResponseDTO;
import com.levo_pra_voce.backend.services.chat.dto.MessageResponseDTO;
import com.levo_pra_voce.backend.entities.Message;
import com.levo_pra_voce.backend.repository.MessageRepository;
import com.levo_pra_voce.backend.repository.UserRepository;
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
        messageResponseDTO.setName(message.getSender().getFirstName());
        return messageResponseDTO;
    }
}
