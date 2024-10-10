package com.api.adm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String mostrarDashboard() {
        return "dashboard";  // El nombre del archivo Thymeleaf para la vista del dashboard
    }
}

