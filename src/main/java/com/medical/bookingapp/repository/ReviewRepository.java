package com.medical.bookingapp.repository;

import com.medical.bookingapp.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    boolean existsByAppointmentId(Integer appointmentId);
}