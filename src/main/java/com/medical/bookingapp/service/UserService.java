package com.medical.bookingapp.service;

import com.medical.bookingapp.dto.UserDTO;
import com.medical.bookingapp.dto.UserDoctorDTO;
import com.medical.bookingapp.dto.UserPatientDTO;
import com.medical.bookingapp.entity.Doctor;
import com.medical.bookingapp.entity.Patient;
import com.medical.bookingapp.entity.User;
import com.medical.bookingapp.repository.DoctorRepository;
import com.medical.bookingapp.repository.PatientRepository;
import com.medical.bookingapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username đã tồn tại!");
        }


        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(userDTO.getRole());
        user.setFullName(userDTO.getFullName());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        if (userDTO.getRole() == null || userDTO.getRole().isEmpty()) {
            user.setRole("ROLE_CUSTOMER");
        } else {
            user.setRole(userDTO.getRole());
        }
        userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<UserDTO> getAllCustomers() {
        // Lấy tất cả user
        List<User> users = userRepository.findAll();
        // Lọc theo role=ROLE_CUSTOMER, map sang DTO để ẩn password
        return users.stream()
                .filter(u -> "ROLE_CUSTOMER".equals(u.getRole()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }



    public List<UserDTO> getAllUsers() {
        // Lấy tất cả user
        List<User> users = userRepository.findAll();
        // Lọc theo role=ROLE_CUSTOMER, map sang DTO để ẩn password
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getAllDoctors() {
        // Lấy tất cả user
        List<User> users = userRepository.findAll();
        // Lọc theo role=ROLE_CUSTOMER, map sang DTO để ẩn password
        return users.stream()
                .filter(u -> "ROLE_DOCTOR".equals(u.getRole()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        if (user.getRole() != null && user.getRole().equals("ROLE_CUSTOMER")) {
            dto.setRole("Khách hàng");
        }
        if (user.getRole() != null && user.getRole().equals("ROLE_ADMIN")) {
            dto.setRole("Quản lý");
        }
        if (user.getRole() != null && user.getRole().equals("ROLE_ASSISTANT")) {
            dto.setRole("Trợ lý");
        }
        if (user.getRole() != null && user.getRole().equals("ROLE_DOCTOR")) {
            dto.setRole("Bác sĩ");
        }
        return dto;
    }

    public void deleteUserById(int id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id=" + id);
        }
        userRepository.deleteById(id);
    }

    public UserDTO updateUser(int id, UserDTO dto) {
        // 1. Tìm user cũ
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id=" + id));
        if (!user.getUsername().equals(dto.getUsername()) && userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username đã tồn tại!");
        }


        if (!user.getEmail().equals(dto.getEmail()) && userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã được sử dụng!");
        }
        // 2. Cập nhật các trường
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        // password, role... tuỳ ý
        // user.setPassword(...) ?

        // 3. Lưu DB
        userRepository.save(user);

        // 4. Trả về UserDTO
        UserDTO updatedDto = new UserDTO();
        updatedDto.setId(user.getId());
        updatedDto.setUsername(user.getUsername());
        updatedDto.setFullName(user.getFullName());
        updatedDto.setPhone(user.getPhone());
        updatedDto.setEmail(user.getEmail());
        // ...
        return updatedDto;
    }

    public void changePassword(String username, String oldPassword, String newPassword) {
        // 1. Tìm user theo username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        // 2. Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không chính xác!");
        }

        // 3. Mã hoá mật khẩu mới & lưu DB
        String hashedNew = passwordEncoder.encode(newPassword);
        user.setPassword(hashedNew);
        userRepository.save(user);
    }

    public UserDTO updateUserRole(int id, String newRole) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id=" + id));

        user.setRole(newRole);
        userRepository.save(user);

        // Trả về DTO
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole()); // Mới cập nhật
        return dto;
    }

    public List<UserPatientDTO> getAllCustomersWithPatient() {
        // 1. Lấy tất cả user có ROLE_CUSTOMER
        List<User> customerUsers = userRepository.findByRole("ROLE_CUSTOMER");

        // 2. Tạo danh sách DTO
        List<UserPatientDTO> result = new ArrayList<>();

        for (User user : customerUsers) {
            UserPatientDTO dto = new UserPatientDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setFullName(user.getFullName());
            dto.setPhone(user.getPhone());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole());
            Patient patient = patientRepository.findByUserId(user.getId()).orElse(null);

            if (patient != null) {
                dto.setAddress(patient.getAddress());
                dto.setHeight(patient.getHeight());
                dto.setWeight(patient.getWeight());
                dto.setMedicalHistory(patient.getMedicalHistory());
                dto.setGender(patient.getGender());
                dto.setBirthday(patient.getBirthday());
            }

            result.add(dto);
        }

        return result;
    }

    public UserPatientDTO updateCustomer(int userId, UserPatientDTO dto) {
        // Tìm user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found, id=" + userId));

        if (!user.getUsername().equals(dto.getUsername()) && userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username đã tồn tại!");
        }


        if (!user.getEmail().equals(dto.getEmail()) && userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        // Cập nhật user
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        userRepository.save(user);

        // Nếu role = ROLE_CUSTOMER -> cập nhật tbl_patient
        if ("ROLE_CUSTOMER".equals(user.getRole())) {
            // Tìm patient hoặc tạo mới nếu chưa có
            Patient patient = patientRepository.findByUserId(userId)
                    .orElse(new Patient());

            // Nếu là patient mới, cần gán user
            if (patient.getId() == null) {
                patient.setUser(user);
            }

            patient.setAddress(dto.getAddress());
            patient.setHeight(dto.getHeight());
            patient.setWeight(dto.getWeight());
            patient.setMedicalHistory(dto.getMedicalHistory());
            patient.setGender(dto.getGender());
            patient.setBirthday(dto.getBirthday());
            patientRepository.save(patient);
        }

        // Trả về DTO
        // Lấy user + patient -> gộp vào UserPatientDTO
        return toUserPatientDTO(user);
    }

    private UserPatientDTO toUserPatientDTO(User user) {
        UserPatientDTO dto = new UserPatientDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());

        if ("ROLE_CUSTOMER".equals(user.getRole())) {
            // Lấy patient
            Patient p = patientRepository.findByUserId(user.getId()).orElse(null);
            if (p != null) {
                dto.setAddress(p.getAddress());
                dto.setHeight(p.getHeight());
                dto.setWeight(p.getWeight());
                dto.setMedicalHistory(p.getMedicalHistory());
            }
        }
        return dto;
    }

    public List<UserDoctorDTO> getAllDoctorsWithInfo() {
        // 1. Lấy user có role=ROLE_DOCTOR
        List<User> doctors = userRepository.findByRole("ROLE_DOCTOR");
        List<UserDoctorDTO> result = new ArrayList<>();

        for (User user : doctors) {
            UserDoctorDTO dto = new UserDoctorDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setFullName(user.getFullName());
            dto.setPhone(user.getPhone());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole());

            // Tìm doctor
            Doctor doctor = doctorRepository.findByUserId(user.getId()).orElse(null);
            if (doctor != null) {
                dto.setSpecialty(doctor.getSpecialty());
                dto.setQualification(doctor.getQualification());
                dto.setExperienceYears(doctor.getExperienceYears());
                dto.setConsultationFee(doctor.getConsultationFee());
                dto.setMultiplier(doctor.getMultiplier());
                dto.setDescription(doctor.getDescription());
                dto.setImage(doctor.getImage());
            }
            result.add(dto);
        }
        return result;
    }

    public List<UserDoctorDTO> getDoctorsWithInfo() {
        // 1. Lấy user có role=ROLE_DOCTOR
        List<User> doctors = userRepository.findByRole("ROLE_DOCTOR");
        List<UserDoctorDTO> result = new ArrayList<>();

        for (User user : doctors) {
            UserDoctorDTO dto = new UserDoctorDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setFullName(user.getFullName());
            dto.setPhone(user.getPhone());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole());

            // Tìm doctor
            Doctor doctor = doctorRepository.findByUserId(user.getId()).orElse(null);
            if (doctor != null) {
                dto.setDoctorId(doctor.getId());
                dto.setSpecialty(doctor.getSpecialty());
                dto.setQualification(doctor.getQualification());
                dto.setExperienceYears(doctor.getExperienceYears());
                dto.setConsultationFee(doctor.getConsultationFee());
                dto.setMultiplier(doctor.getMultiplier());
                dto.setDescription(doctor.getDescription());
                dto.setImage(doctor.getImage());
                result.add(dto);
            }
        }
        return result;
    }

    public UserDoctorDTO updateDoctor(int userId, UserDoctorDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getUsername().equals(dto.getUsername()) && userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username đã tồn tại!");
        }


        if (!user.getEmail().equals(dto.getEmail()) && userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã được sử dụng!");
        }
        // update user
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        userRepository.save(user);

        // doctor
        if ("ROLE_DOCTOR".equals(user.getRole())) {
            Doctor doctor = doctorRepository.findByUserId(userId).orElse(new Doctor());
            if (doctor.getId() == null) {
                doctor.setUser(user);
            }
            doctor.setSpecialty(dto.getSpecialty());
            doctor.setQualification(dto.getQualification());
            doctor.setExperienceYears(dto.getExperienceYears());
            doctor.setConsultationFee(dto.getConsultationFee());
            doctor.setMultiplier(dto.getMultiplier());
            doctor.setDescription(dto.getDescription());

            // nếu dto.getImage() != null => cập nhật
            if (dto.getImage() != null) {
                doctor.setImage(dto.getImage());
            }

            doctorRepository.save(doctor);
        }

        return toUserDoctorDTO(user);
    }


    private UserDoctorDTO toUserDoctorDTO(User user) {
        UserDoctorDTO dto = new UserDoctorDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());

        if ("ROLE_DOCTOR".equals(user.getRole())) {
            Doctor doctor = doctorRepository.findByUserId(user.getId()).orElse(null);
            if (doctor != null) {
                dto.setSpecialty(doctor.getSpecialty());
                dto.setQualification(doctor.getQualification());
                dto.setExperienceYears(doctor.getExperienceYears());
                dto.setConsultationFee(doctor.getConsultationFee());
                dto.setMultiplier(doctor.getMultiplier());
                dto.setDescription(doctor.getDescription());
                dto.setImage(doctor.getImage());
            }
        }
        return dto;
    }

    public UserPatientDTO getCurrentUserProfile() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());

        return user.map(u -> new UserPatientDTO(
                u.getId(),
                u.getUsername(),
                u.getFullName(),
                u.getPhone(),
                u.getEmail(),
                u.getPatient().getAddress(),
                u.getPatient().getHeight(),
                u.getPatient().getWeight(),
                u.getPatient().getMedicalHistory(),
                u.getRole(),
                u.getPatient().getGender(),
                u.getPatient().getBirthday()
        )).orElseThrow(() -> new RuntimeException("User not found"));
    }
}