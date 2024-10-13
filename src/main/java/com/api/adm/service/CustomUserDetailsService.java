package com.api.adm.service;

import com.api.adm.entity.Usuario;
import com.api.adm.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar al usuario en el repositorio
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Asignar roles con el prefijo ROLE_ y mostrar los roles cargados en consola
        var authorities = usuario.getRoles().stream()
                .map(role -> {
                    String roleName = "ROLE_" + role.getName(); // Asegúrate de obtener el nombre del rol correctamente
                    System.out.println("Rol cargado: " + roleName); // Imprimir los roles para verificación
                    return new SimpleGrantedAuthority(roleName);
                })
                .collect(Collectors.toList());

        // Retornar los detalles del usuario con los roles correspondientes
        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                authorities
        );
    }
}



