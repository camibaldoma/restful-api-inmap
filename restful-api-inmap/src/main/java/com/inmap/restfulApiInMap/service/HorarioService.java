package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.dto.HorarioRequestDTO;
import com.inmap.restfulApiInMap.entity.Horario;
import com.inmap.restfulApiInMap.entity.Materia;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;

import java.util.List;

public interface HorarioService {
    List<Horario> obtenerTodosHorarios();
    Horario saveHorario(HorarioRequestDTO horario) throws ArgumentNotValidException;
    Horario updateHorario(String id, HorarioRequestDTO horario) throws ArgumentNotValidException, NotFoundException;
    void deleteHorario(String id) throws NotFoundException;
}
