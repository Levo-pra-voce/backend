package com.levopravoce.backend.config;

import com.levopravoce.backend.entities.UserType;
import com.levopravoce.backend.entities.Vehicle;
import com.levopravoce.backend.repository.UserRepository;
import com.levopravoce.backend.services.authenticate.dto.UserDTO;
import com.levopravoce.backend.services.user.UserManagement;
import com.levopravoce.backend.services.user.UserManagementDeciderService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@ConditionalOnProperty(value="config.mockuser", havingValue="true")
@RequiredArgsConstructor
@Configuration("inicialData")
public class InicialDataConfiguration {
  private final UserRepository userRepository;
  private final UserManagementDeciderService userManagementDeciderService;

  @EventListener(ApplicationReadyEvent.class)
  public void init() {
    boolean existAnyUser = userRepository.count() > 0;

    if (!existAnyUser) {
      UserDTO userClientDTO = UserDTO.builder()
          .name("Douglas")
          .zipCode("88061412")
          .cpf("85173680024")
          .cnh("56563430390")
          .email("levopravocecliente@gmail.com")
          .password("Teste1234")
          .phone("48973737943")
          .build();
      UserManagement userManagementClient = userManagementDeciderService.getServiceByType(
          UserType.CLIENTE);
      userManagementClient.save(userClientDTO);

      // Criação do veículo associado ao entregador
      Vehicle vehicle = Vehicle.builder()
          .plate("ABC1234")
          .model("Fiorino")
          .color("Branco")
          .manufacturer("Fiat")
          .renavam("12345678910")
          .creationDate(LocalDateTime.now())
          .active(true)
          .height(1.9)
          .width(1.6)
          .maxWeight(800.0)
          .priceBase(100.0)
          .pricePerKm(10.0)
          .build();

      // Criação do usuário entregador
      UserDTO userDeliveryDTO = UserDTO.builder()
          .name("Carlos")
          .zipCode("88061412")
          .cpf("98765432100")
          .cnh("12345678901")
          .email("levopravoceentregador@gmail.com")
          .password("Teste1234")
          .phone("48987654321")
          .userType(UserType.ENTREGADOR)
          .status("ACTIVE")
          .city("Florianópolis")
          .state("SC")
          .country("Brasil")
          .street("Rua dos Entregadores")
          .neighborhood("Centro")
          .addressNumber("123")
          .complement("Apto 101")
          .token("randomGeneratedTokenForAuth")
          .profilePicture(new byte[]{}) // assume um array de bytes vazio para a foto de perfil
          .vehicle(vehicle)
          .build();

      // Obtém o serviço de gerenciamento de usuários para o tipo ENTREGADOR
      UserManagement userManagementDelivery = userManagementDeciderService.getServiceByType(
          UserType.ENTREGADOR);

      // Salva o usuário entregador
      userManagementDelivery.save(userDeliveryDTO);
    }
  }
}
