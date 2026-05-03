package com.inmap.restfulApiInMap.repository;


import com.inmap.restfulApiInMap.dto.InformacionRecintoDTO;
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
class RecintoRepositoryTest {


    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RecintoRepository recintoRepository;

    private GeometryFactory geometryFactory = new GeometryFactory();
    private Recinto recinto1;
    private Recinto recinto2;

    @BeforeEach
    void setUp() {
        // Se crea un Destino (el punto en el mapa)
        Point puntoAula = geometryFactory.createPoint(new Coordinate(-38.0, -57.5));
        Destino aula5 = new Destino();
        aula5.setIdDestino("D50");
        aula5.setNombreDestino("Aula 5");
        aula5.setGeometria(puntoAula);
        entityManager.persist(aula5);

        Point puntoAula1 = geometryFactory.createPoint(new Coordinate(-40.0, -57.5));
        Destino aulaNueva = new Destino();
        aulaNueva.setIdDestino("D60");
        aulaNueva.setNombreDestino("Aula Nueva");
        aulaNueva.setGeometria(puntoAula1);
        entityManager.persist(aulaNueva);

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
        this.recinto1 = new Recinto();
        this.recinto1.setIdRecinto("R50");
        this.recinto1.setDestino(aula5);
        this.recinto1.setGeometria(recintoMultiPoligono);
        this.recinto1.setBloqueado(false);
        entityManager.persist(recinto1);

        this.recinto2 = new Recinto();
        this.recinto2.setIdRecinto("R60");
        this.recinto2.setDestino(aulaNueva);
        this.recinto2.setGeometria(recintoMultiPoligono);
        this.recinto2.setBloqueado(true);
        entityManager.persist(recinto2);

        // Se crea al Personal
        Personal docente = new Personal();
        docente.setIdPersonal("80");
        docente.setNombrePersonal("Cami");
        docente.setApellidoPersonal("Baldomá");
        docente.setDni("22222222");
        docente.setCargoLaboral("Cargo laboral");
        entityManager.persist(docente);

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
        esta.setIdPersonal(docente.getIdPersonal());
        esta.setIdAsignacion(asignacion.getIdAsignacion());
        entityManager.persist(esta);



        entityManager.flush(); // Sincroniza con la DB
    }

    @Test
    @Transactional
    void findRecinto() {

        List<Recinto> resultado = recintoRepository.findRecinto("R50");
        assertThat(resultado.get(0).getIdRecinto()).isEqualTo("R50");
    }

    @Test
    @Transactional
    void findInformation() {

        // Se prueba buscar a las 09:00 (en medio de la clase)
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";

        List<InformacionRecintoDTO> resultado = recintoRepository.findInformation(recinto1.getIdRecinto(),horaConsulta, diaConsulta);

        assertThat(resultado).isNotNull();
        InformacionRecintoDTO informacionRecinto = resultado.get(0);

        assertThat(informacionRecinto.getIdDestino()).isEqualTo("D50");
        assertThat(informacionRecinto.getIdRecinto()).isEqualTo("R50");
        assertThat(informacionRecinto.getNombreDestino()).isEqualTo("Aula 5");
        assertThat(informacionRecinto.getNombreMateria()).isEqualTo("Sistemas Operativos");
    }
    @Test
    @Transactional
    void existsDestinoInRecinto(){
        String destinoExistente = "D50";
        String recintoInexistente = "R100";
        Boolean exists= recintoRepository.existsDestinoInRecinto(destinoExistente,recintoInexistente);
        assertThat(exists.booleanValue()).isTrue();
    }
    @Test
    @Transactional
    void findRecintoBlocked(){
        List<Recinto> resultados = recintoRepository.findRecintoBlocked();
        Recinto recinto =  resultados.get(0);
        assertThat(recinto.getIdRecinto()).isEqualTo("R60");
    }
}
