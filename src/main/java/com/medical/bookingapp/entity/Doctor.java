package com.medical.bookingapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tbl_doctor")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String specialty;
    private String qualification;
    @Column(name = "experience_years")
    private Integer experienceYears;
    @Column(name = "consultation_fee")
    private BigDecimal consultationFee;
    private BigDecimal multiplier;
    private String description;
    private String image;

    @ManyToMany(mappedBy = "doctors")
    private Set<Service> services = new HashSet<>();

    @OneToMany(mappedBy = "doctor")
    private List<Leave> leaves = new ArrayList<>();

}
