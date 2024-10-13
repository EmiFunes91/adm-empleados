package com.api.adm.config;

import com.api.adm.entity.Role;
import com.api.adm.entity.Usuario;
import com.api.adm.service.RoleService;
import com.api.adm.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Override
    public void run(String... args) throws Exception {
        String adminPassword = System.getenv("ADMIN_PASSWORD");
        logger.info("Valor de la variable de entorno ADMIN_PASSWORD: " + adminPassword);

        if (adminPassword == null || adminPassword.isEmpty()) {
            throw new RuntimeException("La variable de entorno 'ADMIN_PASSWORD' no está configurada.");
        }

        Optional<Usuario> usuarioOptional = usuarioService.buscarPorUsername("admin");

        if (usuarioOptional.isEmpty()) {
            Optional<Role> adminRoleOptional = roleService.buscarPorNombre("ADMIN");
            Role adminRole;

            if (adminRoleOptional.isEmpty()) {
                // Crear rol si no existe
                adminRole = new Role();
                adminRole.setName("ADMIN");
                adminRole = roleService.guardar(adminRole);
                logger.info("Rol ADMIN creado.");
            } else {
                // Usar rol existente
                adminRole = adminRoleOptional.get();
            }

            // Crear usuario administrador
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setEmail("admin@ejemplo.com");

            Set<Role> roles = new HashSet<>();
            roles.add(adminRole); // Asignar rol persistido
            admin.setRoles(roles);

            // Guardar usuario
            usuarioService.guardarUsuario(admin);
            logger.info("Usuario administrador creado: admin/[contraseña desde variable de entorno]");
        } else {
            logger.info("El usuario administrador ya existe.");
        }
    }
}









