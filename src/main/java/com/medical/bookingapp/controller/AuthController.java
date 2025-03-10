package com.medical.bookingapp.controller;

import com.medical.bookingapp.dto.UserDTO;
import com.medical.bookingapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", result.getAllErrors().get(0).getDefaultMessage());
            return "register";
        }

        try {
            userService.registerUser(userDTO);
            model.addAttribute("successMessage", "Đăng ký thành công! Hãy đăng nhập.");
            return "redirect:/auth/login"; // Ở lại trang đăng ký, không redirect
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }


    @GetMapping("/change-password")
    public String showChangePasswordForm(
            @RequestParam(value = "redirect", required = false) String redirect,
            Model model
    ) {
        // Lưu redirect vào model để Thymeleaf hiển thị trong hidden input
        model.addAttribute("redirect", redirect);
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam(value = "redirect", required = false) String redirectUrl,
            Authentication authentication,
            Model model
    ) {
        String currentUsername = authentication.getName();

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Xác nhận mật khẩu mới không khớp!");
            if (redirectUrl != null && !redirectUrl.isEmpty() && redirectUrl.equals("/admin/change-password")) {
                // Nếu có URL mong muốn, redirect về đó
                return "admin-change-password";
            }
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                // Nếu có URL mong muốn, redirect về đó
                return "redirect:" + redirectUrl;
            }
            return "change-password";
        }

        try {
            userService.changePassword(currentUsername, oldPassword, newPassword);
            model.addAttribute("successMessage", "Đổi mật khẩu thành công!");

            // Đổi mật khẩu thành công
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                if (redirectUrl != null && !redirectUrl.isEmpty() && redirectUrl.equals("/admin/change-password")) {
                    // Nếu có URL mong muốn, redirect về đó
                    return "admin-change-password";
                }
                // Nếu có URL mong muốn, redirect về đó
                return "redirect:" + redirectUrl;
            } else {
                if (redirectUrl != null && !redirectUrl.isEmpty() && redirectUrl.equals("/admin/change-password")) {
                    // Nếu có URL mong muốn, redirect về đó
                    return "admin-change-password";
                }
                // Nếu không có, redirect về trang chủ hoặc trang tuỳ ý
                return "redirect:/auth/login";
            }
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            if (redirectUrl != null && !redirectUrl.isEmpty() && redirectUrl.equals("/admin/change-password")) {
                // Nếu có URL mong muốn, redirect về đó
                return "admin-change-password";
            }
            return "change-password";
        }
    }


}
