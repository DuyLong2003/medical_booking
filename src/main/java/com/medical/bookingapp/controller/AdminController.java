package com.medical.bookingapp.controller;

import com.medical.bookingapp.common.Constant;
import com.medical.bookingapp.dto.*;
import com.medical.bookingapp.entity.Service;
import com.medical.bookingapp.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping(Constant.ADMIN)
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private MyServiceService myServiceService;
    @Autowired
    private LeaveService leaveService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private InvoiceService invoiceService;
    @GetMapping(Constant.HOME)
    public String adminHome() {
        return Constant.VIEW_ADMIN_HOME;
    }

    @GetMapping(Constant.USERS)
    public String showUsers(Model model) {
        List<UserDTO> customers = userService.getAllUsers();
        model.addAttribute("customers", customers);
        return Constant.VIEW_ADMIN_USERS;
    }

    @GetMapping(Constant.CUSTOMERS)
    public String showCustomer(Model model) {
        List<UserPatientDTO> customers = userService.getAllCustomersWithPatient();
        model.addAttribute("customers", customers);
        return Constant.VIEW_ADMIN_CUSTOMERS;
    }

    @GetMapping("/doctors")
    public String showDoctors(Model model) {
        List<UserDoctorDTO> doctorList = userService.getAllDoctorsWithInfo();
        model.addAttribute("customers", doctorList);
        return "admin-doctors";
    }


    @GetMapping("/services")
    public String showServices(Model model) {
        try {
            List<ServiceDTO> services = myServiceService.getAllServicesDTO();
            model.addAttribute("services", services);
        } catch (Exception e) {
            // log e
            throw e;
        }
        return "admin-services";  // Tên template
    }

    // API xóa (nếu muốn tách hẳn ra RestController cũng được)
    @DeleteMapping("/services/{id}")
    @ResponseBody
    public void deleteService(@PathVariable("id") Integer id) {
        myServiceService.deleteService(id);
    }

    // API thêm/sửa (dùng POST hay PUT tùy ý)
    @PostMapping("/services")
    @ResponseBody
    public Service createService(@RequestBody Service s) {
        return myServiceService.saveService(s);
    }

    @PutMapping("/services/{id}")
    @ResponseBody
    public Service updateService(@PathVariable("id") Integer id, @RequestBody Service s) {
        // Lấy service cũ
        Service old = myServiceService.findById(id);
        old.setName(s.getName());
        old.setPrice(s.getPrice());
        old.setDescription(s.getDescription());
        return myServiceService.saveService(old);
    }

    @GetMapping("/leave-request")
    public String getAllLeaves(Model model) {
        model.addAttribute("leaves", leaveService.getAllLeaves());
        return "admin-leave";  // Tên template
    }

    @GetMapping("/appointments")
    public String getAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getAllAppointments());
        return "admin-appointment";  // Tên template
    }

    @PostMapping("/leaves")
    public ResponseEntity<LeaveDTO> createLeave(@RequestBody LeaveDTO dto) {
        // Gọi service
        LeaveDTO created = leaveService.createLeave(dto);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/leaves/{id}")
    public ResponseEntity<?> deleteLeave(@PathVariable int id) {
        leaveService.deleteLeave(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/change-password")
    public String showChangePasswordForm(
            @RequestParam(value = "redirect", required = false) String redirect,
            Model model
    ) {
        // Lưu redirect vào model để Thymeleaf hiển thị trong hidden input
        model.addAttribute("redirect", redirect);
        return "admin-change-password";
    }

    @GetMapping("/invoices")
    public String viewInvoices(Model model) {
        List<InvoiceDTO> invoices = invoiceService.getCompletedAppointmentsGrouped();
        model.addAttribute("invoices", invoices);
        return "admin-invoices";
    }

    @PostMapping("/invoices/generate")
    @ResponseBody
    public String generateInvoice(@RequestParam Integer patientId, @RequestParam String date) {
        invoiceService.generateInvoice(patientId, LocalDate.parse(date));
        return "Invoice generated successfully";
    }

}
