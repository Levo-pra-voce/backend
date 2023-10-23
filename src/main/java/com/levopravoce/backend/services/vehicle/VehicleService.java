package com.levopravoce.backend.services.vehicle;

import com.levopravoce.backend.common.AuthUtils;
import com.levopravoce.backend.common.SecurityUtils;
import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.entities.Vehicle;
import com.levopravoce.backend.repository.UserRepository;
import com.levopravoce.backend.repository.VehicleRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
  private final AuthUtils authUtils;

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
    if (!Objects.equals(vehicleForUpdate.getRatings(), vehicleToUpdate.getRatings())) {
            throw new IllegalArgumentException(NOT_POSSIBLE_TO_UPDATE_VEHICLE_MESSAGE);
        }
    if (!Objects.equals(vehicleForUpdate.getCreationDate(), vehicleToUpdate.getCreationDate())) {
            throw new IllegalArgumentException(NOT_POSSIBLE_TO_UPDATE_VEHICLE_MESSAGE);
        }
        vehicleToUpdate.setPlate(vehicleForUpdate.getPlate());
        return this.vehicleRepository.save(vehicleToUpdate);
    }

    public void deleteVehicle(Vehicle vehicle) {
        var vehicleToDelete = this.vehicleRepository
                .findById(vehicle.getId())
                .orElseThrow();

    if (!authUtils.emailIsSame(vehicleToDelete.getUser().getEmail())) {
            throw new IllegalArgumentException("You can't delete this vehicle");
        }

        vehicleToDelete.setActive(false);

        this.vehicleRepository.save(vehicleToDelete);
    }

    public List<Vehicle> getVehiclesByUserId(Long userId) {
        return this.vehicleRepository.getVehiclesByUserId(userId);
    }
}
