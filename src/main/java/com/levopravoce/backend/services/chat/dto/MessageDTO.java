package com.levopravoce.backend.services.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {

  private MessageType type;
  private Long channelId;
  private Long timestamp;
  private String message;
  private String sender;
}
