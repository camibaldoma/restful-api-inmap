package com.inmap.restfulApiInMap.repository;

import com.inmap.restfulApiInMap.entity.Asignacion;
import com.inmap.restfulApiInMap.entity.Destino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//Interfaz del repositorio que extiende JpaRepository. Proporciona varios métodos preconfigurados para realizar operaciones CRUD en la entidad.
@Repository //Marca la interfaz como un repositorio JPA de Spring Data.
public interface AsignacionRepository extends JpaRepository<Asignacion, String> {
    @Query("SELECT COUNT(a) > 0 FROM Asignacion a " +
            "WHERE a.destino.idDestino = :idDestino " +
            "AND a.horario.dias = :dia " +
            "AND NOT (a.horario.horaFin <= :inicio OR a.horario.horaInicio >= :fin)")
    boolean existsChoqueDeHorario(@Param("idDestino") String idDestino,
                                  @Param("dia") String dia,
                                  @Param("inicio") String inicio,
                                  @Param("fin") String fin);
    @Query("SELECT COUNT(a) > 0 FROM Asignacion a " +
            "WHERE a.materia.codMateria = :cod_materia " +
            "AND a.horario.dias = :dia " +
            "AND NOT (a.horario.horaFin <= :inicio OR a.horario.horaInicio >= :fin)")
    boolean existsMateriaDuplicada(@Param("cod_materia") String cod_materia,
                                  @Param("dia") String dia,
                                  @Param("inicio") String inicio,
                                  @Param("fin") String fin);
    @Query("SELECT COUNT(a) > 0 FROM Asignacion a JOIN Esta e ON a.idAsignacion = e.idAsignacion " +
            "WHERE e.idPersonal = :idPersonal " +
            "AND a.horario.dias = :dia " +
            "AND NOT (a.horario.horaFin <= :inicio OR a.horario.horaInicio >= :fin)")
    boolean existsChoqueHorarioPersonal(@Param("idPersonal") String idPersonal,
                                        @Param("dia") String dia,
                                        @Param("inicio") String inicio,
                                        @Param("fin") String fin);
    @Query("SELECT a.idAsignacion FROM Asignacion a WHERE a.idAsignacion LIKE 'A%' ORDER BY CAST(SUBSTRING(a.idAsignacion, 2) AS int) DESC LIMIT 1")
    String findLastId();
}
