package com.commsignia.vehiclemonitorappbe.controller.model;

import java.util.List;

public record VehicleDTO(
    Long id,
    Double latitude,
    Double longitude,
    List<NotificationDTO> notifications
) {
}
