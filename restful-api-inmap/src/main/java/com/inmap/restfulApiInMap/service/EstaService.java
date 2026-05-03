package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Esta;
import com.inmap.restfulApiInMap.entity.Tiene_asociado;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.error.OverlapException;

import java.util.List;

public interface EstaService {
    List<Esta> obtenerTodosEsta();
    Esta saveEsta(Esta esta) throws NotFoundException, OverlapException, ArgumentNotValidException;
    void deleteEsta(String idPersonal, String idAsignacion) throws NotFoundException;
}
