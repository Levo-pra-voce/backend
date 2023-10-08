package com.school.informationsecurity.resources.chat;

import com.school.informationsecurity.common.SecurityUtils;
import com.school.informationsecurity.services.chat.ChatService;
import com.school.informationsecurity.services.chat.dto.ChatResponseDTO;
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

    @GetMapping("/messages/{username}")
    public ChatResponseDTO getAllMessagesBySender(@PathVariable String username) {
        return this.chatService.getAllMessagesBySenderAndReceiver(username, SecurityUtils.getCurrentUsername());
    }
}
