package com.medical.bookingapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_service")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private BigDecimal price;
    private String description;
    @ManyToMany
    @JoinTable(name = "tbl_doctor_service",
            joinColumns = @JoinColumn(name = "service_id"),
            inverseJoinColumns = @JoinColumn(name = "doctor_id")
    )    private Set<Doctor> doctors = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "tbl_appoiment",
            joinColumns = @JoinColumn(name = "service_id")
    )    private Set<Doctor> appointments = new HashSet<>();
}
