package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.classes.EstaId;
import com.inmap.restfulApiInMap.entity.Asignacion;
import com.inmap.restfulApiInMap.entity.Esta;
import com.inmap.restfulApiInMap.entity.Tiene_asociado;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.error.OverlapException;
import com.inmap.restfulApiInMap.repository.AsignacionRepository;
import com.inmap.restfulApiInMap.repository.EstaRepository;
import com.inmap.restfulApiInMap.repository.PersonalRepository;
import com.inmap.restfulApiInMap.repository.Tiene_asociadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EstaServiceImplementation implements EstaService {


    @Autowired
    EstaRepository estaRepository;
    @Autowired
    AsignacionRepository asignacionRepository;
    @Autowired
    PersonalRepository personalRepository;
    @Override
    public List<Esta> obtenerTodosEsta(){
        return estaRepository.findAll();
    }

    @Override
    public Esta saveEsta(Esta esta) throws NotFoundException, OverlapException,ArgumentNotValidException {
        // Se verifica que los componentes existan en sus tablas base
        boolean existeAsignacion = asignacionRepository.existsById(esta.getIdAsignacion());
        boolean existePersonal = personalRepository.existsById(esta.getIdPersonal());
        EstaId id = new EstaId(esta.getIdAsignacion(), esta.getIdPersonal());
        if (estaRepository.existsById(id)) {
            throw new ArgumentNotValidException("El ID ya existe, no se puede usar uno duplicado");
        }
        if (existeAsignacion && existePersonal) {
            // Se obtienen los detalles de la asignación a la que se quiere vincular al docente
            Asignacion nuevaAsignacion = asignacionRepository.findById(esta.getIdAsignacion())
                    .orElseThrow(() -> new NotFoundException("Asignación no encontrada"));

            String dia = nuevaAsignacion.getHorario().getDias();
            String inicio = nuevaAsignacion.getHorario().getHoraInicio();
            String fin = nuevaAsignacion.getHorario().getHoraFin();

            // Se valida si el docente ya tiene otra clase en el mismo horario
            boolean estaOcupado = asignacionRepository.existsChoqueHorarioPersonal(
                    esta.getIdPersonal(), dia, inicio, fin);

            if (estaOcupado) {
                throw new OverlapException("El docente " + esta.getIdPersonal() +
                        " ya tiene una clase asignada los " + dia + " de " + inicio + " a " + fin);
            }

            //Si no hay choque, guardamos el vínculo
            return estaRepository.save(esta);

        } else {
            if(existeAsignacion == false && existePersonal==true)
            {
                throw new NotFoundException("La Asignación no se encontró en la base de datos.");
            } else if (existeAsignacion == true && existePersonal==false) {
                throw new NotFoundException("El Personal no se encontró en la base de datos.");
            }
            else {
                throw new NotFoundException("El Personal o la Asignación no se encontraron en la base de datos.");
            }
        }
    }

    @Override
    // Para eliminar con clave compuesta, se necesitan ambos IDs
    public void deleteEsta(String idPersonal, String idAsignacion) throws NotFoundException {
        EstaId id = new EstaId(idAsignacion, idPersonal);
        Esta estaToDelete = estaRepository.findById(id).orElseThrow(() -> new NotFoundException("Dato no encontrado"));
        estaRepository.deleteById(id);
    }
}
