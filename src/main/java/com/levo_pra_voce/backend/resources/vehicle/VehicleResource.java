package com.levo_pra_voce.backend.resources.vehicle;

import com.levo_pra_voce.backend.entities.Vehicle;
import com.levo_pra_voce.backend.services.vehicle.VehicleService;
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

    @PostMapping
    public Vehicle createVehicle(@RequestBody Vehicle vehicle) {
        return this.vehicleService.createVehicle(vehicle);
    }

    @PutMapping
    public Vehicle updateVehicle(@RequestBody Vehicle vehicle) {
        return this.vehicleService.updateVehicle(vehicle);
    }

    @DeleteMapping
    public void deleteVehicle(@RequestBody Vehicle vehicle) {
        this.vehicleService.deleteVehicle(vehicle);
    }
}
