package com.levopravoce.backend.services.order.utils;

import com.levopravoce.backend.entities.Request.RequestStatus;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class RequestUtils {
  public void validatePendingStatus(RequestStatus status) {
    if (Objects.equals(status, RequestStatus.RECUSADO)) {
      throw new RuntimeException("Pedido já foi recusado.");
    }
    if (Objects.equals(status, RequestStatus.ACEITO)) {
      throw new RuntimeException("Pedido já foi aceito.");
    }
  }
}
