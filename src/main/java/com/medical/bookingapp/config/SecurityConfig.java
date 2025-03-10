package com.medical.bookingapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/doctor/**","/service/**","/404","/error/*", "/", "index","/auth/register", "/auth/login", "/css/**", "/js/**", "/uploads/*").permitAll() // Ai cũng vào được
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Chỉ admin vào được
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")  // REST API
                        .requestMatchers("/doctor-mnt/**").hasRole("DOCTOR") // Chỉ bác sĩ vào được
                        .requestMatchers("/customer/**", "/user/**").hasRole("CUSTOMER") // Chỉ khách hàng vào được
                        .requestMatchers("/appointment/**", "/appointment-list", "/review/**").hasAnyRole("CUSTOMER", "ADMIN") // Chỉ khách hàng vào được
                        .requestMatchers("/api/invoices/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .successHandler(customSuccessHandler()) // Điều hướng đúng role sau login
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied") // Nếu truy cập trang không có quyền
                );
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return (request, response, authentication) -> {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String role = userDetails.getAuthorities().iterator().next().getAuthority();

            if (role.equals("ROLE_ADMIN")) {
                response.sendRedirect("/admin/home");
            } else if (role.equals("ROLE_DOCTOR")) {
                response.sendRedirect("/doctor/home");
            } else {
                response.sendRedirect("/");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
