package com.inmap.restfulApiInMap.repository;

import com.inmap.restfulApiInMap.entity.Horario;
import com.inmap.restfulApiInMap.entity.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


//Interfaz del repositorio que extiende JpaRepository. Proporciona varios métodos preconfigurados para realizar operaciones CRUD en la entidad.
@Repository //Marca la interfaz como un repositorio JPA de Spring Data.
public interface HorarioRepository extends JpaRepository<Horario, String> {
    @Query("SELECT u.idHorario FROM Horario u WHERE u.idHorario LIKE 'H%' ORDER BY CAST(SUBSTRING(u.idHorario, 2) AS int) DESC LIMIT 1")
    String findLastId();
    boolean existsByHoraInicioAndHoraFinAndDias(String horaInicio, String horaFin, String dias);
}
