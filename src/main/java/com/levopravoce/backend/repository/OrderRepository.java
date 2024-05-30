package com.levopravoce.backend.repository;

import com.levopravoce.backend.entities.Order;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
        WHERE u.deliveryman.id = :deliveryId
    """)
    List<Order> findAllByDeliveryMan(Long deliveryId);

    @Query(
            value = """
                    SELECT u FROM Order u
                    WHERE u.deliveryman.id = :deliveryId
                          AND u.deliveryDate = :deliveryDate AND u.status = 'ENTREGADO'
                """)
    List<Order> findAllByDeliveryManAndDeliveryDate(Long deliveryId, LocalDate deliveryDate);

    @Query(
            value = """
                    SELECT u FROM Order u
                        WHERE
                            u.deliveryman.id = :deliveryId
                            AND u.status = 'ENTREGADO'
                            AND u.payment.status = 'PAGO'
                """)
    Page<Order> findAllByDeliveryMan(Long deliveryId, Pageable pageable);

    @Query(
            value = """
            SELECT u FROM Order u
        WHERE u.deliveryman.id = :deliveryId
              AND u.deliveryDate BETWEEN :inicialDate AND :finalDate
              AND u.status = 'ENTREGADO'
              AND u.payment.status = 'PAGO'
    """)
    Page<Order> findAllByDeliveryManAndDeliveryDate(Long deliveryId, LocalDateTime inicialDate, LocalDateTime finalDate, Pageable pageable);
}
