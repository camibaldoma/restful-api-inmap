package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.classes.EstaId;
import com.inmap.restfulApiInMap.classes.TieneAsociadoId;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.entity.Tiene_asociado;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.error.OverlapException;
import com.inmap.restfulApiInMap.repository.DestinoRepository;
import com.inmap.restfulApiInMap.repository.PersonalRepository;
import com.inmap.restfulApiInMap.repository.RecintoRepository;
import com.inmap.restfulApiInMap.repository.Tiene_asociadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Tiene_asociadoServiceImplementation implements Tiene_asociadoService {
    @Autowired
    Tiene_asociadoRepository tiene_asociadoRepository;
    @Autowired
    PersonalRepository personalRepository;
    @Autowired
    DestinoRepository destinoRepository;
    @Override
    public List<Tiene_asociado> obtenerTodosAsociados(){
        return tiene_asociadoRepository.findAll();
    }

    @Override
    public Tiene_asociado saveTiene_asociado(Tiene_asociado tiene) throws NotFoundException, OverlapException, ArgumentNotValidException {
        boolean existeDestino = destinoRepository.existsById(tiene.getIdDestino());
        boolean existePersonal = personalRepository.existsById(tiene.getIdPersonal());
        TieneAsociadoId id = new TieneAsociadoId(tiene.getIdPersonal(),tiene.getIdDestino());
        if (tiene_asociadoRepository.existsById(id)) {
            throw new ArgumentNotValidException("El ID ya existe, no se puede usar uno duplicado");
        }
        if(existeDestino && existePersonal){
            // Regla de Negocio: Un personal solo puede tener UN destino asociado (oficina)
            if(tiene_asociadoRepository.existsAsociacion(tiene.getIdPersonal())){
                throw new OverlapException("El personal "+ tiene.getIdPersonal() +" ya tiene asociado un destino fijo." );
            }
            else {
                return tiene_asociadoRepository.save(tiene);
            }
        }
        else {
            if(existeDestino == false && existePersonal ==true){
                throw new NotFoundException("El Destino no se encontró en la base de datos." );
            }
            else if(existeDestino == true && existePersonal ==false){
                throw new NotFoundException("El Personal no se encontró en la base de datos." );
            }
            else {
                throw new NotFoundException("El Personal o el Destino no se encontraron en la base de datos." );
            }

        }
    }


    @Override
    public void deleteTiene_asociado(String idPersonal, String idDestino) throws NotFoundException {
        TieneAsociadoId id = new TieneAsociadoId(idPersonal,idDestino);
        Tiene_asociado tieneToUpdate = tiene_asociadoRepository.findById(id).orElseThrow(() -> new NotFoundException("Dato no encontrado"));
        tiene_asociadoRepository.deleteById(id);
    }

}
