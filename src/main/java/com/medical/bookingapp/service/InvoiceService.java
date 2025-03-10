package com.medical.bookingapp.service;

import com.medical.bookingapp.dto.InvoiceDTO;
import com.medical.bookingapp.entity.Appointment;
import com.medical.bookingapp.entity.Invoice;
import com.medical.bookingapp.repository.AppointmentRepository;
import com.medical.bookingapp.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final AppointmentRepository appointmentRepository;

    // Lấy danh sách appointment đã hoàn thành và nhóm theo ngày + bệnh nhân
    public List<InvoiceDTO> getCompletedAppointmentsGrouped() {
        List<Appointment> completedAppointments = appointmentRepository.findByStatus("COMPLETED");

        return completedAppointments.stream()
                .collect(Collectors.groupingBy(
                        appt -> appt.getPatient().getId() + "-" + appt.getAppointmentDate().toLocalDate(),
                        Collectors.toList()))
                .values().stream()
                .map(appointments -> {
                    Appointment firstAppt = appointments.get(0);

                    // ✅ Tính total amount = price của service * multiplier của doctor
                    BigDecimal totalAmount = appointments.stream()
                            .map(appt -> appt.getService().getPrice().multiply(appt.getDoctor().getMultiplier()))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    boolean hasInvoice = invoiceRepository.existsByAppointmentId(firstAppt.getId());

                    return new InvoiceDTO(
                            firstAppt.getPatient().getId(),
                            firstAppt.getPatient().getUser().getFullName(),
                            firstAppt.getAppointmentDate().toLocalDate(),
                            appointments.stream()
                                    .map(appt -> appt.getService().getName())
                                    .collect(Collectors.joining(", ")),
                            totalAmount,
                            hasInvoice
                    );
                }).collect(Collectors.toList());
    }


    // Tạo Invoice dựa trên nhóm appointment theo ngày và bệnh nhân
    public void generateInvoice(Integer patientId, LocalDate date) {
        List<Appointment> appointments = appointmentRepository
                .findByPatientIdAndAppointmentDateBetween(patientId, date.atStartOfDay(), date.atTime(23, 59));

        if (appointments.isEmpty()) {
            throw new RuntimeException("No completed appointments found.");
        }

        // ✅ Tính đúng tổng tiền với multiplier
        BigDecimal totalAmount = appointments.stream()
                .map(appt -> appt.getService().getPrice().multiply(appt.getDoctor().getMultiplier()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Invoice invoice = new Invoice();
        invoice.setAppointment(appointments.get(0)); // Chọn 1 appointment đại diện
        invoice.setTotalAmount(totalAmount);
        invoice.setIssuedAt(LocalDate.now().atStartOfDay());
        invoiceRepository.save(invoice);
    }

}
