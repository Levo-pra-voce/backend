package com.levopravoce.backend.order;

import com.levopravoce.backend.repository.UserRepository;
import com.levopravoce.backend.services.order.dto.OrderDTO;
import com.levopravoce.backend.services.order.utils.OrderUtils;
import com.levopravoce.backend.services.user.UserManagementDeciderService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderUtilsTest {
    private final OrderUtils orderUtils;
    private final UserManagementDeciderService userManagementDeciderService;
    private final UserRepository userRepository;

    @Test
    @DisplayName("A largura não pode ser negativa")
    public void givenOrderDTOWithNegativeWidth_whenValidateOrderFields_thenThrowIllegalArgumentException() {
        Double width = -1.0;
        assertThrows(IllegalArgumentException.class, () -> orderUtils.validateWidth(width));
    }

    @Test
    @DisplayName("A largura não pode ser zero")
    public void givenOrderDTOWithZeroWidth_whenValidateOrderFields_thenThrowIllegalArgumentException() {
        Double width = 0.0;
        assertThrows(IllegalArgumentException.class, () -> orderUtils.validateWidth(width));
    }

    @Test
    @DisplayName("A largura não pode ser nula")
    public void givenOrderDTOWithNullWidth_whenValidateOrderFields_thenThrowIllegalArgumentException() {
        Double width = null;
        assertThrows(IllegalArgumentException.class, () -> orderUtils.validateWidth(width));
    }

    @Test
    @DisplayName("A altura não pode ser negativa")
    public void givenOrderDTOWithNegativeHeight_whenValidateOrderFields_thenThrowIllegalArgumentException() {
        Double height = -1.0;
        assertThrows(IllegalArgumentException.class, () -> orderUtils.validateHeight(height));
    }

    @Test
    @DisplayName("A altura não pode ser zero")
    public void givenOrderDTOWithZeroHeight_whenValidateOrderFields_thenThrowIllegalArgumentException() {
        Double height = 0.0;
        assertThrows(IllegalArgumentException.class, () -> orderUtils.validateHeight(height));
    }

    @Test
    @DisplayName("A altura não pode ser nula")
    public void givenOrderDTOWithNullHeight_whenValidateOrderFields_thenThrowIllegalArgumentException() {
        Double height = null;
        assertThrows(IllegalArgumentException.class, () -> orderUtils.validateHeight(height));
    }

    @Test
    @DisplayName("O peso máximo não pode ser negativo")
    public void givenOrderDTOWithNegativeMaxWeight_whenValidateOrderFields_thenThrowIllegalArgumentException() {
        Double maxWeight = -1.0;
        assertThrows(IllegalArgumentException.class, () -> orderUtils.validadeMaxWeight(maxWeight));
    }

    @Test
    @DisplayName("O peso máximo não pode ser zero")
    public void givenOrderDTOWithZeroMaxWeight_whenValidateOrderFields_thenThrowIllegalArgumentException() {
        Double maxWeight = 0.0;
        assertThrows(IllegalArgumentException.class, () -> orderUtils.validadeMaxWeight(maxWeight));
    }

    @Test
    @DisplayName("O peso máximo não pode ser nulo")
    public void givenOrderDTOWithNullMaxWeight_whenValidateOrderFields_thenThrowIllegalArgumentException() {
        Double maxWeight = null;
        assertThrows(IllegalArgumentException.class, () -> orderUtils.validadeMaxWeight(maxWeight));
    }

    @Test
    @DisplayName("A data de entrega não pode ser anterior a data atual")
    public void givenOrderDTOWithPastDeliveryDate_whenValidateOrderFields_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> orderUtils.validateDeliveryDate(
            LocalDate.now().minusDays(1)));
    }

    @Test
    @DisplayName("A data de entrega não pode ser nula")
    public void givenOrderDTOWithNullDeliveryDate_whenValidateOrderFields_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> orderUtils.validateDeliveryDate(null));
    }

    @Test
    @DisplayName("A validação total da ordem não deve lançar exceção")
    public void givenOrderDTOWithValidFields_whenValidateOrderFields_thenDoNothing() {
        OrderDTO orderDTO = OrderDTO.builder()
                .height(1.0)
                .width(1.0)
                .maxWeight(1.0)
                .deliveryDate(LocalDate.now().plusDays(1))
                .haveSecurity(false)
                .destinationLatitude(1.0)
                .destinationLongitude(1.0)
                .originLatitude(1.0)
                .originLongitude(1.0)
                .build();
        assertDoesNotThrow(() -> orderUtils.validateNewOrder(orderDTO));
    }
}
