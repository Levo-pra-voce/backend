package com.levopravoce.backend.resources.chat;

import com.levopravoce.backend.services.chat.ChatService;
import com.levopravoce.backend.services.chat.dto.ChatResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRestResource {

    private final ChatService chatService;

//    @GetMapping("/messages/{groupId}")
//    public ChatResponseDTO getAllMessagesBySender(@PathVariable Long groupId) {
//        return this.chatService.getAllMessagesBySenderAndReceiver(groupId);
//    }
}
