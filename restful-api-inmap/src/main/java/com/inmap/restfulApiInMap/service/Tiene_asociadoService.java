package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.entity.Tiene_asociado;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.error.OverlapException;

import java.util.List;

public interface Tiene_asociadoService {
    List<Tiene_asociado> obtenerTodosAsociados();
    Tiene_asociado saveTiene_asociado(Tiene_asociado tiene) throws NotFoundException, OverlapException, ArgumentNotValidException;
    void deleteTiene_asociado(String idPersonal,String idDestino) throws NotFoundException;
}
