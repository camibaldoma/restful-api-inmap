package com.inmap.restfulApiInMap.repository;

import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.entity.Zonas_a_bloquear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Zonas_a_bloquearRepository extends JpaRepository<Zonas_a_bloquear, String> {
    @Query("SELECT z FROM Zonas_a_bloquear z WHERE z.bloqueado = true")
    List<Zonas_a_bloquear> findZonasBlocked();
    @Query("SELECT z FROM Zonas_a_bloquear z WHERE z.bloqueado = false")
    List<Zonas_a_bloquear> findZonasNoBlocked();
}
