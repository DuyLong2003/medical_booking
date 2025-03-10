package com.medical.bookingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class InvoiceDTO {
    private Integer patientId;
    private String patientName;
    private LocalDate date;
    private String services;
    private BigDecimal totalAmount;
    private boolean hasInvoice;
}
