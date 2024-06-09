package com.levopravoce.backend.order;

import com.levopravoce.backend.services.order.dto.OrderDTO;
import com.levopravoce.backend.services.order.utils.OrderUtils;
import lombok.RequiredArgsConstructor;
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

    @Test
    public void givenOrderDTOWithNegativeWidth_whenValidateOrderFields_thenThrowIllegalArgumentException() {
        Double width = -1.0;
        assertThrows(IllegalArgumentException.class, () -> orderUtils.validateWidth(width));
    }

    @Test
    public void givenOrderDTOWithZeroWidth_whenValidateOrderFields_thenThrowIllegalArgumentException() {
        Double width = 0.0;
        assertThrows(IllegalArgumentException.class, () -> orderUtils.validateWidth(width));
    }

    @Test
    public void givenOrderDTOWithNullWidth_whenValidateOrderFields_thenThrowIllegalArgumentException() {
        Double width = null;
        assertThrows(IllegalArgumentException.class, () -> orderUtils.validateWidth(width));
    }

    @Test
    public void givenOrderDTOWithNegativeHeight_whenValidateOrderFields_thenThrowIllegalArgumentException() {
        Double height = -1.0;
        assertThrows(IllegalArgumentException.class, () -> orderUtils.validateHeight(height));
    }

    @Test
    public void givenOrderDTOWithZeroHeight_whenValidateOrderFields_thenThrowIllegalArgumentException() {
        Double height = 0.0;
        assertThrows(IllegalArgumentException.class, () -> orderUtils.validateHeight(height));
    }

    @Test
    public void givenOrderDTOWithNullHeight_whenValidateOrderFields_thenThrowIllegalArgumentException() {
        Double height = null;
        assertThrows(IllegalArgumentException.class, () -> orderUtils.validateHeight(height));
    }

    @Test
    public void givenOrderDTOWithNegativeMaxWeight_whenValidateOrderFields_thenThrowIllegalArgumentException() {
        Double maxWeight = -1.0;
        assertThrows(IllegalArgumentException.class, () -> orderUtils.validadeMaxWeight(maxWeight));
    }

    @Test
    public void givenOrderDTOWithZeroMaxWeight_whenValidateOrderFields_thenThrowIllegalArgumentException() {
        Double maxWeight = 0.0;
        assertThrows(IllegalArgumentException.class, () -> orderUtils.validadeMaxWeight(maxWeight));
    }

    @Test
    public void givenOrderDTOWithNullMaxWeight_whenValidateOrderFields_thenThrowIllegalArgumentException() {
        Double maxWeight = null;
        assertThrows(IllegalArgumentException.class, () -> orderUtils.validadeMaxWeight(maxWeight));
    }

    @Test
    public void givenOrderDTOWithPastDeliveryDate_whenValidateOrderFields_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> orderUtils.validateDeliveryDate(LocalDate.now().minusDays(1)));
    }

    @Test
    public void givenOrderDTOWithNullDeliveryDate_whenValidateOrderFields_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> orderUtils.validateDeliveryDate(null));
    }

    @Test
    public void givenOrderDTOWithValidFields_whenValidateOrderFields_thenDoNothing() {
        OrderDTO orderDTO = OrderDTO.builder()
                .height(1.0)
                .width(1.0)
                .maxWeight(1.0)
                .deliveryDate(LocalDate.now().plusDays(1))
                .build();
        assertDoesNotThrow(() -> orderUtils.validateNewOrder(orderDTO));
    }
}
