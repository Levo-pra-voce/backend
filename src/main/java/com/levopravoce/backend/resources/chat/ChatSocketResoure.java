package com.levopravoce.backend.resources.chat;

import com.levopravoce.backend.services.chat.dto.MessageRequestDTO;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatSocketResoure {

    private final SimpMessagingTemplate messagingTemplate;
    
    @MessageMapping("/chat")
    @SendToUser("/topic/chat")
    public MessageRequestDTO chat(MessageRequestDTO message) {
        return message;
    }

    @MessageMapping("/chat/{username}")
    public void chat(MessageRequestDTO message, @DestinationVariable String username) {
        this.messagingTemplate.convertAndSendToUser(username, "/topic/chat", message);
    }
}
