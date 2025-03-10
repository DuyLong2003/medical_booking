package com.medical.bookingapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class AppointmentRequestDTO {
    private UserPatientDTO userInfo;
    private List<AppointmentDTO> appointments;
}