package com.api.adm.service;

import com.api.adm.entity.Usuario;
import com.api.adm.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar al usuario en la base de datos
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Verificar si la cuenta está activa
        if (!usuario.isActivo()) {
            throw new RuntimeException("Cuenta no activada. Verifique su correo electrónico.");
        }

        // Loguear los detalles del usuario
        System.out.println("Usuario encontrado: " + usuario.getUsername());
        System.out.println("Roles: " + usuario.getRoles());

        // Convertir los roles del usuario a GrantedAuthority
        List<SimpleGrantedAuthority> authorities = usuario.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase())) // Asegúrate de que el nombre del rol sea mayúscula
                .collect(Collectors.toList());

        // Retornar el objeto UserDetails con los detalles del usuario y sus roles
        return new org.springframework.security.core.userdetails.User(
                usuario.getUsername(),
                usuario.getPassword(),
                authorities
        );
    }
}












