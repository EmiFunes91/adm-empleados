package com.api.adm.controllers;

import com.api.adm.dto.AuthRequest;
import com.api.adm.dto.AuthResponse;
import com.api.adm.dto.UsuarioDTO;
import com.api.adm.service.UsuarioService;
import com.api.adm.service.CustomUserDetailsService;
import com.api.adm.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import jakarta.validation.Valid;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    // Endpoint para autenticación y generación de token JWT
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authenticationRequest)
            throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Credenciales incorrectas", e);
        }

        final UserDetails userDetails = customUserDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    // Endpoint para registro de usuarios
    @PostMapping("/usuarios/registro")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UsuarioDTO usuarioDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Errores en la validación");
        }

        if (usuarioService.existePorEmail(usuarioDTO.getEmail())) {
            return ResponseEntity.badRequest().body("El correo electrónico ya está en uso.");
        }

        if (usuarioService.existePorUsername(usuarioDTO.getUsername())) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya está en uso.");
        }

        usuarioService.registrarUsuario(usuarioDTO);

        return ResponseEntity.ok("Usuario registrado exitosamente");
    }
}
