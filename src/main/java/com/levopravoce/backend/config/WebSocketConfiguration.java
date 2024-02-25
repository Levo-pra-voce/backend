package com.levopravoce.backend.config;

import com.levopravoce.backend.interceptor.HttpSessionHandshakeSecurityInterceptor;
import com.levopravoce.backend.security.JwtTokenUtil;
import com.levopravoce.backend.socket.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfiguration implements WebSocketConfigurer {

  private final JwtTokenUtil jwtService;
  private final UserDetailsService userService;
  private final WebSocketHandler webSocketHandler;
  private final HttpSessionHandshakeSecurityInterceptor securityInterceptor;
  private final ApplicationContext context;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(webSocketHandler, "/socket")
        .addInterceptors(securityInterceptor);
  }
}
