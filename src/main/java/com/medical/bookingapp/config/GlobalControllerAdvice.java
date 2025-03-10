package com.medical.bookingapp.config;

import com.medical.bookingapp.entity.User;
import com.medical.bookingapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

import java.util.Optional;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UserService userService; // service tự định nghĩa

    @ModelAttribute
    public void addUserInfoToModel(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Optional<User> userEntityOpt = userService.getUserByUsername(username);
            if (userEntityOpt.isPresent()) {
                User userEntity = userEntityOpt.get();
                model.addAttribute("username", userEntity.getUsername());
                model.addAttribute("fullName", userEntity.getFullName());
            }
        }
    }
}

