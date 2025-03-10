package com.medical.bookingapp.controller;

import com.medical.bookingapp.dto.AppointmentDTO;
import com.medical.bookingapp.dto.AppointmentRequestDTO;
import com.medical.bookingapp.dto.DoctorDTO;
import com.medical.bookingapp.dto.ServiceDTO;
import com.medical.bookingapp.entity.*;
import com.medical.bookingapp.repository.PatientRepository;
import com.medical.bookingapp.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/appointment")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final MyServiceService serviceService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final UserService userService;
    private final PatientRepository appointmentRepository;
    private final PatientRepository patientRepository;

    @GetMapping
    public String showAppointmentForm(Model model) {
        model.addAttribute("services", serviceService.getAllServices());
        return "appointment";
    }

    @GetMapping("/services")
    @ResponseBody
    public List<ServiceDTO> getAllServices() {
        return serviceService.getAllServicesDTO();
    }

    @GetMapping("/doctors/{serviceId}")
    @ResponseBody
    public List<DoctorDTO> getDoctorsByService(@PathVariable Integer serviceId) {
        return doctorService.getDoctorsByService(serviceId);
    }

    @GetMapping("/timeslots/{doctorId}/{date}")
    @ResponseBody
    public List<String> getAvailableTimes(@PathVariable Integer doctorId, @PathVariable String date) {
        LocalDate appointmentDate = LocalDate.parse(date);
        return appointmentService.getAvailableTimeSlots(doctorId, appointmentDate);
    }

    @PostMapping("/book")
    public String bookAppointment(@RequestParam Integer doctorId,
                                  @RequestParam String dateTime,
                                  @RequestParam String reason,
                                  Principal principal) {
        Patient patient = patientService.findByUsername(principal.getName());
        Doctor doctor = doctorService.findById(doctorId);

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(LocalDateTime.parse(dateTime));
        appointment.setReason(reason);
        appointment.setStatus("CONFIRMED");

        appointmentService.bookAppointment(appointment);
        return "redirect:/appointment/confirmation";
    }

    @PostMapping("/book-multiple")
    @ResponseBody
    public ResponseEntity<String> bookMultipleAppointments(@RequestBody AppointmentRequestDTO requests, Principal principal) {
        // Lấy User dựa trên username
        Optional<User> userOptional = userService.getUserByUsername(principal.getName());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: User not found!");
        }

        User user = userOptional.get();
        // Kiểm tra nếu user có patient
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: No patient found for this user!");
        }
        Patient patient = user.getPatient();

        if (patient == null) {
            patient = new Patient();
        }
        patient.setUser(user);
        patient.setAddress(requests.getUserInfo().getAddress());
        patient.setHeight(requests.getUserInfo().getHeight());
        patient.setWeight(requests.getUserInfo().getWeight());
        patient.setMedicalHistory(requests.getUserInfo().getMedicalHistory());
        patient.setGender(requests.getUserInfo().getGender());
        patient.setBirthday(requests.getUserInfo().getBirthday());
            user.setPatient(patient);
            patientRepository.save(patient);
        for (AppointmentDTO request : requests.getAppointments()) {
            Doctor doctor = doctorService.findById(request.getDoctorId());
            Service service = serviceService.findById(request.getServiceId()); // Lấy Service

            Appointment appointment = new Appointment();
            appointment.setPatient(patient);
            appointment.setDoctor(doctor);
            appointment.setService(service);
            appointment.setAppointmentDate(parseDateTimeRange(request.getAppointmentDateStr()));
            appointment.setReason(request.getReason());
            appointment.setNotes(request.getNotes());
            appointment.setStatus("CONFIRMED");

            appointmentService.bookAppointment(appointment);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Appointments booked success");
    }

    public static LocalDateTime parseDateTimeRange(String input) {
        try {
            // Tách ngày và khoảng thời gian
            String datePart = input.substring(0, 10); // "2025-02-21"
            String[] timeParts = input.substring(11).split(" - "); // ["07:00", "07:30"]

            // Định dạng parser
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            // Parse ngày
            LocalDate date = LocalDate.parse(datePart, dateFormatter);

            // Parse giờ bắt đầu và kết thúc
            LocalTime startTime = LocalTime.parse(timeParts[0], timeFormatter);
            LocalTime endTime = LocalTime.parse(timeParts[1], timeFormatter);

            // Kết hợp thành LocalDateTime
            LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
            LocalDateTime endDateTime = LocalDateTime.of(date, endTime);

            return startDateTime;

        } catch (Exception e) {
            System.err.println("Error parsing datetime: " + e.getMessage());
            return null;
        }
    }
}
