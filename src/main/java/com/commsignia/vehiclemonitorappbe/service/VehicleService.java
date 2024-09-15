package com.commsignia.vehiclemonitorappbe.service;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commsignia.vehiclemonitorappbe.controller.model.VehicleDTO;
import com.commsignia.vehiclemonitorappbe.data.VehicleRepository;
import com.commsignia.vehiclemonitorappbe.data.model.Vehicle;
import com.commsignia.vehiclemonitorappbe.mapper.VehicleMapper;
import com.commsignia.vehiclemonitorappbe.util.JsonUtil;
import com.commsignia.vehiclemonitorappbe.websocket.VehicleWebSocketHandler;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    private final VehicleWebSocketHandler vehicleWebSocketHandler;

    public VehicleService(
        VehicleRepository vehicleRepository,
        VehicleWebSocketHandler vehicleWebSocketHandler
    ) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleWebSocketHandler = vehicleWebSocketHandler;
    }

    @Transactional(readOnly = true)
    public List<VehicleDTO> getVehiclesInRadius(Double latitude, Double longitude, Double radius) {
        List<Vehicle> byLocationWithinRadius =
            vehicleRepository.findByLocationWithinRadius(latitude, longitude, radius);
        return VehicleMapper.toDTOList(byLocationWithinRadius);
    }

    @Transactional
    public VehicleDTO registerNewVehicle() {
        Vehicle vehicle = Vehicle.builder().build();
        Vehicle save = vehicleRepository.save(vehicle);
        return VehicleMapper.toDTO(save);
    }

    @Transactional
    public boolean updateVehicleLocation(Long id, Double latitude, Double longitude) {
        int rowsUpdated = vehicleRepository.updateVehicleLocation(latitude, longitude, id);
        return rowsUpdated > 0;
    }

    @Scheduled(fixedRate = 5000)
    @Transactional(readOnly = true)
    public void checkForUpdates() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        String updateMessage = JsonUtil.convertVehicleDataToJson(vehicles);
        System.out.println("### checkForUpdates updateMessage: " + updateMessage);
        vehicleWebSocketHandler.sendUpdate(updateMessage);
    }

}
