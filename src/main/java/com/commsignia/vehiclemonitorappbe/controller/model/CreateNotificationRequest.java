package com.commsignia.vehiclemonitorappbe.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateNotificationRequest {

    @JsonProperty("vehicle_id")
    private Long vehicleId;

    private String message;

}
