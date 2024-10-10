package com.api.adm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Deshabilitar CSRF si solo usas APIs REST
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").authenticated()  // Requiere autenticación para las APIs
                        .requestMatchers("/cajeros/**", "/empleados/**", "/dashboard").hasRole("ADMIN")  // Solo ADMIN puede acceder a estas rutas
                        .anyRequest().permitAll()  // Permitir el acceso a otras rutas (login, home, etc.)
                )
                .formLogin(form -> form  // Configurar el formulario de login
                        .loginPage("/login")  // Definir la página de login personalizada
                        .defaultSuccessUrl("/dashboard", true)  // Redirigir a dashboard tras iniciar sesión
                        .permitAll()  // Permitir acceso a la página de login
                )
                .logout(logout -> logout  // Configurar el logout
                        .logoutUrl("/logout")  // URL para cerrar sesión
                        .logoutSuccessUrl("/login?logout")  // Redirigir tras cerrar sesión
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();


        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}





