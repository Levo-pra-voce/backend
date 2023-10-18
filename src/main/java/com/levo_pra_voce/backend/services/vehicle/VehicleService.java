package com.levo_pra_voce.backend.services.vehicle;

import com.levo_pra_voce.backend.common.SecurityUtils;
import com.levo_pra_voce.backend.entities.User;
import com.levo_pra_voce.backend.entities.Vehicle;
import com.levo_pra_voce.backend.repository.UserRepository;
import com.levo_pra_voce.backend.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    private final String NOT_POSSIBLE_TO_UPDATE_VEHICLE_MESSAGE = "You can't update this vehicle";

    public Vehicle createVehicle(Vehicle vehicle) {
        User user = userRepository.findByEmail(SecurityUtils.getCurrentUsername()).orElseThrow();

        vehicle.setUser(user);

        return this.vehicleRepository.save(vehicle);
    }

    public Vehicle updateVehicle(Vehicle vehicleForUpdate) {
        var vehicleToUpdate = this.vehicleRepository
                .findById(vehicleForUpdate.getId())
                .orElseThrow();

        if (Optional.ofNullable(vehicleForUpdate.getUser().getEmail()).filter(email -> email.equals(SecurityUtils.getCurrentUsername())).isEmpty()) {
            throw new IllegalArgumentException(NOT_POSSIBLE_TO_UPDATE_VEHICLE_MESSAGE);
        }

        vehicleToUpdate.setActive(vehicleForUpdate.isActive());
        vehicleToUpdate.setColor(vehicleForUpdate.getColor());
        vehicleToUpdate.setModel(vehicleForUpdate.getModel());
        if (!vehicleForUpdate.getRatings().equals(vehicleToUpdate.getRatings())) {
            throw new IllegalArgumentException(NOT_POSSIBLE_TO_UPDATE_VEHICLE_MESSAGE);
        }
        if (!vehicleForUpdate.getCreationDate().equals(vehicleToUpdate.getCreationDate())) {
            throw new IllegalArgumentException(NOT_POSSIBLE_TO_UPDATE_VEHICLE_MESSAGE);
        }
        vehicleToUpdate.setPlate(vehicleForUpdate.getPlate());
        return this.vehicleRepository.save(vehicleToUpdate);
    }

    public void deleteVehicle(Vehicle vehicle) {
        var vehicleToDelete = this.vehicleRepository
                .findById(vehicle.getId())
                .orElseThrow();

        if (!vehicle.getUser().getEmail().equals(SecurityUtils.getCurrentUsername())) {
            throw new IllegalArgumentException("You can't delete this vehicle");
        }

        vehicleToDelete.setActive(false);

        this.vehicleRepository.save(vehicleToDelete);
    }

    public List<Vehicle> getVehiclesByUserId(Long userId) {
        return this.vehicleRepository.getVehiclesByUserId(userId);
    }
}