package com.levopravoce.backend.services.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.levopravoce.backend.entities.Order;
import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.repository.OrderRepository;
import com.levopravoce.backend.services.order.dto.OrderTrackingDTO;
import com.levopravoce.backend.socket.WebSocketDestination;
import com.levopravoce.backend.socket.WebSocketHandler;
import com.levopravoce.backend.socket.WebSocketMessageService;
import com.levopravoce.backend.socket.dto.MessageSocketDTO;
import com.levopravoce.backend.socket.dto.MessageType;
import com.levopravoce.backend.socket.dto.WebSocketEventDTO;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
@RequiredArgsConstructor
public class OrderSocketService implements WebSocketMessageService<OrderTrackingDTO> {
  private final OrderRepository orderRepository;
  private final ObjectMapper objectMapper;

  @Override
  public void handleMessage(WebSocketEventDTO eventDTO, WebSocketHandler webSocketHandler,
      User currentSessionUser, Object message) {
    OrderTrackingDTO orderTrackingDTO = (OrderTrackingDTO) message;
    Optional<Order> orderOptional = orderRepository.findById(orderTrackingDTO.getOrderId());
    if (orderOptional.isEmpty()) {
      return;
    }
    Order order = orderOptional.get();
    List<WebSocketSession> websockets = webSocketHandler.userToActiveSessions.get(
        order.getDeliveryman().getId());
    if (websockets == null) {
      return;
    }
    try {
      String orderTrackingJson = objectMapper.writeValueAsString(orderTrackingDTO);
      MessageSocketDTO messageSocketDTO = MessageSocketDTO.builder()
          .type(MessageType.TEXT)
          .message(orderTrackingJson)
          .timestamp(System.currentTimeMillis())
          .sender(currentSessionUser.getUsername())
          .receiver(order.getDeliveryman().getUsername())
          .build();
      String messageJson = objectMapper.writeValueAsString(messageSocketDTO);
      for (WebSocketSession webSocketSession: websockets) {
        webSocketHandler.handleMessage(webSocketSession, new TextMessage(messageJson));
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean hasPermission(WebSocketEventDTO eventDTO, Object message) {
    return true;
  }

  @Override
  public WebSocketDestination getDestination() {
    return WebSocketDestination.ORDER_MAP;
  }

  @Override
  public Class<OrderTrackingDTO> getMessageType() {
    return OrderTrackingDTO.class;
  }
}
