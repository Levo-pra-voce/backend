package com.levo_pra_voce.backend.services.user;

import com.levo_pra_voce.backend.entities.UserType;
import com.levo_pra_voce.backend.services.client.ClientUserService;
import com.levo_pra_voce.backend.services.delivery.DeliveryUserService;
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
      default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User type not found");
    };
  }
}
