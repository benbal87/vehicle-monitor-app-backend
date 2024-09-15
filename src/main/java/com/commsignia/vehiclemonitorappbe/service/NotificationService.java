package com.commsignia.vehiclemonitorappbe.service;

import org.springframework.stereotype.Service;

import com.commsignia.vehiclemonitorappbe.controller.model.NotificationDTO;
import com.commsignia.vehiclemonitorappbe.data.NotificationRepository;
import com.commsignia.vehiclemonitorappbe.data.model.Notification;
import com.commsignia.vehiclemonitorappbe.data.model.Vehicle;
import com.commsignia.vehiclemonitorappbe.mapper.NotificationMapper;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationDTO createNotificationForVehicle(long vehicleId, String message) {
        var vehicle = Vehicle.builder().id(vehicleId).build();
        var notification = Notification.builder().vehicle(vehicle).message(message).build();
        Notification save = notificationRepository.save(notification);
        return NotificationMapper.toDTO(save);
    }

}
