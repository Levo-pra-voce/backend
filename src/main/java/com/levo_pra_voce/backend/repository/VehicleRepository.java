package com.levo_pra_voce.backend.repository;

import com.levo_pra_voce.backend.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> getVehiclesByUserId(Long userId);
}
