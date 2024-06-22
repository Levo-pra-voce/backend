package com.levopravoce.backend.socket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MessageSocketDTO {
  private MessageType type;
  private String message;
  private String sender;
  private String receiver;
  private Long timestamp;
  private String channelId;
}
