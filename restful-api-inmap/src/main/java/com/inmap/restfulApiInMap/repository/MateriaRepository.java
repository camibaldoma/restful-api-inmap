package com.inmap.restfulApiInMap.repository;

import com.inmap.restfulApiInMap.entity.Materia;

import com.inmap.restfulApiInMap.entity.Recinto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

//Interfaz del repositorio que extiende JpaRepository. Proporciona varios métodos preconfigurados para realizar operaciones CRUD en la entidad.
@Repository //Marca la interfaz como un repositorio JPA de Spring Data.
public interface MateriaRepository extends JpaRepository<Materia, String> {
    //@Query: Define una consulta JPQL personalizada o SQL nativa para una entidad.
    //Si se devuelve el recinto, también se puede obtener la información del destino
    //Con JPQL
    @Query("SELECT r FROM Recinto r " +
            "JOIN r.destino d " +
            "JOIN Asignacion a ON d = a.destino " + // Comparamos objetos, no IDs
            "JOIN a.materia m " +
            "JOIN a.horario h " +
            "WHERE h.dias = :dia " +
            "AND :hora BETWEEN h.horaInicio AND h.horaFin " +
            "AND m.codMateria = :id")
    List<Recinto> findMateria(@Param("id") String id, @Param("hora") String hora, @Param("dia") String dia);
}
