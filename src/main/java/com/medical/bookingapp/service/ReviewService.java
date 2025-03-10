package com.medical.bookingapp.service;

import com.medical.bookingapp.entity.Appointment;
import com.medical.bookingapp.entity.Patient;
import com.medical.bookingapp.entity.Review;
import com.medical.bookingapp.repository.AppointmentRepository;
import com.medical.bookingapp.repository.PatientRepository;
import com.medical.bookingapp.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    public void submitReview(Integer appointmentId, Integer patientId, Integer rate, String comment) {
        if (reviewRepository.existsByAppointmentId(appointmentId)) {
            throw new RuntimeException("Review already exists!");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Review review = new Review();
        review.setAppointment(appointment);
        review.setPatient(patient);
        review.setRate(rate);
        review.setComment(comment);
        reviewRepository.save(review);
    }
}
