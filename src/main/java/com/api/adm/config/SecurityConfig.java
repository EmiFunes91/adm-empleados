package com.api.adm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/crear-cuenta", "/login", "/logout", "/css/**", "/js/**").permitAll() // Permitir acceso a estas rutas
                        .anyRequest().authenticated() // Cualquier otra solicitud requiere autenticación
                )
                .formLogin(form -> form
                        .loginPage("/login") // Página de login
                        .defaultSuccessUrl("/dashboard", true) // Redirigir al dashboard después del login exitoso
                        .permitAll()
                )
                .logout(logout -> logout
                        .permitAll()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout") // Redirigir al login tras cerrar sesión
                )
                // Redirige a /crear-cuenta cuando se accede a la página de inicio sin autenticación
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/crear-cuenta"))
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}









