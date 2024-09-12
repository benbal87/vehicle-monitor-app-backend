package com.commsignia.vehiclemonitorappbe.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.commsignia.vehiclemonitorappbe.data.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
