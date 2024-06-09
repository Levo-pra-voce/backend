package com.levopravoce.backend.resources.vehicle;

import com.levopravoce.backend.entities.Vehicle;
import com.levopravoce.backend.services.vehicle.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
public class VehicleResource {
    private final VehicleService vehicleService;

    @GetMapping("/{userId}")
    public List<Vehicle> getVehiclesByUserId(@PathVariable Long userId) {
        return this.vehicleService.getVehiclesByUserId(userId);
    }
}
