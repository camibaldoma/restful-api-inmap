package com.inmap.restfulApiInMap.repository;


import com.inmap.restfulApiInMap.dto.DestinoReducidoDTO;
import com.inmap.restfulApiInMap.entity.Destino;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest // Solo carga lo necesario para JPA
@ActiveProfiles("test")
class DestinoRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DestinoRepository destinoRepository;

    private GeometryFactory geometryFactory = new GeometryFactory();

    @Test
    @Transactional
    void findDestino() {
        // Se crea un Destino (el punto en el mapa)
        Point puntoAula = geometryFactory.createPoint(new Coordinate(-38.0, -57.5));
        Destino aula5 = new Destino();
        aula5.setIdDestino("D50");
        aula5.setNombreDestino("Aula 5");
        aula5.setGeometria(puntoAula);
        entityManager.persist(aula5);

        entityManager.flush();

        List<DestinoReducidoDTO> resultados = destinoRepository.findDestino(aula5.getIdDestino());

        DestinoReducidoDTO destino = resultados.get(0);

        assertThat(destino.getNombreDestino()).isEqualTo("Aula 5");
        assertThat(destino.getGeometria()).isNotNull();
    }
}
