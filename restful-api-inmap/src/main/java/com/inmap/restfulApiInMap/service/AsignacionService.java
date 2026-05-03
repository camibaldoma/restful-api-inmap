package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.dto.AsignacionRequestDTO;
import com.inmap.restfulApiInMap.entity.Asignacion;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.error.OverlapException;

import java.util.List;

public interface AsignacionService {
    List<Asignacion> obtenerTodasAsignaciones();
    Boolean existsChoqueDeHorario(String idDestino,String dia, String horaInicio, String horaFin, String idActual);
    Asignacion saveAsignacion(AsignacionRequestDTO asignacion) throws ArgumentNotValidException, OverlapException, NotFoundException;
    Asignacion updateAsignacion(String id, AsignacionRequestDTO asignacion) throws ArgumentNotValidException, NotFoundException, OverlapException;
    void deleteAsignacion(String id) throws NotFoundException;
}
