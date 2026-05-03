package com.inmap.restfulApiInMap.controller;


import com.inmap.restfulApiInMap.dto.PersonalReducidoDTO;
import com.inmap.restfulApiInMap.dto.PersonalRequestDTO;
import com.inmap.restfulApiInMap.dto.UbicacionPersonalDTO;
import com.inmap.restfulApiInMap.entity.Esqueleto;
import com.inmap.restfulApiInMap.entity.Personal;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;

import com.inmap.restfulApiInMap.repository.EsqueletoRepository;
import com.inmap.restfulApiInMap.repository.PersonalRepository;
import com.inmap.restfulApiInMap.service.PersonalService;
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
public class PersonalController {

    @Autowired
    private PersonalService personalService;

    @GetMapping("/personalCompleto")
    public List<Personal> obtenerTodoPersonal() {
        return personalService.obtenerTodoPersonal();
    }
    @GetMapping("/personal")
    public List<PersonalReducidoDTO> findAllOrderByApellido(){
        return personalService.findAllOrderByApellido();
    }
    @GetMapping("/personal/{id}/{hora}/{dia}")
    public List<UbicacionPersonalDTO> findUbicacionCompletaNative(@PathVariable String id, @PathVariable String dia, @PathVariable String hora) throws NotFoundException
    {
        return personalService.findUbicacionCompletaNative(id, dia, hora);
    }
    @PostMapping("/guardarPersonal")
    @ResponseStatus(HttpStatus.CREATED)
    public Personal savePersonal(@Valid @RequestBody PersonalRequestDTO personal) throws ArgumentNotValidException {
        return personalService.savePersonal(personal);
    }
    @PutMapping("/actualizarPersonal/{id}")
    public Personal updatePersonal(@PathVariable String id, @RequestBody PersonalRequestDTO personal) throws NotFoundException,ArgumentNotValidException {
        return personalService.updatePersonal(id, personal);
    }
    @DeleteMapping("/eliminarPersonal/{id}")
    public void deletePersonal(@PathVariable String id) throws NotFoundException {
        personalService.deletePersonal(id);
    }
}
