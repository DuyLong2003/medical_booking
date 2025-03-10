package com.medical.bookingapp.repository;

import com.medical.bookingapp.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
     Optional<Patient> findByUserId(Integer userId);
     Optional<Patient> findByUser_Username(String username);
}
