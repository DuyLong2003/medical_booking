package com.medical.bookingapp.mapper;

import com.medical.bookingapp.dto.AppointmentDTO;
import com.medical.bookingapp.entity.Appointment;
import com.medical.bookingapp.entity.Doctor;
import com.medical.bookingapp.entity.Patient;
import com.medical.bookingapp.entity.Service;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AppointmentMapper {

    public static AppointmentDTO toDTO(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setPatientId(appointment.getPatient().getId());
        dto.setDoctorId(appointment.getDoctor().getId());
        dto.setServiceId(appointment.getService().getId());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setAppointmentDateStr(appointment.getAppointmentDate().toString()); // Format nếu cần
        dto.setReason(appointment.getReason());
        dto.setNotes(appointment.getNotes());
        dto.setMedicalNote(appointment.getMedicalNote());
        dto.setPatientName(appointment.getPatient().getUser().getFullName());
        dto.setDoctorName(appointment.getDoctor().getUser().getFullName());
        dto.setServiceName(appointment.getService().getName());
        dto.setId(appointment.getId());
        dto.setStatus(appointment.getStatus());
        return dto;
    }

    public static Appointment toEntity(AppointmentDTO dto, Doctor doctor, Patient patient, Service service) {
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setService(service);
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setReason(dto.getReason());
        appointment.setNotes(dto.getNotes());
        appointment.setMedicalNote(dto.getMedicalNote());
        return appointment;
    }
}
