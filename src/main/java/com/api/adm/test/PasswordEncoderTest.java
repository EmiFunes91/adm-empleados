package com.api.adm.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newPassword = "36River449millo."; // Cambia esto por tu nueva contraseña
        String encodedPassword = passwordEncoder.encode(newPassword);
        System.out.println(encodedPassword);
    }
}

