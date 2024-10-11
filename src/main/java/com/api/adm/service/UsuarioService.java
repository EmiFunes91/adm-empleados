package com.api.adm.service;

import com.api.adm.entity.Usuario;
import java.util.List;

public interface UsuarioService {
    void guardarUsuario(Usuario usuario);
    List<Usuario> obtenerTodosLosUsuarios();
}


