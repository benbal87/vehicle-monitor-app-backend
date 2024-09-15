package com.commsignia.vehiclemonitorappbe.util;

import java.util.List;

import com.commsignia.vehiclemonitorappbe.data.model.Vehicle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonUtil {

    public static String convertVehicleDataToJson(List<Vehicle> vehicles) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(vehicles);
        } catch (JsonProcessingException e) {
            log.error("Error converting vehicle data to JSON", e);
            return "[]";
        }
    }

}
