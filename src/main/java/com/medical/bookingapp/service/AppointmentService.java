package com.medical.bookingapp.service;

import com.medical.bookingapp.dto.AppointmentDTO;
import com.medical.bookingapp.dto.ServiceDTO;
import com.medical.bookingapp.dto.UserDoctorDTO;
import com.medical.bookingapp.entity.Appointment;
import com.medical.bookingapp.entity.Doctor;
import com.medical.bookingapp.entity.Leave;
import com.medical.bookingapp.mapper.AppointmentMapper;
import com.medical.bookingapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final LeaveRepository leaveRepository;
    private final ReviewRepository reviewRepository;

    // Lấy danh sách khung giờ khả dụng cho bác sĩ
    public List<String> getAvailableTimeSlots(Integer doctorId, LocalDate appointmentDate) {
        List<String> availableSlots = new ArrayList<>();
        LocalTime[] timeSlots = {
                LocalTime.of(7, 0), LocalTime.of(7, 30),
                LocalTime.of(8, 0), LocalTime.of(8, 30),
                LocalTime.of(9, 0), LocalTime.of(9, 30),
                LocalTime.of(10, 0), LocalTime.of(10, 30),
                LocalTime.of(13, 0), LocalTime.of(13, 30),
                LocalTime.of(14, 0), LocalTime.of(14, 30),
                LocalTime.of(15, 0), LocalTime.of(15, 30),
                LocalTime.of(16, 0), LocalTime.of(16, 30),
        };

        for (LocalTime slot : timeSlots) {
            LocalDateTime start = LocalDateTime.of(appointmentDate, slot);
            LocalDateTime end = start.plusMinutes(30);
            LocalDateTime cleanStart = start.truncatedTo(ChronoUnit.SECONDS);
            LocalDateTime cleanEnd = end.truncatedTo(ChronoUnit.SECONDS);
            System.out.println("Checking leave for doctorId: " + doctorId);
            System.out.println("Start time: " + start.truncatedTo(ChronoUnit.SECONDS));
            System.out.println("End time: " + end.truncatedTo(ChronoUnit.SECONDS));

            boolean isTaken = !appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, cleanStart).isEmpty();
            boolean isOnLeave = !leaveRepository.findConflictingLeaves(doctorId, cleanStart, cleanEnd).isEmpty(); // Fix sử dụng query mới
            if (!isTaken && !isOnLeave) {
                availableSlots.add(slot.toString() + " - " + slot.plusMinutes(30));
            }
        }

        return availableSlots.isEmpty() ? List.of("No available time") : availableSlots;
    }



    // Đặt lịch hẹn
    public Appointment bookAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }


    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(AppointmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AppointmentDTO updateAppointment(int id, AppointmentDTO dto) throws Exception {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new Exception("Appointment not found"));

        appointment.setNotes(dto.getNotes());
        appointment.setMedicalNote(dto.getMedicalNote());
        appointment.setStatus(dto.getStatus());

        appointmentRepository.save(appointment);
        return AppointmentMapper.toDTO(appointment);
    }

    public List<AppointmentDTO> getAppointmentsByPatientId(Integer patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        return appointments.stream()
                .map(a -> new AppointmentDTO(
                       a.getPatient().getUser().getId(),
                        a.getDoctor().getUser().getId(),
                        a.getService().getId(),
                        a.getId(),
                        a.getAppointmentDate(),
                        a.getReason(),
                        a.getNotes(),
                        a.getAppointmentDate().toString(),
                        a.getMedicalNote(),
                        a.getStatus(),
                        a.getPatient().getUser().getFullName(),
                        a.getDoctor().getUser().getFullName(),
                        a.getService().getName(),
                        reviewRepository.existsByAppointmentId(a.getId())
                ))
                .collect(Collectors.toList());
    }

    public List<AppointmentDTO> getAppointmentsForDoctor(Integer doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);

        return appointments.stream().map(a -> new AppointmentDTO(
                        a.getPatient().getUser().getId(),
                        a.getDoctor().getUser().getId(),
                        a.getService().getId(),
                        a.getId(),
                        a.getAppointmentDate(),
                        a.getReason(),
                        a.getNotes(),
                        a.getAppointmentDate().toString(),
                        a.getMedicalNote(),
                        a.getStatus(),
                        a.getPatient().getUser().getFullName(),
                        a.getDoctor().getUser().getFullName(),
                        a.getService().getName(),
                        reviewRepository.existsByAppointmentId(a.getId())
                ))
                .collect(Collectors.toList());
    }
}

