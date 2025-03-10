package com.medical.bookingapp.controller;

import com.medical.bookingapp.dto.*;
import com.medical.bookingapp.entity.Appointment;
import com.medical.bookingapp.service.AppointmentService;
import com.medical.bookingapp.service.DoctorService;
import com.medical.bookingapp.service.MyServiceService;
import com.medical.bookingapp.service.UserService;
import com.medical.bookingapp.validation.UpdateGroup;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")  // Hoặc "/admin/api", tùy ý
public class AdminUserRestController {
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private UserService userService;

    @Autowired
    private MyServiceService myServiceService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private Validator validator;

    // Xóa user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok().build(); // 200 OK
        } catch (RuntimeException e) {
            // Nếu user không tồn tại, v.v.
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // Cập nhật user (PUT /api/admin/users/{id})
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable("id") int id,
            @Validated(UpdateGroup.class) @RequestBody UserDTO userDto,
            BindingResult bindingResult
    ) {
        try {
            if (bindingResult.hasErrors()) {
                // Có lỗi validate
                // Trả về 400 + danh sách lỗi
                List<String> errors = bindingResult.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());

                return ResponseEntity.badRequest().body(errors);
            }
            // Gọi service cập nhật
            UserDTO updated = userService.updateUser(id, userDto);
            return ResponseEntity.ok(updated); // Trả về JSON userDTO
        } catch (RuntimeException e) {
            // Nếu user không tồn tại hoặc lỗi gì khác
            return ResponseEntity.badRequest().body(new String[]{e.getMessage()});
        }
    }

    @PutMapping("/users/customer/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") int id,
                                                     @Valid @RequestBody UserPatientDTO dto,
                                                     BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                // Có lỗi validate
                // Trả về 400 + danh sách lỗi
                List<String> errors = bindingResult.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());

                return ResponseEntity.badRequest().body(errors);
            }
            UserPatientDTO updated = userService.updateCustomer(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new String[]{e.getMessage()});
        }
    }

    @PutMapping(value = "/users/doctor/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateDoctor(
            @PathVariable("id") int id,
            @RequestParam("username") String username,
            @RequestParam("fullName") String fullName,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("specialty") String specialty,
            @RequestParam("qualification") String qualification,
            @RequestParam("experienceYears") Integer experienceYears,
            @RequestParam("consultationFee") BigDecimal consultationFee,
            @RequestParam("multiplier") BigDecimal multiplier,
            @RequestParam("description") String description,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        try {
            // 1. Lưu file (nếu có)
            String imagePath = null;
            if (imageFile != null && !imageFile.isEmpty()) {
                // Lưu file vào thư mục uploads
                // Tùy ý bạn, ví dụ /var/www/uploads/...
                File dir = new File(this.uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                File dest = new File(dir, fileName);
                imageFile.transferTo(dest);

                // Lưu path
                imagePath = "/uploads/" + fileName; // path trả về
            }

            // 2. Gọi service update
            UserDoctorDTO dto = new UserDoctorDTO();
            dto.setId(id);
            dto.setUsername(username);
            dto.setFullName(fullName);
            dto.setPhone(phone);
            dto.setEmail(email);
            dto.setSpecialty(specialty);
            dto.setQualification(qualification);
            dto.setExperienceYears(experienceYears);
            dto.setConsultationFee(consultationFee);
            dto.setMultiplier(multiplier);
            dto.setDescription(description);
            dto.setImage(imagePath); // set path nếu có

            Set<ConstraintViolation<UserDoctorDTO>> violations = validator.validate(dto, UpdateGroup.class);
            if (!violations.isEmpty()) {
                // Có lỗi => trả về 400 + danh sách lỗi
                List<String> errors = violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }


            UserDoctorDTO updated = userService.updateDoctor(id, dto);

            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new String[]{e.getMessage()});
        }
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<UserDTO> updateUserRole(
            @PathVariable("id") int id,
            @RequestBody Map<String, String> payload
    ) {
        // Lấy "role" từ JSON
        String newRole = payload.get("role");
        try {
            UserDTO updated = userService.updateUserRole(id, newRole);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // hoặc 400 Bad Request
        }
    }

    @GetMapping("/services/{serviceId}/doctors")
    public List<UserDoctorDTO> getDoctorsOfService(@PathVariable int serviceId) {
        return myServiceService.getDoctorsByService(serviceId);
    }

    // Cập nhật danh sách doctor cho service
    @PutMapping("/services/{serviceId}/doctors")
    public List<UserDoctorDTO> updateDoctorsForService(
            @PathVariable int serviceId,
            @RequestBody List<Integer> doctorIds
    ) {
        return myServiceService.updateDoctorsForService(serviceId, doctorIds);
    }

    // GET /api/admin/doctors
    @GetMapping("/doctors")
    public ResponseEntity<List<UserDoctorDTO>> getAllDoctors() {
        List<UserDoctorDTO> list = doctorService.getAllDoctors();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @PutMapping("/appointments/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(
            @PathVariable int id,
            @RequestBody AppointmentDTO updatedAppointment) throws Exception {
        return ResponseEntity.ok(appointmentService.updateAppointment(id, updatedAppointment));
    }
}
