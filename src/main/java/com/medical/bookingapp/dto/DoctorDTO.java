package com.medical.bookingapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class DoctorDTO {
    private Integer id;           // ID của doctor
    private String fullName;
    private String specialty;
    private String image;
    private String description;   // tùy chọn, nếu bạn muốn mô tả bác sĩ

    // Danh sách dịch vụ bác sĩ cung cấp (nếu muốn hiển thị)
    private List<ServiceDTO> services;
}
