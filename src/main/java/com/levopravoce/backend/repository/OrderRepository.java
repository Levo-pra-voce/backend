package com.levopravoce.backend.repository;

import com.levopravoce.backend.entities.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(
            value = """
              SELECT EXISTS(SELECT 1 FROM pedido u 
              WHERE u.id_cliente = :clientId 
                    AND u.status in ('PENDING', 'IN_PROGRESS'))
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
}
