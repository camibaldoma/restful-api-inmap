package com.inmap.restfulApiInMap.repository;

import com.inmap.restfulApiInMap.classes.EstaId;
import com.inmap.restfulApiInMap.entity.Esta;
import com.inmap.restfulApiInMap.entity.Tiene_asociado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


//Interfaz del repositorio que extiende JpaRepository. Proporciona varios métodos preconfigurados para realizar operaciones CRUD en la entidad.
@Repository //Marca la interfaz como un repositorio JPA de Spring Data.
public interface EstaRepository extends JpaRepository<Esta, EstaId> {
    @Query("SELECT e FROM Esta e WHERE e.idPersonal = :idPersonal")
    List<Esta> findByPersonal(@Param("idPersonal") String idPersonal);

}
