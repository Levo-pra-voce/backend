package com.levopravoce.backend.repository;

import com.levopravoce.backend.entities.Order;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

  @Query(
      value = """
                    SELECT EXISTS(SELECT 1 FROM pedido u
                    WHERE u.id_cliente = :clientId
                          AND u.status in ('ESPERANDO', 'EM_PROGRESSO'))
          """, nativeQuery = true)
  boolean existsByStatusInProgressOrPending(
      Long clientId
  );

  @Query(
      value = """
                    SELECT u FROM Order u
                      WHERE u.deliveryman.id = :deliveryUserId
                        AND (u.status = 'ESPERANDO' or u.status = 'EM_PROGRESSO')
          """)
  List<Order> findByStatusPendingOrInProgress(
      Long deliveryUserId
  );

  @Query(
      value = """
              SELECT u FROM Order u
              WHERE u.deliveryman.id = :deliveryId
                    AND u.status = 'FEITO_PAGAMENTO'
          """)
  List<Order> findAllByDeliveryMan(Long deliveryId);

  @Query(
      value = """
              SELECT u FROM Order u
              WHERE u.deliveryman.id = :deliveryId
                    AND u.deliveryDate BETWEEN :inicialDate AND :finalDate
                    AND u.status = 'FEITO_PAGAMENTO'
          """)
  List<Order> findAllByDeliveryManAndDeliveryDate(Long deliveryId, LocalDateTime inicialDate,
      LocalDateTime finalDate);

  @Query(
      value = """
              SELECT u FROM Order u
                  WHERE
                      u.deliveryman.id = :deliveryId
                      AND u.status = 'FEITO_PAGAMENTO'
          """)
  Page<Order> findAllByDeliveryMan(Long deliveryId, Pageable pageable);

  @Query(
      value = """
                  SELECT u FROM Order u
              WHERE u.deliveryman.id = :deliveryId
                    AND u.deliveryDate BETWEEN :inicialDate AND :finalDate
                    AND u.status = 'FEITO_PAGAMENTO'
          """)
  Page<Order> findAllByDeliveryManAndDeliveryDate(Long deliveryId, LocalDateTime inicialDate,
      LocalDateTime finalDate, Pageable pageable);

  @Query(
      value = """
              SELECT u FROM Order u
                WHERE (u.deliveryman.id = :userId or u.client.id = :userId)
                      AND (u.status = 'EM_PROGRESSO' or u.status = 'ENTREGADO')
          """)
  Optional<Order> findLastOrderInProgress(Long userId);

  @Query(
      value = """
              SELECT u FROM Order u
                WHERE (u.deliveryman.id = :userId or u.client.id = :userId)
                      AND u.status = 'ESPERANDO'
          """)
  Optional<Order> findLastOrderInPending(Long userId);
}
