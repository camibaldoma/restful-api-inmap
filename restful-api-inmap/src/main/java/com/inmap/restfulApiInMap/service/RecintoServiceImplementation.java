package com.inmap.restfulApiInMap.service;


import com.inmap.restfulApiInMap.dto.InformacionRecintoDTO;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.error.OverlapException;
import com.inmap.restfulApiInMap.repository.DestinoRepository;
import com.inmap.restfulApiInMap.repository.RecintoRepository;
import org.hibernate.sql.ast.tree.expression.Over;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
public class RecintoServiceImplementation implements RecintoService {
    @Autowired
    RecintoRepository recintoRepository;
    @Autowired
    DestinoRepository destinoRepository;
    @Override
    public List<Recinto> obtenerTodosRecintos(){
        return recintoRepository.findAll();
    }
    @Override
    public List<Recinto> findRecinto(String id) throws NotFoundException {
        List<Recinto> recintos = recintoRepository.findRecinto(id);
        if(recintos == null || recintos.isEmpty())
        {
            throw new NotFoundException("Recinto no encontrado");
        }
        return recintos;
    }
    @Override
    public List<Recinto> findRecintoBlocked()
    {
        return recintoRepository.findRecintoBlocked();
    }
    @Override
    public List<InformacionRecintoDTO> findInformation(String id, String hora, String dia ) throws NotFoundException,OverlapException {
        List<InformacionRecintoDTO> lista = recintoRepository.findInformation(id, hora, dia);
        Recinto recinto = recintoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recinto no encontrado"));
        if (lista.isEmpty() || lista == null)
        {
            //Si no se encontró información puede ser por dos motivos:
            //No hay información en ese momento del recinto porque está desocupado. Puede estar desocupado porque esta bloqueado o porque no hay clases en ese momento.
            if(recinto.getBloqueado()==false)
            {
                throw new NotFoundException("No se encontró información para el recinto: " + id + ". En este momento se encuentra desocupado.");
            }
            else
            {
                throw new NotFoundException("En este momento el recinto " + id + " se encuentra bloqueado. No se permite su uso para ninguna actividad.");
            }
        }
        //Si se devuelve información del recinto, en teoría es porque está ocupado por una clase y, por lo tanto, no está bloqueado.
        //Sin embargo, se verifica igualmente
        else {
            if(recinto.getBloqueado()==true)
            {
                throw new OverlapException("El recinto: " + id + " no debería tener información de ocupación, dado que se encuentra bloqueado para su uso. Es probable que la información se encuentre desactualizada.");
            }
        }
        return lista;
    }

    @Override
    public Recinto saveRecinto(Recinto recinto) throws ArgumentNotValidException, OverlapException {
        if (recintoRepository.existsById(recinto.getIdRecinto())) {
            throw new ArgumentNotValidException("El ID ya existe, no se puede usar uno duplicado");
        }
        Destino destino = recinto.getDestino();
        boolean existsDestinoInRecinto = recintoRepository.existsDestinoInRecinto(destino.getIdDestino(),recinto.getIdRecinto());
        //Se debe verificar que si el destino ya existe, no esté asociado a ningún recinto
        if(destinoRepository.existsById(destino.getIdDestino()) && existsDestinoInRecinto) {
            throw new OverlapException("El Destino ya se encuentra asociado a un Recinto.");
        }
        return recintoRepository.save(recinto);
    }

    @Override
    public Recinto updateRecinto(String id, Recinto recinto) throws NotFoundException {
        Recinto recintoToUpdate = recintoRepository.findById(id).orElseThrow(() -> new NotFoundException("Recinto no encontrado"));
        if (recinto.getIdRecinto() != null && !id.equals(recinto.getIdRecinto())) {
            throw new ArgumentNotValidException("No está permitido cambiar el ID de un recinto.");
        }
        Destino destino = recinto.getDestino();
        boolean existsDestinoInRecinto = recintoRepository.existsDestinoInRecinto(destino.getIdDestino(),recinto.getIdRecinto());
        //Se debe verificar que si el destino ya existe, no esté asociado a ningún recinto
        if(destinoRepository.existsById(destino.getIdDestino()) && existsDestinoInRecinto) {
            throw new OverlapException("El Destino ya se encuentra asociado a un Recinto.");
        }
        if (Objects.nonNull(recinto.getIdRecinto()) && !"".equalsIgnoreCase(recintoToUpdate.getIdRecinto())) {
            //El id del recinto no puede actualizarse
            //recintoToUpdate.setIdRecinto(recinto.getIdRecinto());
        }
        if (Objects.nonNull(recinto.getDestino())) {
            recintoToUpdate.setDestino(recinto.getDestino());
        }
        if (Objects.nonNull(recinto.getGeometria())) {
            recintoToUpdate.setGeometria(recinto.getGeometria());
        }
        if (Objects.nonNull(recinto.getBloqueado())) {
            recintoToUpdate.setBloqueado(recinto.getBloqueado());
        }
        return recintoRepository.save(recintoToUpdate);
    }
    @Override
    public void deleteRecinto(String id) throws NotFoundException {
        Recinto recintoToDelete = recintoRepository.findById(id).orElseThrow(() -> new NotFoundException("Recinto no encontrado"));;
        recintoRepository.deleteById(id);
    }
    @Override
    public Recinto updateStateRecinto(String id, Boolean state) throws NotFoundException,ArgumentNotValidException
    {
        Recinto recintoToUpdate = recintoRepository.findById(id).orElseThrow(() -> new NotFoundException("Recinto no encontrado"));
        recintoToUpdate.setBloqueado(state);
        return recintoRepository.save(recintoToUpdate);
    }
}
