package com.commsignia.vehiclemonitorappbe.data;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.commsignia.vehiclemonitorappbe.data.model.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    @Query("SELECT v FROM Vehicle v WHERE sqrt(pow(v.latitude - :latitude, 2) + pow(v.longitude - :longitude, 2)) <= "
           + ":radius")
    Set<Vehicle> findByLocationWithinRadius(
        @Param("latitude") Double latitude,
        @Param("longitude") Double longitude,
        @Param("radius") Double radius
    );

    @Modifying
    @Query("UPDATE Vehicle v SET v.latitude = :latitude, v.longitude = :longitude WHERE v.id = :id")
    int updateLocationById(
        @Param("latitude") Double latitude,
        @Param("longitude") Double longitude,
        @Param("id") Long id
    );

}
