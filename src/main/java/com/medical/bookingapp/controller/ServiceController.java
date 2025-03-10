package com.medical.bookingapp.controller;

import com.medical.bookingapp.dto.ServiceDTO;
import com.medical.bookingapp.dto.UserDoctorDTO;
import com.medical.bookingapp.service.MyServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ServiceController {

    @Autowired
    private MyServiceService serviceService;

    @GetMapping("/service/{id}")
    public String serviceInfo(@PathVariable("id") int id, Model model) {
        // Lấy thông tin dịch vụ
        ServiceDTO service = serviceService.getServiceById(id);
        if (service == null) {
            return "redirect:/404";
        }

        // Lấy danh sách bác sĩ cung cấp dịch vụ này
        List<UserDoctorDTO> doctors = serviceService.getDoctorsByService(id);

        model.addAttribute("service", service);
        model.addAttribute("doctors", doctors);
        return "service-info"; // Render Thymeleaf template
    }
}
