package com.commsignia.vehiclemonitorappbe.util;

import java.util.List;

import com.commsignia.vehiclemonitorappbe.data.model.Vehicle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonUtil {

    public static String convertVehicleDataToJson(List<Vehicle> vehicles) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(vehicles);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

}
