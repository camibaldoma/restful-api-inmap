package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.classes.AuthResponse;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    @DeleteMapping("/eliminarUsuario/{username}")
    public void deleteUsuario(@PathVariable String username) throws NotFoundException
    {
        usuarioService.eliminarUsuario(username);
    }
}
