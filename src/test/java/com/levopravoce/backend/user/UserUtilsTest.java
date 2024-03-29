package com.levopravoce.backend.user;

import com.levopravoce.backend.common.UserUtils;
import com.levopravoce.backend.user.mock.UserMock;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserUtilsTest {

  private final UserUtils userUtils = new UserUtils(null);

    @Test
    @DisplayName("verifica se o nome está vazio ou nulo")
    public void givenUserDTOWithEmptyFirstName_whenValidateUserFields_thenThrowIllegalArgumentException() {
      UserMock.CLIENT_USER.setName(StringUtils.EMPTY);
        assertThrows(IllegalArgumentException.class, () -> userUtils.validateUserFields(UserMock.CLIENT_USER.toDTO()));
    }

    @Test
    @DisplayName("verifica se o email está vazio ou nulo")
    public void givenUserDTOWithEmptyEmail_whenValidateUserFields_thenThrowIllegalArgumentException() {
        UserMock.CLIENT_USER.setEmail(StringUtils.EMPTY);
        assertThrows(IllegalArgumentException.class, () -> userUtils.validateUserFields(UserMock.CLIENT_USER.toDTO()));
    }

    @Test
    @DisplayName("verifica se o email está no formato correto")
    public void givenUserDTOWithInvalidEmail_whenValidateUserFields_thenThrowIllegalArgumentException() {
        UserMock.CLIENT_USER.setEmail("email");
        assertThrows(IllegalArgumentException.class, () -> userUtils.validateUserFields(UserMock.CLIENT_USER.toDTO()));
    }

    @Test
    @DisplayName("verifica se a senha está vazia ou nula")
    public void givenUserDTOWithEmptyPassword_whenValidateUserFields_thenThrowIllegalArgumentException() {
        UserMock.CLIENT_USER.setPassword(StringUtils.EMPTY);
        assertThrows(IllegalArgumentException.class, () -> userUtils.validateUserFields(UserMock.CLIENT_USER.toDTO()));
    }

    @Test
    @DisplayName("verifica se a senha tem menos de 6 caracteres")
    public void givenUserDTOWithPasswordLessThanSixCharacters_whenValidateUserFields_thenThrowIllegalArgumentException() {
        UserMock.CLIENT_USER.setPassword("12345");
        assertThrows(IllegalArgumentException.class, () -> userUtils.validateUserFields(UserMock.CLIENT_USER.toDTO()));
    }

    @Test
    @DisplayName("verifica se o telefone está vazio ou nulo")
    public void givenUserDTOWithEmptyPhone_whenValidateUserFields_thenThrowIllegalArgumentException() {
        UserMock.CLIENT_USER.setContact(StringUtils.EMPTY);
        assertThrows(IllegalArgumentException.class, () -> userUtils.validateUserFields(UserMock.CLIENT_USER.toDTO()));
    }

    @Test
    @DisplayName("verifica se o telefone contém espaços")
    public void givenUserDTOWithPhoneWithSpaces_whenValidateUserFields_thenThrowIllegalArgumentException() {
        UserMock.CLIENT_USER.setContact("123 456 789");
        assertThrows(IllegalArgumentException.class, () -> userUtils.validateUserFields(UserMock.CLIENT_USER.toDTO()));
    }

    @Test
    @DisplayName("verifica se o cpf está vazio ou nulo")
    public void givenUserDTOWithEmptyCpf_whenValidateUserFields_thenThrowIllegalArgumentException() {
        UserMock.CLIENT_USER.setCpf(StringUtils.EMPTY);
        assertThrows(IllegalArgumentException.class, () -> userUtils.validateUserFields(UserMock.CLIENT_USER.toDTO()));
    }

    @Test
    @DisplayName("verifica se o cpf tem 11 dígitos")
    public void givenUserDTOWithCpfWithLessThanElevenDigits_whenValidateUserFields_thenThrowIllegalArgumentException() {
        UserMock.CLIENT_USER.setCpf("1234567891");
        assertThrows(IllegalArgumentException.class, () -> userUtils.validateUserFields(UserMock.CLIENT_USER.toDTO()));
    }

    @Test
    @DisplayName("verifica se o cpf tem apenas números")
    public void givenUserDTOWithCpfWithLetters_whenValidateUserFields_thenThrowIllegalArgumentException() {
        UserMock.CLIENT_USER.setCpf("1234567891a");
        assertThrows(IllegalArgumentException.class, () -> userUtils.validateUserFields(UserMock.CLIENT_USER.toDTO()));
    }
}
