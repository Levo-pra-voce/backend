package com.levopravoce.backend.user.mock;

import com.levopravoce.backend.entities.Address;
import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.entities.UserType;
import java.time.LocalDateTime;
import java.util.List;

public final class UserMock {

  public static final String CLIENT_EMAIL = "client@gmail.com";
  public static final String DELIVERY_EMAIL = "delivery@gmail.com";
  private static final Address ADDRESS =
      Address.builder()
          .id(1L)
          .city("Test")
          .state("Test")
          .active(true)
          .zipCode("123456")
          .complement("Test")
          .number("123")
          .street("Test")
          .creationDate(LocalDateTime.now())
          .build();

  public static final User DELIVERY_USER =
      User.builder()
          .id(2L)
          .userType(UserType.ENTREGADOR)
          .email(DELIVERY_EMAIL)
          .password("123456")
          .name("Test")
          .cpf("12345678912")
          .contact("123456789")
          .addresses(List.of(ADDRESS))
          .build();
  public static final User CLIENT_USER =
      User.builder()
          .id(1L)
          .userType(UserType.CLIENTE)
          .email(CLIENT_EMAIL)
          .password("123456")
          .name("Test")
          .cpf("12345678910")
          .contact("123456789")
          .addresses(List.of(ADDRESS))
          .build();
}
