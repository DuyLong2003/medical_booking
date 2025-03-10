package com.medical.bookingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class AppointmentSummaryDTO {
    private Integer patientId;
    private String patientName;
    private LocalDate date;
    private String services;
    private BigDecimal totalAmount;
    private int invoiceCount;
}
