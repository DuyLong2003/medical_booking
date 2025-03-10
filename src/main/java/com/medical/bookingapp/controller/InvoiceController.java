package com.medical.bookingapp.controller;

import com.medical.bookingapp.dto.InvoiceDTO;
import com.medical.bookingapp.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/invoices")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;

    @GetMapping("/invoices")
    public String viewInvoices(Model model) {
        List<InvoiceDTO> invoices = invoiceService.getCompletedAppointmentsGrouped();
        model.addAttribute("invoices", invoices);
        return "admin-invoices";
    }

    @PostMapping("/invoices/generate")
    @ResponseBody
    public String generateInvoice(@RequestParam Integer patientId, @RequestParam String date) {
        invoiceService.generateInvoice(patientId, LocalDate.parse(date));
        return "Invoice generated successfully";
    }
}
