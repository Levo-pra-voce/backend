package com.levopravoce.backend.services.chat.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChatUserDTO {

  @Column(name = "nome")
  private String name;
  @Column(name = "channelId")
  private Long channelId;
}
