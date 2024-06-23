package com.levopravoce.backend.repository;

import com.levopravoce.backend.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query(
            value = """
              SELECT EXISTS(SELECT 1 FROM solicitacao u
              WHERE u.id_pedido = :orderId
                    AND u.status in ('SOLICITADO', 'ACEITO', 'RECUSADO'))
    """, nativeQuery = true)
boolean existsByStatusInProgressOrPending(
            Long orderId
    );

    @Query(
            value = """
              SELECT u FROM Request u
                WHERE u.deliveryman.id = :deliveryUserId 
                  AND (u.status = 'SOLICITADO' or u.status = 'ACEITO')
    """)
    List<Request> findByStatusPendingOrInProgress(
        Long deliveryUserId
    );

@Query(
            value = """
        SELECT u FROM Request u
        WHERE u.deliveryman.id = :deliveryId
              AND u.status = 'ACEITO'
    """)
    List<Request> findAllByDeliveryMan(Long deliveryId);
}
