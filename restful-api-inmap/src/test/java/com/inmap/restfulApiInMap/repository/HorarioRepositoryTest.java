package com.inmap.restfulApiInMap.repository;

import com.inmap.restfulApiInMap.entity.Horario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class HorarioRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private HorarioRepository horarioRepository;

    @Test
    @Transactional
    void findLastIdRetornaElMayorIdNumerico() {
        entityManager.persist(new Horario("H2", "Lunes", "08:00", "10:00"));
        entityManager.persist(new Horario("H10", "Martes", "10:00", "12:00"));
        entityManager.persist(new Horario("H3", "Miercoles", "12:00", "14:00"));
        entityManager.flush();

        String ultimoId = horarioRepository.findLastId();

        assertEquals("H10", ultimoId);
    }

    @Test
    @Transactional
    void existsByHoraInicioAndHoraFinAndDiasDetectaCoincidencias() {
        entityManager.persist(new Horario("H7", "Jueves", "16:00", "18:00"));
        entityManager.flush();

        assertTrue(horarioRepository.existsByHoraInicioAndHoraFinAndDias("16:00", "18:00", "Jueves"));
        assertFalse(horarioRepository.existsByHoraInicioAndHoraFinAndDias("08:00", "10:00", "Lunes"));
    }
}
