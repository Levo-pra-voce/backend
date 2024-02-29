package com.levopravoce.backend.resources.chat;

import com.levopravoce.backend.services.chat.ChatService;
import com.levopravoce.backend.services.chat.dto.MessageDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatResource {

    private final ChatService chatService;

    @GetMapping("/messages/{channelId}")
    public List<MessageDTO> getAllMessagesBySender(@PathVariable Long channelId) {
        return this.chatService.getAllByChannelId(channelId);
    }

    @GetMapping("/messages/{channelId}/{timestamp}")
    public List<MessageDTO> getAllMessagesBySender(@PathVariable Long channelId,
        @PathVariable Long timestamp) {
        return this.chatService.getAllByChannelIdAndTimestamp(channelId, timestamp);
    }
}
