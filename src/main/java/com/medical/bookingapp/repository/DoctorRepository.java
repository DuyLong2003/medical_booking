package com.medical.bookingapp.repository;

import com.medical.bookingapp.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    Optional<Doctor> findByUserId(Integer userId);
    List<Doctor> findByServices_Id(int serviceId);
    List<Doctor> findBySpecialtyContainingIgnoreCase(String specialty);
    List<Doctor> findByServices_Id(Integer serviceId);
}
