package com.medical.bookingapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "tbl_patient")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String address;
    private Float height;
    private Float weight;
    private Integer gender;
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Column(name = "medical_history")
    private String medicalHistory;
}
