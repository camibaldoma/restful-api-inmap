package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Materia;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface MateriaService {
    List<Materia> obtenerTodasMaterias();
    List<Recinto> findMateria(String id,String hora,String dia) throws NotFoundException;
    Materia saveMateria(Materia materia) throws ArgumentNotValidException;
    Materia updateMateria(String id,Materia materia) throws NotFoundException, ArgumentNotValidException;
    void deleteMateria(String idMateria) throws NotFoundException;
}
