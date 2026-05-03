package com.inmap.restfulApiInMap.service;


import com.inmap.restfulApiInMap.dto.DestinoReducidoDTO;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.repository.DestinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DestinoServiceImplementation implements DestinoService {
    @Autowired
    DestinoRepository destinoRepository;
    @Override
    public List<Destino> obtenerTodosDestinos(){
        return destinoRepository.findAll();
    }
    @Override
    public List<DestinoReducidoDTO> findDestino(String id_destino)  throws NotFoundException {
        List<DestinoReducidoDTO> destinos = destinoRepository.findDestino(id_destino);
        if(destinos == null || destinos.isEmpty())
        {
            throw new NotFoundException("Destino no encontrado");
        }
        else {
            return destinos;
        }

    }

    @Override
    public Destino saveDestino(Destino destino) throws ArgumentNotValidException {
        if (destinoRepository.existsById(destino.getIdDestino())) {
            throw new ArgumentNotValidException("El ID ya existe, no se puede usar uno duplicado");
        }
        return destinoRepository.save(destino);
    }

    @Override
    public Destino updateDestino(String id, Destino destino)  throws NotFoundException,ArgumentNotValidException {

        Destino destinoToUpdate = destinoRepository.findById(id).orElseThrow(() -> new NotFoundException("Destino no encontrado"));
        if (destino.getIdDestino() != null && !id.equals(destino.getIdDestino())) {
            throw new ArgumentNotValidException("No está permitido cambiar el ID de un destino.");
        }
        if(Objects.nonNull(destino.getIdDestino()) && !"".equalsIgnoreCase(destino.getIdDestino())){
            //El id del destino no puede actualizarse
            //destinoToUpdate.setIdDestino(destino.getIdDestino());
        }
        if(Objects.nonNull(destino.getNombreDestino())){
            destinoToUpdate.setNombreDestino(destino.getNombreDestino());
        }
        if(Objects.nonNull(destino.getGeometria())){
            destinoToUpdate.setGeometria(destino.getGeometria());
        }
        return destinoRepository.save(destinoToUpdate);
    }

    @Override
    public void deleteDestino(String id)  throws NotFoundException {
        Destino destinoToDelete = destinoRepository.findById(id).orElseThrow(() -> new NotFoundException("Destino no encontrado"));
        destinoRepository.deleteById(id);
    }

}

