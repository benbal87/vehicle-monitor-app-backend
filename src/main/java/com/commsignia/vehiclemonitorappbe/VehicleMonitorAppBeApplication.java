package com.commsignia.vehiclemonitorappbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VehicleMonitorAppBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehicleMonitorAppBeApplication.class, args);
    }

}
