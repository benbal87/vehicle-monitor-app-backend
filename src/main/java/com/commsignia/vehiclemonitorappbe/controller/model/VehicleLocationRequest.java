package com.commsignia.vehiclemonitorappbe.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleLocationRequest {

    private double latitude;

    private double longitude;

}
