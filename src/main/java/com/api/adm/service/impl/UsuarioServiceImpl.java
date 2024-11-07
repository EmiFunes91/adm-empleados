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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    @Transactional
    public Usuario crearCuentaUsuario(UsuarioDTO usuarioDTO) {
        // Verificar si el correo o el nombre de usuario ya existen
        if (existePorEmail(usuarioDTO.getEmail())) {
            throw new RuntimeException("El correo electrónico ya está en uso.");
        }
        if (existePorUsername(usuarioDTO.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }

        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword())); // Encriptar contraseña
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setActivo(false); // Inicialmente inactivo

        // Asignar rol predeterminado
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        roles.add(role);
        usuario.setRoles(roles);

        // Guardar el usuario
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Enviar correo de confirmación
        enviarCorreoDeConfirmacion(usuario);

        return usuarioGuardado;
    }

    // Método para enviar el correo de confirmación
    private void enviarCorreoDeConfirmacion(Usuario usuario) {
        String asunto = "Confirmación de Registro";
        String mensaje = "Hola " + usuario.getUsername() + ",\n\nTu cuenta ha sido creada exitosamente. " +
                "Por favor, activa tu cuenta haciendo clic en el siguiente enlace:\n" +
                "http://localhost:8080/activacion/" + usuario.getId() + "\n\n" +
                "Gracias por registrarte.";
        emailService.enviarEmailDeConfirmacion("soporte.funesapps@gmail.com", asunto, mensaje); // Enviar al soporte
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
        return usuarioRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query);
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
        return List.of(); // Puedes implementar según tu necesidad
    }

    @Transactional
    @Override
    public Usuario registrarUsuario(UsuarioDTO usuarioDTO) {
        // Validar que el correo electrónico y el nombre de usuario no existan
        if (existePorEmail(usuarioDTO.getEmail())) {
            throw new RuntimeException("El correo electrónico ya está en uso.");
        }

        if (existePorUsername(usuarioDTO.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }

        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setActivo(true); // Activa la cuenta si se registra

        // Asignar roles
        Set<Role> roles = new HashSet<>();
        for (String roleName : usuarioDTO.getRoles()) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleName));
            roles.add(role);
        }
        usuario.setRoles(roles);

        return usuarioRepository.save(usuario); // Retornar el usuario guardado
    }

    @Override
    public Usuario actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setEmail(usuarioDTO.getEmail());

        if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        }

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
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    // Método para activar cuenta
    @Transactional
    @Override
    public void activarCuenta(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(true); // Cambiar a activo

        // Asignar rol "USER" al usuario
        Role role = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        usuario.getRoles().add(role);

        usuarioRepository.save(usuario);
    }

}




















