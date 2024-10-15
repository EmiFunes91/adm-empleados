package com.api.adm.controllers;

import com.api.adm.service.CustomUserDetailsService;
import com.api.adm.util.JwtUtil;
import com.api.adm.dto.AuthRequest;
import com.api.adm.dto.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    // Manejo del formulario de inicio de sesión clásico
    @GetMapping("/login")
    public String loginForm() {
        return "login"; // Muestra el formulario de inicio de sesión
    }

    @PostMapping("/login")
    public String loginWithForm(@RequestParam("username") String username, @RequestParam("password") String password) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            return "redirect:/auth/login?error=true"; // Redirige al formulario con un mensaje de error
        }

        return "redirect:/dashboard"; // Redirige a la página principal después de un login exitoso
    }

    // API para autenticación vía JSON y JWT
    @PostMapping("/api/login")
    @ResponseBody
    public AuthResponse loginApi(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            throw new Exception("Usuario o contraseña incorrectos");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return new AuthResponse(jwt);
    }
}


