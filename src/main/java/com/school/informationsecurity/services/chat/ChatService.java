package com.school.informationsecurity.services.chat;

import com.school.informationsecurity.entities.Message;
import com.school.informationsecurity.repository.MessageRepository;
import com.school.informationsecurity.repository.UserRepository;
import com.school.informationsecurity.services.chat.dto.ChatResponseDTO;
import com.school.informationsecurity.services.chat.dto.MessageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    public ChatResponseDTO getAllMessagesBySenderAndReceiver(String sender, String receiver) {
        List<MessageResponseDTO> messageResponseDTOS =
                this.messageRepository.getAllMessagesBySender(sender, receiver)
                        .stream()
                        .map(this::convertToMessageResponseDTO)
                        .toList();

        ChatResponseDTO chatResponseDTO = new ChatResponseDTO();

        chatResponseDTO.setMessages(messageResponseDTOS);
        chatResponseDTO.setSender(sender);
        chatResponseDTO.setReceiver(receiver);

        Optional<String> userName = this.userRepository.getNameByEmail(sender);
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
