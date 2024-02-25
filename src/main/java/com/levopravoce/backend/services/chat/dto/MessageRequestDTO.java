package com.levopravoce.backend.services.chat.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties
@Data
public class MessageRequestDTO {

    private Long groupId;
    private MessageType type;
    private String message;
    private String name;
}
