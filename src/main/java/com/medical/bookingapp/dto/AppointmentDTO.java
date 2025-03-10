package com.medical.bookingapp.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AppointmentDTO {
    private int patientId;
    private int doctorId;
    private int serviceId;
    private int id;
    private LocalDateTime appointmentDate;
    private String reason;
    private String notes;
    private String appointmentDateStr;
    private String medicalNote;
    private String status;

    private String patientName;
    private String doctorName;
    private String serviceName;
    private boolean hasReview = false;
}
