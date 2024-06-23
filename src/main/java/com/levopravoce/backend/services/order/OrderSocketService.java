package com.levopravoce.backend.services.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.levopravoce.backend.entities.Order;
import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.repository.OrderRepository;
import com.levopravoce.backend.services.order.dto.OrderTrackingDTO;
import com.levopravoce.backend.services.order.dto.OrderTrackingStatus;
import com.levopravoce.backend.socket.WebSocketDestination;
import com.levopravoce.backend.socket.WebSocketHandler;
import com.levopravoce.backend.socket.WebSocketMessageService;
import com.levopravoce.backend.socket.dto.MessageSocketDTO;
import com.levopravoce.backend.socket.dto.MessageType;
import com.levopravoce.backend.socket.dto.WebSocketEventDTO;
import com.mapbox.geojson.Point;
import com.mapbox.turf.TurfMeasurement;
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
    List<WebSocketSession> clientWebSockets = webSocketHandler.userToActiveSessions.get(order.getClient().getId());
    if (clientWebSockets == null) {
      return;
    }
    try {
      Point destinyPoint = Point.fromLngLat(order.getDestinationLongitude(), order.getDestinationLatitude());
      Point trackingPoint = Point.fromLngLat(orderTrackingDTO.getLongitude(), orderTrackingDTO.getLatitude());
      double distance = TurfMeasurement.distance(destinyPoint, trackingPoint, "metres");
      if (distance < 100) {
        orderTrackingDTO.setStatus(OrderTrackingStatus.ITS_CLOSE);
        List<WebSocketSession> delivererWebSockets = webSocketHandler.userToActiveSessions.get(order.getDeliveryman().getId());
        if (delivererWebSockets != null) {
          String orderTrackingJson = objectMapper.writeValueAsString(orderTrackingDTO);
          MessageSocketDTO messageSocketDTO = MessageSocketDTO.builder()
              .type(MessageType.TEXT)
              .message(orderTrackingJson)
              .timestamp(System.currentTimeMillis())
              .sender(currentSessionUser.getUsername())
              .receiver(order.getDeliveryman().getUsername())
              .destination(WebSocketDestination.ORDER_MAP)
              .build();
          String messageJson = objectMapper.writeValueAsString(messageSocketDTO);
          for (WebSocketSession webSocketSession: delivererWebSockets) {
            TextMessage textMessage = new TextMessage(messageJson);
            webSocketSession.sendMessage(textMessage);
          }
        }
      }
      String orderTrackingJson = objectMapper.writeValueAsString(orderTrackingDTO);
      MessageSocketDTO messageSocketDTO = MessageSocketDTO.builder()
          .type(MessageType.TEXT)
          .message(orderTrackingJson)
          .timestamp(System.currentTimeMillis())
          .sender(currentSessionUser.getUsername())
          .receiver(order.getClient().getUsername())
          .destination(WebSocketDestination.ORDER_MAP)
          .build();
      String messageJson = objectMapper.writeValueAsString(messageSocketDTO);
      for (WebSocketSession webSocketSession: clientWebSockets) {
        TextMessage textMessage = new TextMessage(messageJson);
        webSocketSession.sendMessage(textMessage);
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
