package com.inmap.restfulApiInMap.repository;

import com.inmap.restfulApiInMap.dto.PersonalReducidoDTO;
import com.inmap.restfulApiInMap.entity.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest // Solo carga lo necesario para JPA
@ActiveProfiles("test")
class PersonalRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PersonalRepository personalRepository;

    private GeometryFactory geometryFactory = new GeometryFactory();
    private Personal docenteTest1;
    private Personal docenteTest2;
    @BeforeEach
    void setUp() {
        this.docenteTest1 = new Personal();
        this.docenteTest1.setIdPersonal("999");
        this.docenteTest1.setDni("123456789");
        this.docenteTest1.setCargoLaboral("Laboral_A");
        this.docenteTest1.setNombrePersonal("Zulema");
        this.docenteTest1.setApellidoPersonal("AAA_Prueba");
        entityManager.persist(docenteTest1);

        this.docenteTest2 = new Personal();
        this.docenteTest2.setIdPersonal("998");
        this.docenteTest2.setDni("023456789");
        this.docenteTest2.setCargoLaboral("Laboral_B");
        this.docenteTest2.setNombrePersonal("Juana");
        this.docenteTest2.setApellidoPersonal("BBB_Prueba");
        entityManager.persist(docenteTest2);

        // Se crea un Destino (el punto en el mapa)
        Point puntoAula = geometryFactory.createPoint(new Coordinate(-38.0, -57.5));
        Destino aula5 = new Destino();
        aula5.setIdDestino("D50");
        aula5.setNombreDestino("Aula 5");
        aula5.setGeometria(puntoAula);
        entityManager.persist(aula5);

        //Se crea un Recinto (polígono en el mapa)
        // Se crea un Polígono simple (un cuadrado pequeño) para el recinto
        // Se usan coordenadas cercanas al punto del aula
        LinearRing shell = geometryFactory.createLinearRing(new Coordinate[]{
                new Coordinate(-31.41, -64.18, 0.0),
                new Coordinate(-31.41, -64.19, 0.0),
                new Coordinate(-31.42, -64.19, 0.0),
                new Coordinate(-31.42, -64.18, 0.0),
                new Coordinate(-31.41, -64.18, 0.0) // El último siempre igual al primero
        });
        Polygon poligonoAula = geometryFactory.createPolygon(shell);
        MultiPolygon recintoMultiPoligono = geometryFactory.createMultiPolygon(new Polygon[]{poligonoAula});
        Recinto recinto1 = new Recinto();
        recinto1.setIdRecinto("R50");
        recinto1.setDestino(aula5);
        recinto1.setBloqueado(false);
        recinto1.setGeometria(recintoMultiPoligono);
        entityManager.persist(recinto1);

        // Se crea el Horario (Lunes de 08:00 a 10:00)
        Horario horarioLunes = new Horario();
        horarioLunes.setIdHorario("H200");
        horarioLunes.setDias("Lunes");
        horarioLunes.setHoraInicio("08:00");
        horarioLunes.setHoraFin("10:00");
        entityManager.persist(horarioLunes);

        // Se crea la Materia
        Materia materia = new Materia();
        materia.setCodMateria("M1T");
        materia.setNombreMateria("Sistemas Operativos");
        entityManager.persist(materia);

        // Se crea la Asignación
        Asignacion asignacion = new Asignacion();
        asignacion.setIdAsignacion("A200");
        asignacion.setDestino(aula5);
        asignacion.setHorario(horarioLunes);
        asignacion.setMateria(materia);
        entityManager.persist(asignacion);

        //Se crea Esta
        Esta esta = new Esta();
        esta.setIdPersonal(docenteTest1.getIdPersonal());
        esta.setIdAsignacion(asignacion.getIdAsignacion());
        entityManager.persist(esta);

        entityManager.flush();
    }

    @Test
    @Transactional
    void findAllOrderByApellido() {
        //Act
        List<PersonalReducidoDTO> resultados = personalRepository.findAllOrderByApellido();
        //Assert
        assertThat(resultados).isNotNull();
        String nombreRecibido1 = resultados.get(0).getNombreCompleto();
        String nombreRecibido2 = resultados.get(1).getNombreCompleto();
        assertThat(nombreRecibido1).isEqualTo("Zulema AAA_Prueba");
        assertThat(nombreRecibido2).isEqualTo("Juana BBB_Prueba");
    }

    @Test
    @Transactional
    void findUbicacionCompletaNative() {

        // Se prueba buscar a las 09:00 (en medio de la clase)
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";

        List<Object[]> resultados = personalRepository.findUbicacionCompletaNative(docenteTest1.getIdPersonal(), diaConsulta, horaConsulta);

        Object[] fila = resultados.get(0);

        // Se verifican las columnas según el orden del mapeo en el Service:
        assertThat(fila[0]).isEqualTo("D50");
        assertThat(fila[1]).isEqualTo("R50");
        assertThat(fila[2]).isEqualTo("Aula 5");
        assertThat(fila[4]).isNotNull(); // Esto es la geometría (GeoLatte)

    }
}
