package com.levopravoce.backend.user;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.levopravoce.backend.common.UserUtils;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserUtilsTest {
    private final UserUtils userUtils;

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
}
