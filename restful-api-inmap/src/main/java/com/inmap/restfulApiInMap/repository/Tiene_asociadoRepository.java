package com.inmap.restfulApiInMap.repository;

import com.inmap.restfulApiInMap.classes.TieneAsociadoId;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.entity.Tiene_asociado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


//Interfaz del repositorio que extiende JpaRepository. Proporciona varios métodos preconfigurados para realizar operaciones CRUD en la entidad.
@Repository //Marca la interfaz como un repositorio JPA de Spring Data.
public interface Tiene_asociadoRepository extends JpaRepository<Tiene_asociado, TieneAsociadoId> {
    @Query("SELECT COUNT(t) > 0 FROM Tiene_asociado t WHERE t.idPersonal = :idPersonal")
    boolean existsAsociacion(@Param("idPersonal") String idPersonal);
}
