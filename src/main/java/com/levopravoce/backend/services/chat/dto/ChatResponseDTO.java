package com.levopravoce.backend.services.chat.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatResponseDTO {

    private List<MessageDTO> messages;
    private String sender;
    private String receiver;
    private String name;
}
