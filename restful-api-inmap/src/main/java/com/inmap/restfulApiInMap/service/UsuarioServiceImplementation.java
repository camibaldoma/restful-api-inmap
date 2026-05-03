package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Usuario;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImplementation implements UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Override
    @Transactional
    public void eliminarUsuario(String username) throws NotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        usuarioRepository.deleteById(usuario.getIdUsuario());
    }
}
