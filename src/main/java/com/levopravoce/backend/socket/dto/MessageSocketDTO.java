package com.levopravoce.backend.socket.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.levopravoce.backend.socket.WebSocketDestination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
public class MessageSocketDTO {
  private MessageType type;
  private String message;
  @JsonProperty("sender")
  private String sender;
  @JsonProperty("receiver")
  private String receiver;
  private Long timestamp;
  private String channelId;
  private WebSocketDestination destination;

  @JsonCreator
  public MessageSocketDTO(MessageType type, String message, String sender, String receiver,
      Long timestamp, String channelId, WebSocketDestination destination) {
    this.type = type;
    this.message = message;
    this.sender = sender;
    this.receiver = receiver;
    this.timestamp = timestamp;
    this.channelId = channelId;
    this.destination = destination;
  }
}
