package com.school.informationsecurity.interceptor;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

public class JwtHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        HttpHeaders headers = request.getHeaders();
        List<String> authorizationHeaders = headers.get("Authorization");

        // Obtenha o token JWT do cabeçalho de autorização

        // Se a autenticação falhar, lance uma exceção ou retorne null
        throw new HandshakeFailureException("Autenticação JWT falhou.");
    }

    // Implemente métodos para extrair e validar o token JWT
}

