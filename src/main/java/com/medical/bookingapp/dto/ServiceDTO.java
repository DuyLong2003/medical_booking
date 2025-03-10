package com.medical.bookingapp.dto;

import java.math.BigDecimal;
import lombok.*;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {
    private Integer id;
    private String name;
    private BigDecimal price;
    private String description;
    // Không có 'doctors'
}
