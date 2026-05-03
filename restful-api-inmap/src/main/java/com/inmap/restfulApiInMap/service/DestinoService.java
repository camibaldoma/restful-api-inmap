package com.inmap.restfulApiInMap.service;


import com.inmap.restfulApiInMap.dto.DestinoReducidoDTO;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;

import java.util.List;

public interface DestinoService {
    List<Destino> obtenerTodosDestinos();
    List<DestinoReducidoDTO> findDestino(String id_destino)  throws NotFoundException;
    Destino saveDestino(Destino destino) throws ArgumentNotValidException;
    Destino updateDestino(String id, Destino destino)  throws NotFoundException,ArgumentNotValidException;
    void deleteDestino(String id)  throws NotFoundException;
}
