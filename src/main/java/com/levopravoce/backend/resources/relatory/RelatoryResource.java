package com.levopravoce.backend.resources.relatory;

import com.levopravoce.backend.services.relatory.RelatoryService;
import com.levopravoce.backend.services.relatory.dto.RelatoryDTO;
import java.io.IOException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/relatory")
public class RelatoryResource {
  private final RelatoryService relatoryService;

  @GetMapping
  public Page<RelatoryDTO> getOrdersByDeliveryMan(
      LocalDate deliveryDate,
      @PageableDefault Pageable pageable
  ) {
    return relatoryService.getOrdersByDeliveryMan(deliveryDate, pageable);
  }

  @GetMapping("/xlsx")
  public ResponseEntity<ByteArrayResource> getRelatoryXlsx(
      LocalDate deliveryDate
  ) throws IOException {
    ByteArrayResource byteArrayResource = relatoryService.getRelatoryXlsx(deliveryDate);
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatory.xlsx");

    return ResponseEntity.ok()
        .contentLength(byteArrayResource.contentLength())
        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
        .headers(headers)
        .body(byteArrayResource);
  }
}
