package com.commsignia.vehiclemonitorappbe.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateNotificationRequestDTO(
    @JsonProperty("vehicle_id") Long vehicleId,
    String message
) {
}
