package com.inmap.restfulApiInMap.repository;

import com.inmap.restfulApiInMap.dto.PersonalReducidoDTO;
import com.inmap.restfulApiInMap.entity.Personal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//Interfaz del repositorio que extiende JpaRepository. Proporciona varios métodos preconfigurados para realizar operaciones CRUD en la entidad.
@Repository //Marca la interfaz como un repositorio JPA de Spring Data.
public interface PersonalRepository extends JpaRepository<Personal, String> {
    //@Query: Define una consulta JPQL personalizada o SQL nativa para una entidad.
    //Consultas nativas de SQL
    @Query("SELECT NEW com.inmap.restfulApiInMap.dto.PersonalReducidoDTO(p.nombrePersonal, p.apellidoPersonal,p.cargoLaboral) " +
            "FROM Personal p ORDER BY p.apellidoPersonal")
    List<PersonalReducidoDTO> findAllOrderByApellido();

    //No se puede usar JPQL por la UNION
    @Query(value = "SELECT d.id_destino AS idDestino, r.id_recinto AS idRecinto, " +
            "d.nombre_destino AS nombreDestino, 'Clase: ' || m.nombre_materia AS motivo, " +
            "d.geom AS geometria " +
            "FROM recinto r " +
            "JOIN destino d ON r.id_destino = d.id_destino " +
            "JOIN asignacion a ON d.id_destino = a.id_destino " +
            "JOIN materia m ON a.cod_materia = m.cod_materia " +
            "JOIN horario h ON a.id_horario = h.id_horario " +
            "JOIN esta e ON a.id_asignacion = e.id_asignacion " +
            "JOIN personal p ON e.id_personal = p.id_personal " +
            "WHERE p.id_personal = :id " +
            "AND h.dias = :dia AND :hora BETWEEN h.hora_inicio AND h.hora_fin " +
            "UNION " +
            "SELECT d.id_destino AS idDestino, r.id_recinto AS idRecinto, " +
            "d.nombre_destino AS nombreDestino, 'Oficina/Asociado' AS motivo, " +
            "d.geom AS geometria " +
            "FROM recinto r " +
            "JOIN destino d ON r.id_destino = d.id_destino " +
            "JOIN tiene_asociado t ON d.id_destino = t.id_destino " +
            "JOIN personal p ON t.id_personal = p.id_personal " +
            "WHERE p.id_personal = :id " +
            "ORDER BY motivo", nativeQuery = true)
    List<Object[]> findUbicacionCompletaNative(@Param("id") String id,
                                               @Param("dia") String dia,
                                               @Param("hora") String hora);
    @Query("SELECT MAX(CAST(p.idPersonal AS int)) FROM Personal p")
    Integer findMaxId();
    Boolean existsByDni(String Dni);
}



