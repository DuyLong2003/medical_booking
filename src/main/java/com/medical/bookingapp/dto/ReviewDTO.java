package com.medical.bookingapp.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Integer appointmentId;
    private Integer rate;
    private String comment;
}
