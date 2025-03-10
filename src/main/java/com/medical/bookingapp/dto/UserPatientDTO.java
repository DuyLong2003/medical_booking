package com.medical.bookingapp.dto;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserPatientDTO {
    private int id;              // userId
    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 6, message = "Tên đăng nhập phải có ít nhất 6 ký tự")
    private String username;

    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^[0-9]{10,15}$",
            message = "Số điện thoại phải có từ 10 đến 15 chữ số và chỉ chứa số")
    private String phone;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    // Thông tin Patient
    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @Positive(message = "Chiều cao phải lớn hơn 0")
    private Float height;

    @Positive(message = "Cân nặng phải lớn hơn 0")
    private Float weight;

    private String medicalHistory;
    private String role;

    private Integer gender;
    @PastOrPresent(message = "Ngày không được lớn hơn ngày hiện tại")
    private Date birthday;

    // getters, setters ...
}
