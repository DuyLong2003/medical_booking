package com.medical.bookingapp.service;

import com.medical.bookingapp.entity.Doctor;
import com.medical.bookingapp.entity.Patient;
import com.medical.bookingapp.entity.User;
import com.medical.bookingapp.repository.DoctorRepository;
import com.medical.bookingapp.repository.PatientRepository;
import com.medical.bookingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public Integer getCurrentPatientId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Patient> patient = patientRepository.findByUserId(user.getId());
        if (patient.isEmpty()) {
            throw new RuntimeException("Patient record not found");
        }

        return patient.get().getId();
    }

    public Integer getCurrentDoctorId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getDoctor() == null) {
            throw new RuntimeException("Patient record not found");
        }

        return user.getDoctor().getId();
    }
}
