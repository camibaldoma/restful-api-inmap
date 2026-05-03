package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.dto.AsignacionRequestDTO;
import com.inmap.restfulApiInMap.entity.Asignacion;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.error.OverlapException;
import com.inmap.restfulApiInMap.repository.AsignacionRepository;
import com.inmap.restfulApiInMap.repository.DestinoRepository;
import com.inmap.restfulApiInMap.service.AsignacionService;
import jakarta.validation.Valid;
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

public class AsignacionController {

    @Autowired
    private AsignacionRepository asignacionRepository;
    @Autowired
    private AsignacionService asignacionService;

    @GetMapping("/asignaciones")
    public List<Asignacion> obtenerTodoasAsignaciones() {
        return asignacionService.obtenerTodasAsignaciones(); // Esto hace el SELECT * FROM destino automáticamente
    }
    @PostMapping("/guardarAsignacion")
    public Asignacion saveAsignacion(@Valid @RequestBody AsignacionRequestDTO asignacion) throws ArgumentNotValidException, OverlapException, NotFoundException {
        return asignacionService.saveAsignacion(asignacion);
    }
    @PutMapping("/actualizarAsignacion/{id}")
    public Asignacion updateAsignacion(@PathVariable String id, @RequestBody AsignacionRequestDTO asignacion) throws ArgumentNotValidException, OverlapException, NotFoundException {
        return asignacionService.updateAsignacion(id, asignacion);
    }
    @DeleteMapping("/eliminarAsignacion/{id}")
    public void deleteAsignacion(@PathVariable String id) throws NotFoundException{
        asignacionService.deleteAsignacion(id);
    }
}
