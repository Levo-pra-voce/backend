package com.levopravoce.backend.config;

import com.levopravoce.backend.security.JwtTokenUtil;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;


@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfiguration implements WebSocketConfigurer {

  private final JwtTokenUtil jwtService;
  private final UserDetailsService userService;
  private final WebSocketHandler webSocketHandler;
  private final ApplicationContext context;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(webSocketHandler, "/socket")
        .addInterceptors(new HttpSessionHandshakeInterceptor() {
          @Override
          public boolean beforeHandshake(
              ServerHttpRequest request,
              ServerHttpResponse response,
              org.springframework.web.socket.WebSocketHandler wsHandler,
              Map<String, Object> attributes) throws java.lang.Exception {
            return super.beforeHandshake(request, response, wsHandler, attributes)
                && request.getPrincipal() != null && ((UsernamePasswordAuthenticationToken)
                request.getPrincipal()).isAuthenticated();
          }

          @Override
          public void afterHandshake(
              ServerHttpRequest request,
              ServerHttpResponse response,
              org.springframework.web.socket.WebSocketHandler wsHandler,
              Exception ex) {
            super.afterHandshake(request, response, wsHandler, ex);
          }

        });
  }
}
