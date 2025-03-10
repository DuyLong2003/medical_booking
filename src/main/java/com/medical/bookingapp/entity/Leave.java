package com.medical.bookingapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_leave")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Nhiều leave -> 1 doctor
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Doctor doctor;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startTime;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endTime;
}
