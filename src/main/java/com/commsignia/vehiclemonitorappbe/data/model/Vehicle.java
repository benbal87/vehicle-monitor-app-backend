package com.commsignia.vehiclemonitorappbe.data.model;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "vehicle")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Vehicle {

    @Id
    @SequenceGenerator(
        name = "vehicle_sequence",
        sequenceName = "vehicle_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "vehicle_sequence"
    )
    private Long id;

    @NonNull
    private Double latitude;

    @NonNull
    private Double longitude;

    @OneToMany(mappedBy = "vehicle")
    private Set<Notification> notifications;

}
