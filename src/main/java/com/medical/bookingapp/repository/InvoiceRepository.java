package com.medical.bookingapp.repository;

import com.medical.bookingapp.entity.Appointment;
import com.medical.bookingapp.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    boolean existsByAppointmentId(Integer appointmentId);
}
