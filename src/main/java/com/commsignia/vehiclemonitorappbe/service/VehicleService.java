package com.commsignia.vehiclemonitorappbe.service;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commsignia.vehiclemonitorappbe.controller.model.VehicleDTO;
import com.commsignia.vehiclemonitorappbe.controller.model.VehiclesInRadiusResponseDTO;
import com.commsignia.vehiclemonitorappbe.data.VehicleRepository;
import com.commsignia.vehiclemonitorappbe.data.model.Vehicle;
import com.commsignia.vehiclemonitorappbe.mapper.VehicleMapper;
import com.commsignia.vehiclemonitorappbe.util.JsonUtil;
import com.commsignia.vehiclemonitorappbe.websocket.VehicleWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
    public VehiclesInRadiusResponseDTO getVehiclesInRadius(Double latitude, Double longitude, Double radius) {
        log.debug("Fetching vehicles within radius. Latitude={}, Longitude={}, Radius={}", latitude, longitude, radius);
        List<Vehicle> byLocationWithinRadius =
            vehicleRepository.findByLocationWithinRadius(latitude, longitude, radius);
        List<VehicleDTO> dtoList = VehicleMapper.toDTOList(byLocationWithinRadius);
        return new VehiclesInRadiusResponseDTO(dtoList);
    }

    @Transactional
    public VehicleDTO registerNewVehicle() {
        log.debug("Registering a new vehicle");
        Vehicle vehicle = Vehicle.builder().build();
        Vehicle save = vehicleRepository.save(vehicle);
        return VehicleMapper.toDTO(save);
    }

    @Transactional
    public boolean updateVehicleLocation(Long id, Double latitude, Double longitude) {
        log.debug("Updating vehicle location for ID={} with latitude={} and longitude={}", id, latitude, longitude);
        int rowsUpdated = vehicleRepository.updateVehicleLocation(latitude, longitude, id);
        return rowsUpdated > 0;
    }

    @Scheduled(fixedRate = 5000)
    @Transactional(readOnly = true)
    public void checkForUpdates() {
        log.debug("checkForUpdates called | finding all vehicles in database");
        List<Vehicle> vehicles = vehicleRepository.findAll();
        String updateMessage = JsonUtil.convertVehicleDataToJson(vehicles);
        vehicleWebSocketHandler.sendUpdate(updateMessage);
    }

}
