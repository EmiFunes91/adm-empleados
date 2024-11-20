package com.api.adm.service.impl;

import com.api.adm.dto.UsuarioDTO;
import com.api.adm.entity.Role;
import com.api.adm.entity.Usuario;
import com.api.adm.repository.RoleRepository;
import com.api.adm.repository.UsuarioRepository;
import com.api.adm.service.EmailService;
import com.api.adm.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public void guardarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public boolean existePorEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean existePorUsername(String username) {
        return usuarioRepository.findByUsername(username).isPresent();
    }

    @Override
    public List<Usuario> buscarUsuarios(String query) {
        return usuarioRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(query);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public void eliminarUsuarioPorId(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public List<Usuario> obtenerUsuariosPorRol(String rolName) {
        return usuarioRepository.findUsuariosByRolName(rolName);
    }

    @Override
    @Transactional
    public Usuario registrarUsuario(UsuarioDTO usuarioDTO) {
        if (existePorEmail(usuarioDTO.getEmail())) {
            throw new RuntimeException("El correo electrónico ya está en uso.");
        }
        if (existePorUsername(usuarioDTO.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setActivo(true);

        Set<Role> roles = new HashSet<>();
        for (String roleName : usuarioDTO.getRoles()) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleName));
            roles.add(role);
        }
        usuario.setRoles(roles);

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario crearCuentaUsuario(UsuarioDTO usuarioDTO) {
        if (existePorEmail(usuarioDTO.getEmail())) {
            throw new RuntimeException("El correo electrónico ya está en uso.");
        }
        if (existePorUsername(usuarioDTO.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setActivo(false);

        String token = UUID.randomUUID().toString();
        usuario.setActivationToken(token);
        usuario.setTokenExpiration(LocalDateTime.now().plusHours(24));

        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        usuario.setRoles(Set.of(role));

        usuarioRepository.save(usuario);

        enviarCorreoActivacion(usuario);
        return usuario;
    }

    private void enviarCorreoActivacion(Usuario usuario) {
        String enlace = "http://localhost:8080/activacion?token=" + usuario.getActivationToken();
        String asunto = "Activación de Cuenta";
        String mensaje = "Hola " + usuario.getUsername() + ",\n\nActiva tu cuenta haciendo clic en este enlace:\n" + enlace +
                "\n\nEste enlace expirará en 24 horas.";
        emailService.enviarEmailDeConfirmacion(usuario.getEmail(), asunto, mensaje);
    }

    @Override
    @Transactional
    public Usuario actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setEmail(usuarioDTO.getEmail());

        if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        }

        if (usuarioDTO.getRoles() != null && !usuarioDTO.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (String roleName : usuarioDTO.getRoles()) {
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleName));
                roles.add(role);
            }
            usuario.setRoles(roles);
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public void activarCuenta(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setActivo(true);
        usuario.setActivationToken(null);
        usuario.setTokenExpiration(null);

        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void activarCuentaConToken(String token) {
        Usuario usuario = usuarioRepository.findByActivationToken(token)
                .orElseThrow(() -> new RuntimeException("Token de activación inválido o no encontrado."));

        if (usuario.getTokenExpiration() != null && usuario.getTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El token de activación ha expirado.");
        }

        usuario.setActivo(true);
        usuario.setActivationToken(null);
        usuario.setTokenExpiration(null);

        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void reenviarTokenActivacion(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No existe una cuenta asociada a este correo electrónico."));

        if (usuario.isActivo()) {
            throw new RuntimeException("La cuenta ya está activada.");
        }

        String nuevoToken = UUID.randomUUID().toString();
        usuario.setActivationToken(nuevoToken);
        usuario.setTokenExpiration(LocalDateTime.now().plusHours(24));

        usuarioRepository.save(usuario);

        String enlace = "http://localhost:8080/activacion?token=" + nuevoToken;
        String asunto = "Reenvío de Activación de Cuenta";
        String mensaje = "Hola " + usuario.getUsername() + ",\n\nHemos generado un nuevo enlace para activar tu cuenta. Por favor, haz clic en el siguiente enlace:\n"
                + enlace + "\n\nEste enlace expirará en 24 horas.";
        emailService.enviarEmailDeConfirmacion(usuario.getEmail(), asunto, mensaje);
    }

    @Override
    @Transactional
    public void solicitarRecuperacionPassword(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No existe una cuenta asociada a este correo electrónico."));

        String token = UUID.randomUUID().toString();
        usuario.setResetPasswordToken(token);
        usuario.setResetPasswordExpiration(LocalDateTime.now().plusHours(1));

        usuarioRepository.save(usuario);

        String enlace = "http://localhost:8080/reset-password?token=" + token;
        String asunto = "Recuperación de Contraseña";
        String mensaje = "Hola " + usuario.getUsername() + ",\n\nPuedes restablecer tu contraseña haciendo clic en el siguiente enlace:\n"
                + enlace + "\n\nEste enlace expirará en 1 hora.";
        emailService.enviarEmailDeConfirmacion(usuario.getEmail(), asunto, mensaje);
    }

    @Override
    @Transactional
    public void restablecerPassword(String token, String nuevaPassword) {
        Usuario usuario = usuarioRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new RuntimeException("Token de recuperación inválido o no encontrado."));

        if (usuario.getResetPasswordExpiration() != null && usuario.getResetPasswordExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El token de recuperación ha expirado.");
        }

        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuario.setResetPasswordToken(null);
        usuario.setResetPasswordExpiration(null);

        usuarioRepository.save(usuario);
    }
}






























