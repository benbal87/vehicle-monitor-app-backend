package com.commsignia.vehiclemonitorappbe.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.commsignia.vehiclemonitorappbe.data.model.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    @Query(
        value = "SELECT * FROM vehicle WHERE sqrt(pow(latitude - :latitude, 2) + pow(longitude - :longitude, 2)) <= "
                + ":radius",
        nativeQuery = true
    )
    List<Vehicle> findByLocationWithinRadius(
        @Param("latitude") Double latitude,
        @Param("longitude") Double longitude,
        @Param("radius") Double radius
    );

    @Modifying
    @Query(
        "UPDATE Vehicle v SET v.latitude = :latitude, v.longitude = :longitude WHERE v.id = :vehicleId"
    )
    int updateVehicleLocation(
        @Param("latitude") Double latitude,
        @Param("longitude") Double longitude,
        @Param("vehicleId") Long vehicleId
    );

}
