package com.medical.bookingapp.controller;

import com.medical.bookingapp.dto.AppointmentDTO;
import com.medical.bookingapp.dto.ServiceDTO;
import com.medical.bookingapp.dto.UserDoctorDTO;
import com.medical.bookingapp.service.AppointmentService;
import com.medical.bookingapp.service.AuthenticationService;
import com.medical.bookingapp.service.MyServiceService;
import com.medical.bookingapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private MyServiceService myServiceService;
    @Autowired
    private UserService userService;
    @Autowired
    private  AppointmentService appointmentService;
    @Autowired
    private  AuthenticationService authService;

    @GetMapping("/")
    public String index(Model model) {
        List<ServiceDTO> services = myServiceService.getAllServicesDTO();
        model.addAttribute("services", services);
        List<UserDoctorDTO> doctorList = userService.getDoctorsWithInfo();
        model.addAttribute("doctors", doctorList);
        return "index"; // Tên template Thymeleaf: index.html
    }

    // Hoặc /index
    @GetMapping("/index")
    public String indexAlias(Model model) {
        List<ServiceDTO> services = myServiceService.getAllServicesDTO();
        model.addAttribute("services", services);
        List<UserDoctorDTO> doctorList = userService.getDoctorsWithInfo();
        model.addAttribute("doctors", doctorList);
        return "index";
    }

    @GetMapping("/404")
    public String showPageNotFound() {
        return "404";
    }

    @GetMapping("/error")
    public String showPageError() {
        return "error";
    }

    @GetMapping("/appointment-list")
    public String appointmentList(Model model) {
        Integer patientId = authService.getCurrentPatientId();
        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByPatientId(patientId);

        model.addAttribute("appointments", appointments);
        return "appointment-list"; // Trả về trang Thymeleaf
    }
}
