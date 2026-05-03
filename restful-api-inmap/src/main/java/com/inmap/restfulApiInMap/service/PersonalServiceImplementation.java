package com.inmap.restfulApiInMap.service;


import com.inmap.restfulApiInMap.dto.PersonalReducidoDTO;
import com.inmap.restfulApiInMap.dto.PersonalRequestDTO;
import com.inmap.restfulApiInMap.dto.UbicacionPersonalDTO;
import com.inmap.restfulApiInMap.entity.Personal;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;

import com.inmap.restfulApiInMap.repository.PersonalRepository;
import com.inmap.restfulApiInMap.repository.RecintoRepository;
import org.geolatte.geom.jts.JTS;
import org.locationtech.jts.geom.Geometry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PersonalServiceImplementation implements PersonalService{

    @Autowired
    PersonalRepository personalRepository;
    @Override
    public List<Personal> obtenerTodoPersonal(){
        return personalRepository.findAll();
    }
    @Override
    public List<PersonalReducidoDTO> findAllOrderByApellido(){
        return personalRepository.findAllOrderByApellido();
    }

    @Override
    public List<UbicacionPersonalDTO> findUbicacionCompletaNative(String id, String dia, String hora) throws NotFoundException
    {
        List<Object[]> resultados = personalRepository.findUbicacionCompletaNative(id, dia, hora);
        if(resultados == null || resultados.isEmpty())
        {
            if(personalRepository.existsById(id))
            {
                //El personal existe, pero no está presente en el momento de la consulta.
                throw new NotFoundException("El miembro del personal de la facultad con id= "+ id + " no se encuentra presente en este momento en el establecimiento.");
            }
            else{
                //El personal no existe.
                throw new NotFoundException("Ubicación no encontrada. El id no corresponde a ningún miembro del personal de la facultad.");
            }

        }
        return resultados.stream().map(fila -> {
            //Se recibe lo que Hibernate manda (Geolatte)
            org.geolatte.geom.Geometry<?> geoLatteGeom = (org.geolatte.geom.Geometry<?>) fila[4];

            //Se convierte a JTS (Lo que  clase UbicacionPersonal espera)
            Geometry jtsGeom = JTS.to(geoLatteGeom);

            //Se crea el objeto
            return new UbicacionPersonalDTO(
                    (String) fila[0],
                    (String) fila[1],
                    (String) fila[2],
                    (String) fila[3],
                    jtsGeom
            );
        }).collect(Collectors.toList());
    }

    @Override
    public Personal savePersonal(PersonalRequestDTO personal) throws ArgumentNotValidException{
        //Verificar duplicado lógico ( DNI)
        if (personalRepository.existsByDni(personal.getDni())) {
            throw new ArgumentNotValidException("Ya existe una persona registrada con ese DNI");
        }
        Integer ultimoIdNumerico = personalRepository.findMaxId();
        String nuevoId;
        if (ultimoIdNumerico == null) {
            nuevoId = String.valueOf(1);
        } else {
            int numeroSiguiente =ultimoIdNumerico + 1;
            nuevoId = String.valueOf(numeroSiguiente);
        }
        Personal personalNuevo = new Personal();
        personalNuevo.setIdPersonal(nuevoId);
        personalNuevo.setNombrePersonal(personal.getNombrePersonal());
        personalNuevo.setApellidoPersonal(personal.getApellidoPersonal());
        personalNuevo.setDni(personal.getDni());
        personalNuevo.setCargoLaboral(personal.getCargoLaboral());
        return personalRepository.save(personalNuevo);
    }

    @Override
    public Personal updatePersonal(String idPersonal, PersonalRequestDTO personal) {
        Personal personalToUpdate = personalRepository.findById(idPersonal).orElseThrow(() -> new NotFoundException("Miembro del personal no encontrado"));

        if(Objects.nonNull(personal.getNombrePersonal()) && !"".equalsIgnoreCase(personal.getNombrePersonal())){
            personalToUpdate.setNombrePersonal(personal.getNombrePersonal());
        }
        if(Objects.nonNull(personal.getApellidoPersonal()) && !"".equalsIgnoreCase(personal.getApellidoPersonal())){
            personalToUpdate.setApellidoPersonal(personal.getApellidoPersonal());
        }
        if(Objects.nonNull(personal.getCargoLaboral()) && !"".equalsIgnoreCase(personal.getCargoLaboral())){
            personalToUpdate.setCargoLaboral(personal.getCargoLaboral());
        }
        if(Objects.nonNull(personal.getDni()) && !"".equalsIgnoreCase(personal.getDni())){
            personalToUpdate.setDni(personal.getDni());
        }
        return personalRepository.save(personalToUpdate);
    }

    @Override
    public void deletePersonal(String idPersonal) {
        Personal personalToDelete = personalRepository.findById(idPersonal).orElseThrow(() -> new NotFoundException("Miembro del personal no encontrado"));
        personalRepository.deleteById(idPersonal);
    }
}
