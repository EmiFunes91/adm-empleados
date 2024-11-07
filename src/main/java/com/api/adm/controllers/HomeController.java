package com.api.adm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping("/")
    public ModelAndView home() {
        // Redirigir a la página de crear cuenta
        return new ModelAndView("redirect:/crear-cuenta");
    }
}



