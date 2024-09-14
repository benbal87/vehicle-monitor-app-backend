package com.commsignia.vehiclemonitorappbe.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commsignia.vehiclemonitorappbe.data.VehicleRepository;
import com.commsignia.vehiclemonitorappbe.data.model.Vehicle;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> getVehiclesInRadius(Double latitude, Double longitude, Double radius) {
        return vehicleRepository.findByLocationWithinRadius(latitude, longitude, radius);
    }

    public Vehicle registerNewVehicle() {
        Vehicle vehicle = Vehicle.builder().build();
        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public boolean updateVehicleLocation(Long id, Double latitude, Double longitude) {
        int rowsUpdated = vehicleRepository.updateVehicleLocation(latitude, longitude, id);
        return rowsUpdated > 0;
    }

}
