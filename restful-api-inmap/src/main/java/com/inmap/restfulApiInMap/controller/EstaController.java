package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.entity.Esta;
import com.inmap.restfulApiInMap.entity.Tiene_asociado;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.error.OverlapException;
import com.inmap.restfulApiInMap.repository.EstaRepository;
import com.inmap.restfulApiInMap.repository.Tiene_asociadoRepository;
import com.inmap.restfulApiInMap.service.EstaService;
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
public class EstaController {

    @Autowired
    private EstaService estaService;

    @GetMapping("/esta")
    public List<Esta> obtenerTodosEsta() {
        return estaService.obtenerTodosEsta();
    }

    @PostMapping("/guardarEsta")
    public Esta saveEsta(@Valid @RequestBody Esta esta) throws NotFoundException, OverlapException, ArgumentNotValidException {
        return estaService.saveEsta(esta);
    }
    @DeleteMapping("/eliminarEsta/{idPersonal}/{idAsignacion}")
    public void eliminarEsta(@PathVariable String idPersonal,@PathVariable String idAsignacion) throws NotFoundException {
        estaService.deleteEsta(idPersonal, idAsignacion);
    }
}
