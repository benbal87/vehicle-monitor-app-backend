package com.commsignia.vehiclemonitorappbe.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.commsignia.vehiclemonitorappbe.data.VehicleRepository;
import com.commsignia.vehiclemonitorappbe.data.model.Vehicle;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Set<Vehicle> getVehiclesInRadius(Double latitude, Double longitude, Double radius) {
        return vehicleRepository.findByLocationWithinRadius(latitude, longitude, radius);
    }

    public Vehicle registerNewVehicle() {
        Vehicle vehicle = Vehicle.builder().build();
        return vehicleRepository.save(vehicle);
    }

    public boolean updateVehicleLocation(Long id, Double latitude, Double longitude) {
        int rowsUpdated = vehicleRepository.updateLocationById(latitude, longitude, id);
        return rowsUpdated > 0;
    }

}
