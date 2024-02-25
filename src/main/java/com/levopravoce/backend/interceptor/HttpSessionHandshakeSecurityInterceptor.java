package com.levopravoce.backend.interceptor;

import java.util.Map;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Component
public class HttpSessionHandshakeSecurityInterceptor extends HttpSessionHandshakeInterceptor {

  @Override
  public boolean beforeHandshake(
      ServerHttpRequest request,
      ServerHttpResponse response,
      WebSocketHandler wsHandler,
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
}
