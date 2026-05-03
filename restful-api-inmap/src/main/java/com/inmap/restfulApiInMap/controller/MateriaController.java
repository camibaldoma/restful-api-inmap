package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.entity.Materia;

import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.repository.MateriaRepository;

import com.inmap.restfulApiInMap.service.MateriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    @GetMapping("/materias")
    public List<Materia> obtenerTodasMaterias() {
        return materiaService.obtenerTodasMaterias();
    }
    @GetMapping("/materia/{id}/{hora}/{dia}")
    public List<Recinto> findMateria(@PathVariable String id, @PathVariable String hora, @PathVariable String dia) throws NotFoundException
    {
        return materiaService.findMateria(id,hora,dia);
    }
    @PostMapping("/guardarMateria")
    @ResponseStatus(HttpStatus.CREATED)
    public Materia saveMateria(@Valid @RequestBody Materia materia) throws ArgumentNotValidException{
        return materiaService.saveMateria(materia);
    }
    @PutMapping("/actualizarMateria/{id}")
    public Materia updateMateria(@PathVariable String id, @RequestBody Materia materia) throws NotFoundException, ArgumentNotValidException {
        return materiaService.updateMateria(id, materia);
    }
    @DeleteMapping("/eliminarMateria/{id}")
    public void deleteMateria(@PathVariable String id) throws NotFoundException {
        materiaService.deleteMateria(id);
    }
}
