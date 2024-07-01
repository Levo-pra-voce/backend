package com.levopravoce.backend.repository;

import com.levopravoce.backend.entities.Request;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RequestRepository extends JpaRepository<Request, Long> {
  @Query(
      value = """
              SELECT u FROM Request u
                  WHERE u.deliveryman.id = :deliveryId AND u.order.id = :orderId
          """)
  Optional<Request> findByDeliveryManAndOrder(Long deliveryId, Long orderId);

  @Query(
      value = """
              SELECT u FROM Request u
                  WHERE u.deliveryman.id = :deliveryId and u.order.status = 'ESPERANDO'
          """)
  List<Request> findAllByDeliveryManAndOrder(Long deliveryId);
}
