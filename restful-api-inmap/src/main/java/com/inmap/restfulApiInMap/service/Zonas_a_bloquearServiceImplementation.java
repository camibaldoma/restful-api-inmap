package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.entity.Zonas_a_bloquear;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.ForbiddenException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.repository.Zonas_a_bloquearRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Zonas_a_bloquearServiceImplementation implements Zonas_a_bloquearService {

    @Autowired
    Zonas_a_bloquearRepository zonas_a_bloquearRepository;

    @Override
    public List<Zonas_a_bloquear> obtenerTodasZonas() {
         return  zonas_a_bloquearRepository.findAll();
    }

    @Override
    public List<Zonas_a_bloquear> findZonasBlocked()
    {
        return zonas_a_bloquearRepository.findZonasBlocked();
    }
    @Override
    public List<Zonas_a_bloquear> findZonasNoBlocked()
    {
        return zonas_a_bloquearRepository.findZonasNoBlocked();
    }

    @Override
    public Zonas_a_bloquear updateStateZona(String id, Boolean state) throws NotFoundException, ArgumentNotValidException, ForbiddenException {
        Zonas_a_bloquear zonaToUpdate = zonas_a_bloquearRepository.findById(id).orElseThrow(() -> new NotFoundException("Zona no encontrada"));
        if (zonaToUpdate.getBloqueo_permanente()==true) {
             throw new ForbiddenException("Operación denegada: La zona " + id + " tiene un bloqueo estructural permanente.");
        }
        zonaToUpdate.setBloqueado(state);
        return zonas_a_bloquearRepository.save(zonaToUpdate);
    }

    @Override
    public List<Zonas_a_bloquear> updateSeveralStateZonas(List<String> ids, Boolean state) throws NotFoundException, ForbiddenException {
        List<Zonas_a_bloquear> zonas = new ArrayList<>();
        for(String  idZona : ids){
            Zonas_a_bloquear zonaToUpdate = zonas_a_bloquearRepository.findById(idZona).orElseThrow(() -> new NotFoundException("Zona con id: "+idZona+" no encontrada"));
            if (zonaToUpdate.getBloqueo_permanente()==true) {
                throw new ForbiddenException("Operación denegada: La zona " + idZona + " tiene un bloqueo estructural permanente.");
            }
            zonaToUpdate.setBloqueado(state);
            zonas_a_bloquearRepository.save(zonaToUpdate);
            zonas.add(zonaToUpdate);
        }
        return zonas;
    }
}
