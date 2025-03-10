package com.medical.bookingapp.controller;

import com.medical.bookingapp.dto.ReviewDTO;
import com.medical.bookingapp.service.AuthenticationService;
import com.medical.bookingapp.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final AuthenticationService authService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitReview(@RequestBody ReviewDTO reviewDTO) {
        Integer patientId = authService.getCurrentPatientId();
        reviewService.submitReview(reviewDTO.getAppointmentId(), patientId, reviewDTO.getRate(), reviewDTO.getComment());
        return ResponseEntity.ok("Review submitted successfully!");
    }
}
