package com.levopravoce.backend.repository;

import com.levopravoce.backend.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> getVehiclesByUserId(Long userId);
}
