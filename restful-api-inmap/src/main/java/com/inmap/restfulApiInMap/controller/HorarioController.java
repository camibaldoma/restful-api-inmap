package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.dto.HorarioRequestDTO;
import com.inmap.restfulApiInMap.entity.Horario;
import com.inmap.restfulApiInMap.entity.Materia;
import com.inmap.restfulApiInMap.repository.HorarioRepository;
import com.inmap.restfulApiInMap.repository.MateriaRepository;
import com.inmap.restfulApiInMap.service.HorarioService;
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
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @GetMapping("/horarios")
    public List<Horario> obtenerTodosHorarios() {
        return horarioService.obtenerTodosHorarios();
    }

    @PostMapping("/guardarHorario")
    @ResponseStatus(HttpStatus.CREATED)
    public Horario saveHorario(@Valid @RequestBody HorarioRequestDTO horario) {
        return horarioService.saveHorario(horario);
    }

    @PutMapping("/actualizarHorario/{id}")
    public Horario updateHorario(@PathVariable String id, @RequestBody HorarioRequestDTO horario) {
        return horarioService.updateHorario(id, horario);
    }

    @DeleteMapping("/eliminarHorario/{id}")
    public void deleteHorario(@PathVariable String id) {
        horarioService.deleteHorario(id);
    }

}
