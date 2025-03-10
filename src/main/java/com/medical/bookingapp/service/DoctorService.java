package com.medical.bookingapp.service;

import com.medical.bookingapp.dto.DoctorDTO;
import com.medical.bookingapp.dto.ServiceDTO;
import com.medical.bookingapp.dto.UserDoctorDTO;
import com.medical.bookingapp.entity.Doctor;
import com.medical.bookingapp.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    // Lấy tất cả doctor, chuyển sang DoctorDTO
    public List<UserDoctorDTO> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream()
                .map(this::toDoctorDTO)
                .collect(Collectors.toList());
    }

    private UserDoctorDTO toDoctorDTO(Doctor doc) {
        UserDoctorDTO dto = new UserDoctorDTO();
        dto.setId(doc.getId());
        dto.setFullName(doc.getUser().getFullName());
        dto.setPhone(doc.getUser().getPhone());
        dto.setEmail(doc.getUser().getEmail());
        dto.setSpecialty(doc.getSpecialty());
        dto.setQualification(doc.getQualification());
        dto.setExperienceYears(doc.getExperienceYears());
        dto.setConsultationFee(doc.getConsultationFee());
        dto.setMultiplier(doc.getMultiplier());
        dto.setDescription(doc.getDescription());
        dto.setImage(doc.getImage());
        // Nếu doctor có user, set username, role...
        // dto.setUsername(doc.getUser().getUsername());
        return dto;
    }

    public DoctorDTO getDoctorWithServices(int id) {
        Doctor doctorEntity = doctorRepository.findById(id).orElse(null);
        if (doctorEntity == null) return null;

        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctorEntity.getId());
        dto.setFullName(doctorEntity.getUser().getFullName());
        dto.setSpecialty(doctorEntity.getSpecialty());
        dto.setImage(doctorEntity.getImage());
        dto.setDescription(doctorEntity.getDescription());

        // Nếu Doctor có quan hệ ManyToMany với Service
        List<ServiceDTO> serviceList = new ArrayList<>();
        for (com.medical.bookingapp.entity.Service s : doctorEntity.getServices()) {
            ServiceDTO sdto = new ServiceDTO();
            sdto.setId(s.getId());
            sdto.setName(s.getName());
            sdto.setPrice(s.getPrice());
            sdto.setDescription(s.getDescription());
            serviceList.add(sdto);
        }
        dto.setServices(serviceList);

        return dto;
    }

    public List<DoctorDTO> getDoctorsByService(Integer serviceId) {
        List<Doctor> doctorEntity = doctorRepository.findByServices_Id(serviceId);
        List<DoctorDTO> lstDoctorDTO = new ArrayList<>();
        for (Doctor doctorEntity1 : doctorEntity) {
            DoctorDTO dto = new DoctorDTO();
            dto.setId(doctorEntity1.getId());
            dto.setFullName(doctorEntity1.getUser().getFullName());
            dto.setSpecialty(doctorEntity1.getSpecialty());
            dto.setImage(doctorEntity1.getImage());
            dto.setDescription(doctorEntity1.getDescription());
            lstDoctorDTO.add(dto);
        }


        return lstDoctorDTO;
    }

    public Doctor findById(Integer doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found!"));
    }
}
