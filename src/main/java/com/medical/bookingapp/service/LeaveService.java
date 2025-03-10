package com.medical.bookingapp.service;

import com.medical.bookingapp.dto.LeaveDTO;
import com.medical.bookingapp.entity.Doctor;
import com.medical.bookingapp.entity.Leave;
import com.medical.bookingapp.repository.DoctorRepository;
import com.medical.bookingapp.repository.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveService {
    @Autowired
    private LeaveRepository leaveRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    public List<LeaveDTO> getAllLeaves() {
        List<Leave> leaves = leaveRepository.findAll();
        return leaves.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public LeaveDTO createLeave(LeaveDTO dto) {
        Doctor doc = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Leave leave = new Leave();
        leave.setDoctor(doc);
        leave.setStartTime(LocalDateTime.parse(dto.getStartTime())); // parse '2025-02-20T08:00'
        leave.setEndTime(LocalDateTime.parse(dto.getEndTime()));
        leaveRepository.save(leave);

        return toDTO(leave);
    }

    public void deleteLeave(int id) {
        leaveRepository.deleteById(id);
    }

    private LeaveDTO toDTO(Leave leave) {
        LeaveDTO dto = new LeaveDTO();
        dto.setId(leave.getId());
        dto.setDoctorId(leave.getDoctor().getId());
        dto.setDoctorName(leave.getDoctor().getUser().getFullName()); // Tuỳ logic
        dto.setStartTime(leave.getStartTime().toString());
        dto.setEndTime(leave.getEndTime().toString());
        return dto;
    }
}
