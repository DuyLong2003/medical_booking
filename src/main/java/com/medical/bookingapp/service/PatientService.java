package com.medical.bookingapp.service;

import com.medical.bookingapp.entity.Patient;
import com.medical.bookingapp.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

    public Patient findByUsername(String username) {
        return patientRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Patient not found!"));
    }
}
