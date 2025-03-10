package com.medical.bookingapp.repository;

import com.medical.bookingapp.dto.AppointmentSummaryDTO;
import com.medical.bookingapp.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByDoctorIdAndAppointmentDate(Integer doctorId, LocalDateTime appointmentDate);
    List<Appointment> findAll();
    Optional<Appointment> findById(Integer id);
    List<Appointment> findByStatus(String status);
    List<Appointment> findByPatientIdAndAppointmentDateBetween(Integer patientId, LocalDateTime start, LocalDateTime end);
    List<Appointment> findByPatientId(Integer patientId);
    List<Appointment> findByDoctorId(Integer doctorId);
}
