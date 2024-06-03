package com.levopravoce.backend.repository;

import com.levopravoce.backend.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> getVehiclesByUserId(Long userId);

    @Modifying
    @Query(value = """
        UPDATE public.veiculo set ativo = false
            WHERE id_usuario = :userId
""", nativeQuery = true)
    void disableAllVehiclesByUserId(Long userId);

    boolean existsByRenavam(String renavam);

    boolean existsByPlate(String plate);
}
