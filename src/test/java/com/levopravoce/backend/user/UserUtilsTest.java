package com.levopravoce.backend.user;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.levopravoce.backend.common.UserUtils;
import com.levopravoce.backend.config.InicialDataConfiguration;
import com.levopravoce.backend.entities.UserType;
import com.levopravoce.backend.repository.UserRepository;
import com.levopravoce.backend.services.user.UserManagement;
import com.levopravoce.backend.services.user.UserManagementDeciderService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserUtilsTest {
  private final UserUtils userUtils;
  private final UserManagementDeciderService userManagementDeciderService;
  private final UserRepository userRepository;

  @Test
  @DisplayName("verifica se o nome está vazio ou nulo")
  public void givenUserDTOWithEmptyFirstName_whenValidateUserFields_thenThrowIllegalArgumentException() {
    String name = "";
    assertThrows(IllegalArgumentException.class, () -> userUtils.validateName(name));
  }

  @Test
  @DisplayName("verifica se o email está vazio ou nulo")
  public void givenUserDTOWithEmptyEmail_whenValidateUserFields_thenThrowIllegalArgumentException() {
    String email = "";
    assertThrows(IllegalArgumentException.class, () -> userUtils.validateEmail(email));
  }

  @Test
  @DisplayName("verifica se o email está no formato correto")
  public void givenUserDTOWithInvalidEmail_whenValidateUserFields_thenThrowIllegalArgumentException() {
    String email = "email";
    assertThrows(IllegalArgumentException.class, () -> userUtils.validateEmail(email));
  }

  @Test
  @DisplayName("verifica se a senha está vazia ou nula")
  public void givenUserDTOWithEmptyPassword_whenValidateUserFields_thenThrowIllegalArgumentException() {
    String password = "";
    assertThrows(IllegalArgumentException.class, () -> userUtils.validatePassword(password));
  }

  @Test
  @DisplayName("verifica se a senha tem menos de 6 caracteres")
  public void givenUserDTOWithPasswordLessThanSixCharacters_whenValidateUserFields_thenThrowIllegalArgumentException() {
    String password = "12345";
    assertThrows(IllegalArgumentException.class, () -> userUtils.validatePassword(password));
  }

  @Test
  @DisplayName("verifica se a senha deve conter ao menos 8 caracteres, uma letra maiúscula, uma letra minúscula e um número")
  public void givenUserDTOWithPasswordLessThanValid_whenValidateUserFields_thenThrowIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> userUtils.validatePassword("12345678"));
    assertThrows(IllegalArgumentException.class, () -> userUtils.validatePassword("abcdefgh"));
    assertThrows(IllegalArgumentException.class, () -> userUtils.validatePassword("ABCDEFGH"));
    assertThrows(IllegalArgumentException.class,
        () -> userUtils.validatePassword("12345678abcdefgh"));
    assertThrows(IllegalArgumentException.class, () -> userUtils.validatePassword("Df23"));
    assertDoesNotThrow(() -> userUtils.validatePassword("Df2345678"));
  }

  @Test
  @DisplayName("verifica se o telefone está vazio ou nulo")
  public void givenUserDTOWithEmptyPhone_whenValidateUserFields_thenThrowIllegalArgumentException() {
    String contact = "";
    assertThrows(IllegalArgumentException.class, () -> userUtils.validatePhone(contact));
  }

  @Test
  @DisplayName("verifica se o telefone contém espaços")
  public void givenUserDTOWithPhoneWithSpaces_whenValidateUserFields_thenThrowIllegalArgumentException() {
    String contact = "123 456 789";
    assertThrows(IllegalArgumentException.class, () -> userUtils.validatePhone(contact));
  }

  @Test
  @DisplayName("verifica se o cpf está vazio ou nulo")
  public void givenUserDTOWithEmptyCpf_whenValidateUserFields_thenThrowIllegalArgumentException() {
    String cpf = "";
    assertThrows(IllegalArgumentException.class, () -> userUtils.validateCpf(cpf));
  }

  @Test
  @DisplayName("verifica se o cpf tem 11 dígitos")
  public void givenUserDTOWithCpfWithLessThanElevenDigits_whenValidateUserFields_thenThrowIllegalArgumentException() {
    String cpf = "123456789";
    assertThrows(IllegalArgumentException.class, () -> userUtils.validateCpf(cpf));
  }

  @Test
  @DisplayName("verifica se o cpf tem apenas números")
  public void givenUserDTOWithCpfWithLetters_whenValidateUserFields_thenThrowIllegalArgumentException() {
    String cpf = "1234567891a";
    assertThrows(IllegalArgumentException.class, () -> userUtils.validateCpf(cpf));
  }

  @Test
  @DisplayName("Verifica se os usuários estão sendo persistidos corretamente")
  public void givenUserDTO_whenSaveUser_thenPersistUser() {
    // Obtém o serviço de gerenciamento de usuários para o tipo CLIENTE
    UserManagement userManagementClient = userManagementDeciderService.getServiceByType(
        UserType.CLIENTE);
    // Salva o usuário cliente
    userManagementClient.save(InicialDataConfiguration.userClientDTO);

    // Obtém o serviço de gerenciamento de usuários para o tipo ENTREGADOR
    UserManagement userManagementDelivery = userManagementDeciderService.getServiceByType(
        UserType.ENTREGADOR);
    // Salva o usuário entregador
    userManagementDelivery.save(InicialDataConfiguration.userDeliveryDTO);
    userRepository.deleteAll();
  }
}
