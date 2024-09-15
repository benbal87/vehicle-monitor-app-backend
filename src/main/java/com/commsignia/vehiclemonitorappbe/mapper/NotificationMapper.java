package com.commsignia.vehiclemonitorappbe.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.commsignia.vehiclemonitorappbe.controller.model.NotificationDTO;
import com.commsignia.vehiclemonitorappbe.data.model.Notification;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NotificationMapper {

    public static NotificationDTO toDTO(Notification notification) {
        if (notification == null) {
            return null;
        }

        return new NotificationDTO(
            notification.getId(),
            notification.getMessage()
        );
    }

    public static List<NotificationDTO> toDTOList(Set<Notification> notifications) {
        if (notifications == null) {
            return null;
        }

        return notifications.stream()
            .map(NotificationMapper::toDTO)
            .collect(Collectors.toList());
    }

}
