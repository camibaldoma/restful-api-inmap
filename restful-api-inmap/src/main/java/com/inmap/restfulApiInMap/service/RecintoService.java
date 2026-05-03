package com.inmap.restfulApiInMap.service;


import com.inmap.restfulApiInMap.dto.InformacionRecintoDTO;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.error.OverlapException;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecintoService {
    List<Recinto> obtenerTodosRecintos();
    List<Recinto> findRecinto(String id) throws NotFoundException;
    List<InformacionRecintoDTO> findInformation(String id, String hora, String dia ) throws NotFoundException,OverlapException;
    List<Recinto> findRecintoBlocked();
    Recinto saveRecinto(Recinto recinto) throws ArgumentNotValidException, OverlapException;
    Recinto updateRecinto(String id, Recinto recinto) throws NotFoundException,ArgumentNotValidException;
    Recinto updateStateRecinto(String id, Boolean state) throws NotFoundException,ArgumentNotValidException;
    void deleteRecinto(String id) throws NotFoundException;
}
