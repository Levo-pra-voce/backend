package com.levopravoce.backend.services.user;

import com.levopravoce.backend.entities.UserType;
import com.levopravoce.backend.services.client.ClientUserService;
import com.levopravoce.backend.services.delivery.DeliveryUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserManagementDeciderService {
  private final DeliveryUserService deliveryUserService;
  private final ClientUserService clientUserService;

  public UserManagement getServiceByType(UserType userType) {
    return switch (userType) {
      case ENTREGADOR -> deliveryUserService;
      case CLIENTE -> clientUserService;
    };
  }
}
