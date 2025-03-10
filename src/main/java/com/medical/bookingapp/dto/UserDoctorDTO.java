package com.medical.bookingapp.dto;

import com.medical.bookingapp.validation.CreateGroup;
import com.medical.bookingapp.validation.UpdateGroup;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class UserDoctorDTO {
    private Integer id;
    private Integer doctorId;
    @NotBlank(message = "Tên đăng nhập không được để trống",
            groups = {CreateGroup.class, UpdateGroup.class})
    @Size(min = 6, message = "Tên đăng nhập phải có ít nhất 6 ký tự",
            groups = {CreateGroup.class, UpdateGroup.class})
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống",
            groups = {CreateGroup.class})
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự",
            groups = {CreateGroup.class})
    private String password;

    private String role;

    @NotBlank(message = "Họ và tên không được để trống",
            groups = {CreateGroup.class, UpdateGroup.class})
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống",
            groups = {CreateGroup.class, UpdateGroup.class})
    @Pattern(regexp = "^[0-9]{10,15}$",
            message = "Số điện thoại phải có từ 10 đến 15 chữ số và chỉ chứa số",
            groups = {CreateGroup.class, UpdateGroup.class})
    private String phone;

    @NotBlank(message = "Email không được để trống",
            groups = {CreateGroup.class, UpdateGroup.class})
    @Email(message = "Email không hợp lệ",
            groups = {CreateGroup.class, UpdateGroup.class})
    private String email;

    // Thông tin Doctor
    private String specialty;
    private String qualification;
    @Positive(message = "Số năm kinh nghiệm phải > 0")
    private Integer experienceYears;
    @DecimalMin(value = "0.0", inclusive = false, message = "Phí tư vấn phải > 0")
    private BigDecimal consultationFee;
    @DecimalMin(value = "0.0", inclusive = false, message = "Hệ số multiplier phải > 0")
    private BigDecimal multiplier;
    private String description;
    private String image;
}
