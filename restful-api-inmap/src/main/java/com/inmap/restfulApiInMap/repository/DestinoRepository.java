package com.inmap.restfulApiInMap.repository;


import com.inmap.restfulApiInMap.dto.DestinoReducidoDTO;
import com.inmap.restfulApiInMap.entity.Destino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//Interfaz del repositorio que extiende JpaRepository. Proporciona varios métodos preconfigurados para realizar operaciones CRUD en la entidad.
@Repository //Marca la interfaz como un repositorio JPA de Spring Data.
public interface DestinoRepository extends JpaRepository<Destino, String> {
    //@Query: Define una consulta JPQL personalizada o SQL nativa para una entidad.

    //JPQL
    @Query("SELECT new com.inmap.restfulApiInMap.dto.DestinoReducidoDTO( d.nombreDestino, d.geometria) " +
            "FROM Destino d WHERE d.idDestino = :id")
    List<DestinoReducidoDTO> findDestino(@Param("id") String id);

}
