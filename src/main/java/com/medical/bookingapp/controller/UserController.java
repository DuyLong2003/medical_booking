package com.medical.bookingapp.controller;

import com.medical.bookingapp.dto.UserPatientDTO;
import com.medical.bookingapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserPatientDTO> getUserProfile() {
        return ResponseEntity.ok(userService.getCurrentUserProfile());
    }
}
