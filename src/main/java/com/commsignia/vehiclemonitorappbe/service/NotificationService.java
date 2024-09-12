package com.commsignia.vehiclemonitorappbe.service;

import org.springframework.stereotype.Service;

import com.commsignia.vehiclemonitorappbe.data.NotificationRepository;
import com.commsignia.vehiclemonitorappbe.data.model.Notification;
import com.commsignia.vehiclemonitorappbe.data.model.Vehicle;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification createNotificationForVehicle(long vehicleId, String message) {
        var vehicle = Vehicle.builder().id(vehicleId).build();
        var notification = Notification.builder().vehicle(vehicle).message(message).build();
        return notificationRepository.save(notification);
    }

}
