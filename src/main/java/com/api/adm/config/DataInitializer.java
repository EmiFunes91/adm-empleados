package com.api.adm.config;

import com.api.adm.entity.Role;
import com.api.adm.entity.Usuario;
import com.api.adm.service.RoleService;
import com.api.adm.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminPassword = System.getenv("ADMIN_PASSWORD");
        logger.info("Valor de la variable de entorno ADMIN_PASSWORD: " + adminPassword);

        if (adminPassword == null || adminPassword.isEmpty()) {
            throw new RuntimeException("La variable de entorno 'ADMIN_PASSWORD' no está configurada.");
        }

        // Inicializar roles si no existen
        roleService.inicializarRoles();

        // Crear usuario administrador si no existe
        if (!usuarioService.existePorUsername("admin")) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setEmail("admin@ejemplo.com");

            Set<Role> roles = new HashSet<>();
            Role adminRole = roleService.buscarPorNombre("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));
            roles.add(adminRole);
            admin.setRoles(roles);

            // Guardar usuario con el rol ADMIN
            usuarioService.guardarUsuario(admin, "ADMIN");
            logger.info("Usuario administrador creado: admin/[contraseña desde variable de entorno]");
        } else {
            logger.info("El usuario administrador ya existe.");
        }
    }
}














