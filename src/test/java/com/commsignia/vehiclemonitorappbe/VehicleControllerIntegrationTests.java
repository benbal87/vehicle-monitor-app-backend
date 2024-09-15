package com.commsignia.vehiclemonitorappbe;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.commsignia.vehiclemonitorappbe.controller.VehicleController;
import com.commsignia.vehiclemonitorappbe.controller.model.CreateNotificationRequestDTO;
import com.commsignia.vehiclemonitorappbe.controller.model.NotificationDTO;
import com.commsignia.vehiclemonitorappbe.controller.model.VehicleDTO;
import com.commsignia.vehiclemonitorappbe.controller.model.VehicleLocationUpdateRequestDTO;
import com.commsignia.vehiclemonitorappbe.controller.model.VehiclesInRadiusResponseDTO;
import com.commsignia.vehiclemonitorappbe.service.NotificationService;
import com.commsignia.vehiclemonitorappbe.service.VehicleService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(VehicleController.class)
class VehicleControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VehicleService vehicleService;

    @MockBean
    private NotificationService notificationService;

    @InjectMocks
    private VehicleController vehicleController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getVehiclesInRadius_validParameters_shouldReturnVehicles() throws Exception {
        double latitude = 34.052235;
        double longitude = -118.243683;
        double radius = 10.0;

        VehiclesInRadiusResponseDTO responseDTO =
            getVehiclesInRadiusResponseDTO();

        when(vehicleService.getVehiclesInRadius(latitude, longitude, radius)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/vehicle/vehicles")
                .param("latitude", String.valueOf(latitude))
                .param("longitude", String.valueOf(longitude))
                .param("radius", String.valueOf(radius)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.vehicles").isArray());
    }

    private static VehiclesInRadiusResponseDTO getVehiclesInRadiusResponseDTO() {
        List<VehicleDTO> vehicleList = List.of(
            new VehicleDTO(
                1L,
                34.052235,
                -118.243683,
                List.of(
                    new NotificationDTO(1L, "Alert 1"),
                    new NotificationDTO(2L, "Alert 2")
                )
            ),
            new VehicleDTO(
                2L,
                34.062235,
                -118.253683,
                List.of(
                    new NotificationDTO(3L, "Alert 3"),
                    new NotificationDTO(4L, "Alert 4")
                )
            )
        );

        return new VehiclesInRadiusResponseDTO(vehicleList);
    }

    @Test
    void getVehiclesInRadius_missingParameter_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/vehicle/vehicles")
                .param("latitude", String.valueOf(34.052235)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegisterVehicle() throws Exception {
        VehicleDTO vehicleDTO = new VehicleDTO(
            123L,
            34.062235,
            -118.253683,
            List.of(
                new NotificationDTO(1L, "Alert 1"),
                new NotificationDTO(2L, "Alert 2")
            )
        );

        given(vehicleService.registerNewVehicle()).willReturn(vehicleDTO);

        mockMvc.perform(post("/api/v1/vehicle/vehicles")
                .contentType(MediaType.TEXT_PLAIN))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(vehicleDTO.id()));
    }

    @Test
    public void testUpdateVehiclePositionSuccess() throws Exception {
        Long vehicleId = 1L;
        VehicleLocationUpdateRequestDTO requestDTO = new VehicleLocationUpdateRequestDTO(40.7128, 74.0060);

        given(vehicleService.updateVehicleLocation(vehicleId, 40.7128, 74.0060)).willReturn(true);

        mockMvc.perform(post("/api/v1/vehicle/vehicle/" + vehicleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
            .andExpect(status().isOk());
    }

    @Test
    public void testUpdateVehiclePositionBadRequest() throws Exception {
        Long vehicleId = 1L;
        VehicleLocationUpdateRequestDTO requestDTO = new VehicleLocationUpdateRequestDTO(40.7128, 74.0060);

        given(vehicleService.updateVehicleLocation(vehicleId, 40.7128, 74.0060)).willReturn(false);

        mockMvc.perform(post("/api/v1/vehicle/vehicle/" + vehicleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateNotificationSuccess() throws Exception {
        CreateNotificationRequestDTO requestDTO = new CreateNotificationRequestDTO(1L, "Test message");
        NotificationDTO notification = new NotificationDTO(123L, "Test message");

        given(notificationService.createNotificationForVehicle(requestDTO.vehicleId(), requestDTO.message()))
            .willReturn(notification);

        mockMvc.perform(post("/api/v1/vehicle//notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
            .andExpect(status().isOk());
    }

    @Test
    public void testCreateNotificationBadRequest() throws Exception {
        CreateNotificationRequestDTO requestDTO = new CreateNotificationRequestDTO(1L, "Test message");
        NotificationDTO notification = new NotificationDTO(null, "Test message");

        given(notificationService.createNotificationForVehicle(requestDTO.vehicleId(), requestDTO.message()))
            .willReturn(notification);

        mockMvc.perform(post("/api/v1/vehicle//notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
            .andExpect(status().isBadRequest());
    }

}
