package com.commsignia.vehiclemonitorappbe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.commsignia.vehiclemonitorappbe.websocket.VehicleWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final VehicleWebSocketHandler vehicleWebSocketHandler;

    public WebSocketConfig(VehicleWebSocketHandler vehicleWebSocketHandler) {
        this.vehicleWebSocketHandler = vehicleWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
            .addHandler(this.vehicleWebSocketHandler, "/vehicle-updates")
            .setAllowedOrigins("*");
    }

}
