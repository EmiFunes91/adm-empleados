package com.api.adm.config;

import com.api.adm.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Inyectamos el servicio personalizado para cargar los detalles de los usuarios desde la base de datos.
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF: Esta configuración es útil para APIs REST, donde no se requiere protección CSRF.
                .csrf(AbstractHttpConfigurer::disable)
                // Configuración de autorizaciones para diferentes rutas.
                .authorizeHttpRequests(auth -> auth
                        // Requiere autenticación para acceder a cualquier endpoint de la API.
                        .requestMatchers("/api/**").authenticated()
                        // Solo los usuarios con rol ADMIN pueden acceder a las rutas de gestión de empleados, cajeros y al dashboard.
                        .requestMatchers("/cajeros/**", "/empleados/**", "/dashboard").hasRole("ADMIN")
                        // Permitir el acceso a cualquier otra ruta sin necesidad de autenticación.
                        .anyRequest().permitAll()
                )
                // Configuración del formulario de login.
                .formLogin(form -> form
                        // Define la ruta de la página de login personalizada.
                        .loginPage("/login")
                        // Redirige a la página del dashboard tras un inicio de sesión exitoso.
                        .defaultSuccessUrl("/dashboard", true)
                        // Permite a cualquier usuario acceder a la página de login.
                        .permitAll()
                )
                // Configuración para cerrar sesión.
                .logout(logout -> logout
                        // Define la URL para realizar el logout.
                        .logoutUrl("/logout")
                        // Redirige a la página de login con un mensaje de logout exitoso.
                        .logoutSuccessUrl("/login?logout")
                        // Permite a cualquier usuario realizar el logout.
                        .permitAll()
                )
                // Asigna el servicio personalizado de carga de detalles de usuarios para la autenticación.
                .userDetailsService(customUserDetailsService);

        // Construye y devuelve el SecurityFilterChain con la configuración proporcionada.
        return http.build();
    }

    // Bean para encriptar contraseñas usando BCrypt.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}






