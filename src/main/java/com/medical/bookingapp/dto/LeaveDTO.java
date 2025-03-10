package com.medical.bookingapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaveDTO {
    private Integer id;
    private Integer doctorId;
    private String doctorName; // hiển thị
    private String startTime;  // 'YYYY-MM-DDTHH:mm'
    private String endTime;
}
