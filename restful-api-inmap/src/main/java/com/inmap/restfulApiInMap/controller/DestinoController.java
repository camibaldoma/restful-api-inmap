package com.inmap.restfulApiInMap.controller;


import com.inmap.restfulApiInMap.dto.DestinoReducidoDTO;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.service.DestinoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(
        originPatterns = "*",
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = "*"
)
public class DestinoController {


    @Autowired
    private DestinoService destinoService;

    @GetMapping("/destinos")
    public List<Destino> obtenerTodosDestinos() {
        return destinoService.obtenerTodosDestinos(); // Esto hace el SELECT * FROM destino automáticamente
    }
    @GetMapping("/destinos/{id}")
    public List<DestinoReducidoDTO> findDestino(@PathVariable String id) throws NotFoundException {
        return destinoService.findDestino(id);
    }

    @PostMapping("/guardarDestino")
    @ResponseStatus(HttpStatus.CREATED) // Esto fuerza el código 201
    public Destino saveDestino(@Valid @RequestBody Destino destino) throws ArgumentNotValidException {
        return destinoService.saveDestino(destino);
    }

    @PutMapping("/actualizarDestino/{id}")
    public Destino updateDestino(@PathVariable String id, @RequestBody Destino destino)  throws NotFoundException, ArgumentNotValidException {
        return destinoService.updateDestino(id, destino);
    }

    @DeleteMapping("/eliminarDestino/{id}")
    public void deleteDestino(@PathVariable String id)  throws NotFoundException {
        destinoService.deleteDestino(id);
    }

}
