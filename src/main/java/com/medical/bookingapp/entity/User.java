package com.medical.bookingapp.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "tbl_user")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // ROLE_ADMIN, ROLE_DOCTOR, ROLE_CUSTOMER

    @Column(name = "full_name")
    private String fullName;

    private String phone;
    private String email;
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt = new Date();
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = new Date();
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Patient patient;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Doctor doctor;
}
