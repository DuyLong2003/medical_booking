package com.medical.bookingapp.controller;

import com.medical.bookingapp.dto.DoctorDTO;
import com.medical.bookingapp.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/doctor")
public class DoctorInfoController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/{id}")
    public String showDoctorInfo(@PathVariable int id, Model model) {
        // Lấy thông tin doctor
        // Giả sử doctorService.getDoctorWithServices(id) trả về
        // 1 object DoctorDTO (hoặc entity) + set<ServiceDTO>
        DoctorDTO doctor = doctorService.getDoctorWithServices(id);
        if (doctor == null) {
            return "redirect:/404"; // hoặc 404
        }

        model.addAttribute("doctor", doctor);
        return "doctor-info"; // Tên template Thymeleaf
    }
}
