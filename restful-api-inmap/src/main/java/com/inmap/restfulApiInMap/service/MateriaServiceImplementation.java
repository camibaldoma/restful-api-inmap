package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Materia;
import com.inmap.restfulApiInMap.entity.Personal;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.repository.MateriaRepository;
import com.inmap.restfulApiInMap.repository.PersonalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MateriaServiceImplementation implements MateriaService {
    @Autowired
    MateriaRepository materiaRepository;
    @Override
    public List<Materia> obtenerTodasMaterias(){
        return materiaRepository.findAll();
    }
    @Override
    public List<Recinto> findMateria(String id,String hora,String dia) throws NotFoundException
    {
        List<Recinto> resultados =  materiaRepository.findMateria(id,hora,dia);
        if(resultados == null || resultados.isEmpty())
        {
            if(materiaRepository.existsById(id))
            {
                throw new NotFoundException("La materia no se está dictando en ningún espacio del establecimiento en este momento.");
            }
            else{
                throw new NotFoundException("El código no corresponde a una materia que se dicte en el establecimiento.");
            }

        }
        else {
            return resultados;
        }
    }

    @Override
    public Materia saveMateria(Materia materia) throws ArgumentNotValidException {
        if (materiaRepository.existsById(materia.getCodMateria())) {
            throw new ArgumentNotValidException("El código ya existe, no se puede usar uno duplicado");
        }
        return materiaRepository.save(materia);
    }

    @Override
    public Materia updateMateria(String id,Materia materia) throws NotFoundException, ArgumentNotValidException {
        Materia materiaToUpdate = materiaRepository.findById(id).orElseThrow(() -> new NotFoundException("Materia no encontrada"));
        if (materia.getCodMateria() != null && !id.equals(materia.getCodMateria())) {
            throw new ArgumentNotValidException("No está permitido cambiar el código de una materia.");
        }
        if(Objects.nonNull(materia.getCodMateria()) && !"".equalsIgnoreCase(materia.getCodMateria())){
            materiaToUpdate.setCodMateria(materia.getCodMateria());
        }
        if(Objects.nonNull(materia.getNombreMateria()) && !"".equalsIgnoreCase(materia.getNombreMateria())){
            materiaToUpdate.setNombreMateria(materia.getNombreMateria());
        }
        return materiaRepository.save(materiaToUpdate);
    }

    @Override
    public void deleteMateria(String idMateria) throws NotFoundException {
        Materia materiaToDelete = materiaRepository.findById(idMateria).orElseThrow(() -> new NotFoundException("Materia no encontrada"));
        materiaRepository.deleteById(idMateria);
    }
}
