package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.dto.HorarioRequestDTO;
import com.inmap.restfulApiInMap.entity.Horario;
import com.inmap.restfulApiInMap.entity.Materia;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.repository.HorarioRepository;
import com.inmap.restfulApiInMap.repository.MateriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class HorarioServiceImplementation implements HorarioService {
    @Autowired
    HorarioRepository horarioRepository;
    @Override
    public List<Horario> obtenerTodosHorarios(){
        return horarioRepository.findAll();
    }

    @Override
    public Horario saveHorario(HorarioRequestDTO horario) {
        //Verificar duplicado lógico de franja horaria
        if (horarioRepository.existsByHoraInicioAndHoraFinAndDias(horario.getHoraInicio(), horario.getHoraFin(), horario.getDias())) {
            throw new ArgumentNotValidException("Ya existe un horario registrado en esa franja horaria y día.");
        }
        String ultimoId = horarioRepository.findLastId();
        String nuevoId;
        if (ultimoId == null) {
            nuevoId = "H1";
        } else {
            // se extrae el número después de la 'H'
            int numeroSiguiente = Integer.parseInt(ultimoId.substring(1)) + 1;
            nuevoId = "H" + numeroSiguiente;
        }
        Horario horario2 = new Horario();
        horario2.setHoraInicio(horario.getHoraInicio());
        horario2.setHoraFin(horario.getHoraFin());
        horario2.setDias(horario.getDias());
        horario2.setIdHorario(nuevoId);
        return horarioRepository.save(horario2);
    }

    @Override
    public Horario updateHorario(String id, HorarioRequestDTO horario) {
        Horario horarioToUpdate = horarioRepository.findById(id).orElseThrow(() -> new NotFoundException("Horario no encontrado"));

        if(Objects.nonNull(horario.getDias()) && !"".equalsIgnoreCase(horario.getDias())){
            horarioToUpdate.setDias(horario.getDias());
        }
        if(Objects.nonNull(horario.getHoraInicio()) && !"".equalsIgnoreCase(horario.getHoraInicio())){
            horarioToUpdate.setHoraInicio(horario.getHoraInicio());
        }
        if(Objects.nonNull(horario.getHoraFin()) && !"".equalsIgnoreCase(horario.getHoraFin())){
            horarioToUpdate.setHoraFin(horario.getHoraFin());
        }
        return horarioRepository.save(horarioToUpdate);
    }

    @Override
    public void deleteHorario(String id) {
        Horario horarioToDelete = horarioRepository.findById(id).orElseThrow(() -> new NotFoundException("Horario no encontrado"));
        horarioRepository.deleteById(id);
    }

}
