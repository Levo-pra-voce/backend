package com.levopravoce.backend.services.relatory;

import com.levopravoce.backend.common.SecurityUtils;
import com.levopravoce.backend.entities.Order;
import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.repository.OrderRepository;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.TimeZone;
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

  public Page<Order> getOrdersByDeliveryMan(LocalDate deliveryDate, Pageable pageable) {
    User currentUser = SecurityUtils.getCurrentUser().orElseThrow();

    return deliveryDate == null ? orderRepository.findAllByDeliveryMan(currentUser.getId(),
        pageable)
        : orderRepository.findAllByDeliveryManAndDeliveryDate(currentUser.getId(), deliveryDate,
            pageable);
  }

  public ByteArrayResource getRelatoryXlsx(LocalDate deliveryDate) throws IOException {
    User currentUser = SecurityUtils.getCurrentUser().orElseThrow();
    List<Order> orders =
        deliveryDate == null ? orderRepository.findAllByDeliveryMan(currentUser.getId())
            : orderRepository.findAllByDeliveryManAndDeliveryDate(currentUser.getId(),
                deliveryDate);

    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
      Sheet sheet = workbook.createSheet("Orders");
      int rowNumber = 0;

      for (int i = 0; i <= orders.size(); i++) {
        if (i == 0) {
          Row headerRow = sheet.createRow(rowNumber++);
          headerRow.createCell(0).setCellValue("Nome do cliente");
          headerRow.createCell(1).setCellValue("Valor");
          headerRow.createCell(2).setCellValue("Data da entrega");
        } else {
          Order order = orders.get(i - 1);
          Row row = sheet.createRow(rowNumber++);
          row.createCell(0).setCellValue(order.getClient().getName());
          row.createCell(1).setCellValue(order.getValue());
          SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
          sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

          row.createCell(2).setCellValue(sdf.format(order.getDeliveryDate()));
        }
      }

      workbook.write(byteArrayOutputStream);
      return new ByteArrayResource(byteArrayOutputStream.toByteArray());
    }
  }
}
