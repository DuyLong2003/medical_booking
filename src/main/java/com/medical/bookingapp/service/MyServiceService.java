package com.medical.bookingapp.service;

import com.medical.bookingapp.dto.ServiceDTO;
import com.medical.bookingapp.dto.UserDoctorDTO;
import com.medical.bookingapp.entity.Doctor;
import com.medical.bookingapp.entity.Service;
import com.medical.bookingapp.repository.DoctorRepository;
import com.medical.bookingapp.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class MyServiceService {
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    public List<ServiceDTO> getAllServicesDTO() {
        List<Service> list = serviceRepository.findAll();
        return list.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ServiceDTO toDTO(Service s) {
        ServiceDTO dto = new ServiceDTO();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setPrice(s.getPrice());
        dto.setDescription(s.getDescription());
        // không set doctors
        return dto;
    }

    public Service saveService(Service s) {
        return serviceRepository.save(s);
    }

    public void deleteService(Integer id) {
        serviceRepository.deleteById(id);
    }

    public Service findById(Integer id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
    }

    public List<UserDoctorDTO> getDoctorsByService(int serviceId) {
        // Tìm service
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        // Lấy set<Doctor> (nếu xài @ManyToMany)
        // Hoặc custom query tbl_doctor_service
        Set<Doctor> doctors = service.getDoctors();
        return doctors.stream().map(this::toDoctorDTO).collect(Collectors.toList());
    }
    private UserDoctorDTO toDoctorDTO(Doctor doc) {
        UserDoctorDTO dto = new UserDoctorDTO();
        dto.setId(doc.getId());
        dto.setFullName(doc.getUser().getFullName());
        return dto;
    }

    public List<UserDoctorDTO> updateDoctorsForService(int serviceId, List<Integer> doctorIds) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        // Xóa cũ
        service.getDoctors().clear();

        // Thêm mới
        for (Integer docId : doctorIds) {
            Doctor doctor = doctorRepository.findById(docId)
                    .orElseThrow(() -> new RuntimeException("Doctor not found, id=" + docId));
            service.getDoctors().add(doctor);
        }

        serviceRepository.save(service); // JPA update bridging table

        // Trả về danh sách doctors
        return service.getDoctors().stream().map(this::toDoctorDTO).collect(Collectors.toList());
    }

    public ServiceDTO getServiceById(int id) {
        Optional<Service> serviceOpt = serviceRepository.findById(id);
        return serviceOpt.map(this::convertToDTO).orElse(null);
    }

    private ServiceDTO convertToDTO(Service service) {
        ServiceDTO dto = new ServiceDTO();
        dto.setId(service.getId());
        dto.setName(service.getName());
        dto.setPrice(service.getPrice());
        dto.setDescription(service.getDescription());
        return dto;
    }

    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    public Service getServiceById(Integer id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found!"));
    }
}
