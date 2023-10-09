package com.levo_pra_voce.backend.resources.chat;

import com.levo_pra_voce.backend.common.SecurityUtils;
import com.levo_pra_voce.backend.services.chat.ChatService;
import com.levo_pra_voce.backend.services.chat.dto.ChatResponseDTO;
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

    @GetMapping("/messages/{groupId}")
    public ChatResponseDTO getAllMessagesBySender(@PathVariable Long groupId) {
        return this.chatService.getAllMessagesBySenderAndReceiver(groupId);
    }
}
