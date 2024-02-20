package com.levopravoce.backend.config;

import com.levopravoce.backend.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.messaging.context.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;


@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer implements WebSocketMessageBrokerConfigurer  {
    private final JwtTokenUtil jwtService;
    private final UserDetailsService userService;

    private final ApplicationContext context;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket").setAllowedOrigins("*");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }

    @Override
    protected void configureInbound(final MessageSecurityMetadataSourceRegistry messages) {
        messages.anyMessage().authenticated();
    }
//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(new ChannelInterceptor() {
//            @Override
//            public Message<?> preSend(Message<?> message, MessageChannel channel) {
//                StompHeaderAccessor accessor =
//                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//
//                if (accessor == null) {
//                    throw new IllegalArgumentException("Invalid token");
//                }
//
//                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//                    String authorization = Objects.requireNonNull(accessor.getNativeHeader("X-Authorization")).get(0);
////                    logger.debug("X-Authorization: {}", authorization);
//                    final String userEmail = jwtService.getUsernameFromToken(authorization);
//                    if (StringUtils.isEmpty(userEmail)) {
//                        throw new IllegalArgumentException("Invalid token");
//                    }
//                    final UserDetails userDetails = userService.loadUserByUsername(userEmail);
//
//                    if (!jwtService.isTokenValid(authorization, userDetails)) {
//                        throw new IllegalArgumentException("Invalid token");
//                    }
//
//                    Authentication authentication = new UsernamePasswordAuthenticationToken(
//                            userDetails, null, userDetails.getAuthorities());
//                    accessor.setUser(authentication);
//                }
//                return message;
//            }
//        });
//    }
}
