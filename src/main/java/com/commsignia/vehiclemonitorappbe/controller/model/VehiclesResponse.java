package com.commsignia.vehiclemonitorappbe.controller.model;

import java.util.List;

import com.commsignia.vehiclemonitorappbe.data.model.Vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehiclesResponse {

    private List<Vehicle> vehicles;

}
