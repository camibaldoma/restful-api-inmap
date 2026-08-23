package com.inmap.restfulApiInMap.service;


import com.inmap.restfulApiInMap.dto.DestinoReducidoDTO;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.repository.DestinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    //Permite retornar solo las siglas de los destinos cuyo nombre es extremadamente largo
    @Override
    public List<DestinoReducidoDTO> obtenerDestinosAcortados() throws NotFoundException
    {
        /*Los IDs correspondientes a los destinos con siglas son:
        D01 Laboratorio electrónica 1
        D02 Laboratorio electrónica 2
        D03 LIC (Laboratorio de Instrumentación y Control)
        D04 LAC (Laboratorio de Comunicaciones)
        D05 LABI (Laboratorio de Bioingeniería)
        D06 LC (Laboratorio de Componentes)
        D07 LIVRA (Laboratorio de Instrumentación Virtual y Robótica Aplicada)
        D08 LPI (Laboratorio de Procesamiento de Imágenes)
         */
        List<DestinoReducidoDTO> destinos = new ArrayList<>();
        for(int i = 1; i < 9; i++)
        {
            String id_destino = "D0" + i;
            List<DestinoReducidoDTO> destino = destinoRepository.findDestino(id_destino);
            if(destino == null || destino.isEmpty())
            {
                throw new NotFoundException("Destino no encontrado");
            }
            else {
                String newName=" ";
                switch (i) {
                    case 1:
                        newName = "Lab electrónica 1";
                        break;

                    case 2:
                        newName = "Lab electrónica 2";
                        break;

                    case 3:
                        newName = "LIC";
                        break;
                    case 4:
                        newName = "LAC";
                        break;
                    case 5:
                        newName = "LABI";
                        break;
                    case 6:
                        newName = "LC";
                        break;
                    case 7:
                        newName = "LIVRA";
                        break;
                    case 8:
                        newName = "LPI";
                        break;
                    default:
                        // Código si no coincide con ningún caso
                        break;
                }
                DestinoReducidoDTO destinoToChange= destino.get(0);
                destinoToChange.setNombreDestino(newName);
                destinos.add(destinoToChange);
            }
        }
        return destinos;
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

