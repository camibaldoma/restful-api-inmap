package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.error.NotFoundException;

public interface UsuarioService {
    public void eliminarUsuario(String idUsuario) throws NotFoundException;
}
