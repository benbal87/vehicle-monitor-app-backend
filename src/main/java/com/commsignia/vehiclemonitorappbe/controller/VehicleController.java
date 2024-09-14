package com.commsignia.vehiclemonitorappbe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.commsignia.vehiclemonitorappbe.controller.model.CreateNotificationRequest;
import com.commsignia.vehiclemonitorappbe.controller.model.VehicleLocationRequest;
import com.commsignia.vehiclemonitorappbe.controller.model.VehicleRegistrationResponse;
import com.commsignia.vehiclemonitorappbe.controller.model.VehiclesResponse;
import com.commsignia.vehiclemonitorappbe.data.model.Notification;
import com.commsignia.vehiclemonitorappbe.data.model.Vehicle;
import com.commsignia.vehiclemonitorappbe.service.NotificationService;
import com.commsignia.vehiclemonitorappbe.service.VehicleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("api/v1/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    private final NotificationService notificationService;

    public VehicleController(VehicleService vehicleService, NotificationService notificationService) {
        this.vehicleService = vehicleService;
        this.notificationService = notificationService;
    }

    @Operation(
        summary = "Get all vehicles in radius.",
        description = "Query the vehicles in the circle with the given center point and radius.",
        parameters = {
            @Parameter(in = ParameterIn.QUERY,
                       name = "latitude",
                       required = true,
                       description = "The latitude of the circle's center point."),
            @Parameter(in = ParameterIn.QUERY,
                       name = "longitude",
                       required = true,
                       description = "The longitude of the circle's center point."),
            @Parameter(in = ParameterIn.QUERY,
                       name = "radius",
                       required = true,
                       description = "The radius of the circle."),
        },
        responses = {
            @ApiResponse(responseCode = "200",
                         description = "Successful Operation",
                         content = @Content(mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = Vehicle.class))))
        }
    )
    @GetMapping("/vehicles")
    @ResponseBody
    public ResponseEntity<VehiclesResponse> getVehiclesInRadius(
        @RequestParam double latitude,
        @RequestParam double longitude,
        @RequestParam double radius
    ) {
        var vehiclesInRadius = vehicleService.getVehiclesInRadius(latitude, longitude, radius);
        var response = new VehiclesResponse(vehiclesInRadius);
        return ResponseEntity.ok().body(response);
    }

    @Operation(
        summary = "Register the vehicle.",
        description = "Register a new vehicle without any data and give back just it's generated id.",
        responses = {
            @ApiResponse(responseCode = "200",
                         description = "The ID of the registered vehicle",
                         content = @Content(mediaType = "text/plain"))
        }
    )
    @PostMapping("/vehicles")
    @ResponseBody
    public ResponseEntity<VehicleRegistrationResponse> registerVehicle() {
        var vehicleId = vehicleService.registerNewVehicle().getId();
        var response = new VehicleRegistrationResponse(vehicleId);
        return ResponseEntity.ok().body(response);
    }

    @Operation(
        summary = "Update vehicle's position.",
        description = "Update the position of a specific vehicle based on it's unique id.",
        parameters = {
            @Parameter(in = ParameterIn.PATH,
                       name = "id",
                       required = true,
                       description = "The ID of the vehicle."),
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "The location of the vehicle with latitude and longitude.",
            required = true,
            content = @Content(schema = @Schema(implementation = CreateNotificationRequest.class))),
        responses = {
            @ApiResponse(responseCode = "200")
        }
    )
    @PostMapping("/vehicle/{id}")
    @ResponseBody
    public ResponseEntity<Void> updateVehiclePosition(
        @PathVariable Long id,
        @RequestBody VehicleLocationRequest body
    ) {
        boolean isUpdated = vehicleService.updateVehicleLocation(id, body.getLatitude(), body.getLongitude());
        return isUpdated ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @Operation(
        summary = "Create a notification.",
        description = "Create a notification for a specific vehicle with a message.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Notification create request.",
            required = true,
            content = @Content(schema = @Schema(implementation = CreateNotificationRequest.class))),
        responses = {
            @ApiResponse(responseCode = "200")
        }
    )
    @PostMapping("/notifications")
    @ResponseBody
    public ResponseEntity<Void> createNotification(@RequestBody CreateNotificationRequest request) {
        Notification notification =
            notificationService.createNotificationForVehicle(request.getVehicleId(), request.getMessage());
        return notification.getId() != null ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

}

