package com.inmap.restfulApiInMap.controller;


import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.entity.Zonas_a_bloquear;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.service.Zonas_a_bloquearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(
        originPatterns = "*",
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = "*"
)
public class Zonas_a_bloquearController {
    @Autowired
    private Zonas_a_bloquearService zonas_a_bloquearService;

    @GetMapping("/obtenerZonas")
    public List<Zonas_a_bloquear> obtenerTodasZonas(){
        return zonas_a_bloquearService.obtenerTodasZonas();
    }

    @GetMapping("/obtenerZonasBloqueadas")
    public List<Zonas_a_bloquear> findZonasBlocked(){
        return zonas_a_bloquearService.findZonasBlocked();
    }
    @GetMapping("/obtenerZonasNoBloqueadas")
    public List<Zonas_a_bloquear> findZonasNoBlocked(){
        return zonas_a_bloquearService.findZonasNoBlocked();
    }

    @PutMapping("/actualizarEstadoZona/{id}")
    public Zonas_a_bloquear updateStateZona(@PathVariable String id, @RequestBody Boolean state) throws NotFoundException, ArgumentNotValidException {
        return zonas_a_bloquearService.updateStateZona(id, state);
    }
    @PutMapping("/actualizarEstadosZonas/{state}")
    public List<Zonas_a_bloquear> updateSeveralStateZonas(@RequestBody List<String> ids, @PathVariable Boolean state) throws NotFoundException
    {
        return zonas_a_bloquearService.updateSeveralStateZonas(ids, state);
    }
}
