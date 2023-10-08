package com.school.informationsecurity.services.chat.dto;

import lombok.Data;

@Data
public class MessageRequestDTO {
    private String sender;
    private String receiver;
    private String message;
    private String name;
}
