package com.levopravoce.backend.services.relatory;

import com.levopravoce.backend.common.SecurityUtils;
import com.levopravoce.backend.entities.Order;
import com.levopravoce.backend.entities.Order.OrderStatus;
import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.repository.OrderRepository;
import com.levopravoce.backend.services.relatory.dto.RelatoryDTO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RelatoryService {

  private final OrderRepository orderRepository;

  public Page<RelatoryDTO> getOrdersByDeliveryMan(LocalDate deliveryDate, Pageable pageable) {
    User currentUser = SecurityUtils.getCurrentUser().orElseThrow();
    if (deliveryDate == null) {
      return orderRepository.findAllByDeliveryMan(currentUser.getId(), pageable)
          .map(this::createRelatoryDTO);
    }

    LocalDateTime inicialDate = deliveryDate.atTime(0, 0, 0);
    LocalDateTime finalDate = deliveryDate.atTime(23, 59, 59);
    return orderRepository.findAllByDeliveryManAndDeliveryDate(currentUser.getId(), inicialDate,
            finalDate, pageable)
        .map(this::createRelatoryDTO);
  }

  public ByteArrayResource getRelatoryXlsx(LocalDate deliveryDate) throws IOException {
    User currentUser = SecurityUtils.getCurrentUser().orElseThrow();

    List<Order> orders;
    if (deliveryDate == null) {
      orders = orderRepository.findAllByDeliveryMan(currentUser.getId());
    } else {
      LocalDateTime inicialDate = deliveryDate.atTime(0, 0, 0);
      LocalDateTime finalDate = deliveryDate.atTime(23, 59, 59);
      orders = orderRepository.findAllByDeliveryManAndDeliveryDate(currentUser.getId(), inicialDate,
              finalDate);
    }

    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
      Sheet sheet = workbook.createSheet("Orders");
      int rowNumber = 0;

      for (int i = 0; i <= orders.size(); i++) {
        if (i == 0) {
          Row headerRow = sheet.createRow(rowNumber++);
          headerRow.createCell(0).setCellValue("Nome do cliente");
          headerRow.createCell(1).setCellValue("Valor");
          headerRow.createCell(2).setCellValue("Data da entrega");
          headerRow.createCell(3).setCellValue("Pagamento realizado");
        } else {
          Order order = orders.get(i - 1);
          Row row = sheet.createRow(rowNumber++);
          row.createCell(0).setCellValue(order.getClient().getName());
          row.createCell(1).setCellValue(order.getValue());
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
          row.createCell(2).setCellValue(Optional.ofNullable(order.getDeliveryDate())
              .map(date -> date.format(formatter))
              .orElse(null));
          row.createCell(3).setCellValue(Optional.ofNullable(order.getStatus())
              .map(status -> OrderStatus.FEITO_PAGAMENTO.equals(status) ? "Sim" : "Não")
              .orElse(null));
        }
      }

      workbook.write(byteArrayOutputStream);
      return new ByteArrayResource(byteArrayOutputStream.toByteArray());
    }
  }

  private RelatoryDTO createRelatoryDTO(Order order) {
    return RelatoryDTO.builder()
        .clientName(order.getClient().getName())
        .value(order.getValue())
        .deliveryDate(Optional.ofNullable(order.getDeliveryDate())
            .map(date -> {
              DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
              return date.format(formatter);
            })
            .orElse(null))
        .build();
  }
}
