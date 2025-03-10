package com.medical.bookingapp.controller;

import com.medical.bookingapp.dto.AppointmentDTO;
import com.medical.bookingapp.service.AppointmentService;
import com.medical.bookingapp.service.AuthenticationService;
import com.medical.bookingapp.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DoctorAppointmentController {

    private final AppointmentService appointmentService;
    private final AuthenticationService authenticationService;
    @GetMapping("/doctor/appointments")
    public String getDoctorAppointments(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Integer doctorId = authenticationService.getCurrentDoctorId(); // Giả sử username là ID bác sĩ
        List<AppointmentDTO> appointments = appointmentService.getAppointmentsForDoctor(doctorId);

        model.addAttribute("appointments", appointments);
        return "doctor-appointments"; // Gọi trang `doctor-appointments.html`
    }
}
