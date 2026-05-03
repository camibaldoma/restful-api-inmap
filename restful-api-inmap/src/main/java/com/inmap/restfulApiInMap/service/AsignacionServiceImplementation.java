package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.dto.AsignacionRequestDTO;
import com.inmap.restfulApiInMap.entity.Asignacion;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.error.OverlapException;
import com.inmap.restfulApiInMap.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service //Se usa para construir una clase de Servicio que habitualmente se conecta a varios repositorios y agrupa su funcionalidad
public class AsignacionServiceImplementation implements AsignacionService {
    @Autowired //inyecta una instancia de la ...Repository en la clase, lo que permite llamar a sus métodos.
    AsignacionRepository asignacionRepository;
    @Autowired
    MateriaRepository materiaRepository;
    @Autowired
    DestinoRepository destinoRepository;
    @Autowired
    HorarioRepository horarioRepository;
    @Autowired
    RecintoRepository recintoRepository;
    @Override
    public List<Asignacion> obtenerTodasAsignaciones(){
        return asignacionRepository.findAll();
    }

    @Override
    public Boolean existsChoqueDeHorario(String idDestino, String dia, String horaInicio, String horaFin, String idActual) {
        return asignacionRepository.existsChoqueDeHorario(idDestino, dia, horaInicio, horaFin);
    }

    @Override
    public Asignacion saveAsignacion(AsignacionRequestDTO asignacion) throws ArgumentNotValidException, OverlapException, NotFoundException {
        // Se verifica que los componentes existan
        boolean existeMateria = materiaRepository.existsById(asignacion.getMateria().getCodMateria());
        boolean existeDestino = destinoRepository.existsById(asignacion.getDestino().getIdDestino());
        boolean existeHorario = horarioRepository.existsById(asignacion.getHorario().getIdHorario());

        if (existeMateria && existeDestino && existeHorario) {
            String dia = asignacion.getHorario().getDias();
            String inicio = asignacion.getHorario().getHoraInicio();
            String fin = asignacion.getHorario().getHoraFin();
            String idDestino = asignacion.getDestino().getIdDestino();
            String codMateria = asignacion.getMateria().getCodMateria();
            boolean estaOcupado = asignacionRepository.existsChoqueDeHorario(idDestino, dia, inicio, fin);
            //Lo de materia se va a dejar, porque a una materia se le pueden asignar más de un espacio si la cantidad de alumnos es muy grande
            boolean materiaDuplicada = asignacionRepository.existsMateriaDuplicada(codMateria,dia,inicio,fin);
            if (estaOcupado) {
                throw new OverlapException("El destino " + idDestino + " ya tiene una asignación en el horario " + inicio + " - " + fin);
            }

            String ultimoId = asignacionRepository.findLastId();
            String nuevoId;
            if (ultimoId == null) {
                nuevoId = "A1";
            } else {
                // se extrae el número después de la 'A'
                int numeroSiguiente = Integer.parseInt(ultimoId.substring(1)) + 1;
                nuevoId = "A" + numeroSiguiente;
            }
            Asignacion asignacion2 = new Asignacion();
            asignacion2.setIdAsignacion(nuevoId);
            asignacion2.setMateria(asignacion.getMateria());
            asignacion2.setDestino(asignacion.getDestino());
            asignacion2.setHorario(asignacion.getHorario());
            // Si pasó todas las validaciones, se guarda
            return asignacionRepository.save(asignacion2);
        } else {
            // Se lanza una excepción
            if(existeMateria == false && existeDestino == true && existeHorario == true)
            {
                throw new NotFoundException("La Materia no encontró en la base de datos.");
            } else if (existeDestino == false && existeMateria == true && existeHorario == true)
            {
                throw new NotFoundException("El Destino no encontró en la base de datos.");
            }
            else if (existeHorario == false &&  existeDestino == true && existeMateria == true)
            {
                throw new NotFoundException("El Horario no encontró en la base de datos.");
            }
            else {
                throw new NotFoundException("El Destino, la Materia o el Horario no encontraron en la base de datos.");
            }

        }
    }

    @Override
    public Asignacion updateAsignacion(String id, AsignacionRequestDTO asignacion) throws ArgumentNotValidException, NotFoundException, OverlapException {
        Asignacion asignacionToUpdate = asignacionRepository.findById(id).orElseThrow(() -> new NotFoundException("Asignación no encontrada"));
        // Se verifica que los componentes existan
        boolean existeMateria = materiaRepository.existsById(asignacion.getMateria().getCodMateria());
        boolean existeDestino = destinoRepository.existsById(asignacion.getDestino().getIdDestino());
        boolean existeHorario = horarioRepository.existsById(asignacion.getHorario().getIdHorario());

        if (existeMateria && existeDestino && existeHorario)
        {
            String dia = asignacion.getHorario().getDias();
            String inicio = asignacion.getHorario().getHoraInicio();
            String fin = asignacion.getHorario().getHoraFin();
            String idDestino = asignacion.getDestino().getIdDestino();
            String codMateria = asignacion.getMateria().getCodMateria();
            boolean estaOcupado = asignacionRepository.existsChoqueDeHorario(idDestino, dia, inicio, fin);
            //Lo de materia se va a dejar, porque a una materia se le pueden asignar más de un espacio si la cantidad de alumnos es muy grande
            boolean materiaDuplicada = asignacionRepository.existsMateriaDuplicada(codMateria,dia,inicio,fin);
            if (estaOcupado) {
                throw new OverlapException("El destino " + idDestino + " ya tiene una asignación en el horario " + inicio + " - " + fin);
            }
            else
            {

                if(Objects.nonNull(asignacion.getDestino()))
                {
                    asignacionToUpdate.setDestino(asignacion.getDestino());
                }
                if(Objects.nonNull(asignacion.getMateria()))
                {
                    asignacionToUpdate.setMateria(asignacion.getMateria());
                }
                if(Objects.nonNull(asignacion.getHorario()))
                {
                    asignacionToUpdate.setHorario(asignacion.getHorario());
                }
                return asignacionRepository.save(asignacionToUpdate);
            }
        } else {
            // Se lanza una excepción
            if(existeMateria == false && existeDestino == true && existeHorario == true)
            {
                throw new NotFoundException("La Materia no encontró en la base de datos.");
            } else if (existeDestino == false && existeMateria == true && existeHorario == true)
            {
                throw new NotFoundException("El Destino no encontró en la base de datos.");
            }
            else if (existeHorario == false &&  existeDestino == true && existeMateria == true)
            {
                throw new NotFoundException("El Horario no encontró en la base de datos.");
            }
            else {
                throw new NotFoundException("El Destino, la Materia o el Horario no encontraron en la base de datos.");
            }

        }
    }

    @Override
    public void deleteAsignacion(String id) throws NotFoundException {
        Asignacion asignacionToDelete = asignacionRepository.findById(id).orElseThrow(() -> new NotFoundException("Asignación no encontrada"));
        asignacionRepository.deleteById(id);
    }
}
