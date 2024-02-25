package com.levopravoce.backend.socket.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.levopravoce.backend.entities.User;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketEventDTO {

  private Map<String, String> headers;
  private String body;
  private User sender;

  public Boolean isValid() {
    return headers != null && headers.containsKey("destination") && body != null;
  }

  @Getter
  @RequiredArgsConstructor
  public enum HEADERS {
    DESTINATION("destination");
    private final String value;
  }
}
