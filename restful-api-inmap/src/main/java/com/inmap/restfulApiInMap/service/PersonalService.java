package com.inmap.restfulApiInMap.service;


import com.inmap.restfulApiInMap.dto.PersonalReducidoDTO;
import com.inmap.restfulApiInMap.dto.PersonalRequestDTO;
import com.inmap.restfulApiInMap.dto.UbicacionPersonalDTO;
import com.inmap.restfulApiInMap.entity.Personal;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;

import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonalService {
    List<Personal> obtenerTodoPersonal();
    List<PersonalReducidoDTO> findAllOrderByApellido();
    List<UbicacionPersonalDTO> findUbicacionCompletaNative(String id, String dia, String hora) throws NotFoundException;
    Personal savePersonal(PersonalRequestDTO personal) throws ArgumentNotValidException;
    Personal updatePersonal(String idPersonal, PersonalRequestDTO  personal) throws NotFoundException,ArgumentNotValidException;
    void deletePersonal(String idPersonal) throws NotFoundException;
}
