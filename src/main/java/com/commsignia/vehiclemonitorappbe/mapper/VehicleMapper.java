package com.commsignia.vehiclemonitorappbe.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.commsignia.vehiclemonitorappbe.controller.model.VehicleDTO;
import com.commsignia.vehiclemonitorappbe.data.model.Vehicle;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VehicleMapper {

    public static VehicleDTO toDTO(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        return new VehicleDTO(
            vehicle.getId(),
            vehicle.getLatitude(),
            vehicle.getLongitude(),
            NotificationMapper.toDTOList(vehicle.getNotifications())
        );
    }

    public static List<VehicleDTO> toDTOList(List<Vehicle> vehicles) {
        if (vehicles == null) {
            return null;
        }
        return vehicles.stream()
            .map(VehicleMapper::toDTO)
            .collect(Collectors.toList());
    }

}
