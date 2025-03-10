package com.medical.bookingapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_appointment")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;

    @Column(name = "status", length = 50)
    private String status = "PENDING";

    @Column(name = "reason")
    private String reason;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;  // Thêm Service
    private String medicalNote; // ✅ Thêm trường này

}
