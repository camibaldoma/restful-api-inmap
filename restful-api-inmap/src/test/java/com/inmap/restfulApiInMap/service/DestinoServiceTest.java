package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.dto.DestinoReducidoDTO;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.repository.DestinoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DestinoServiceTest {

    @InjectMocks
    private DestinoServiceImplementation destinoService;

    @Mock
    private DestinoRepository destinoRepository;

    private final GeometryFactory geometryFactory = new GeometryFactory();
    private Destino aulaNueva;
    private Destino aulaTest;
    private Destino aulaTest2;

    @BeforeEach
    void setUp() {
        Point puntoAula = geometryFactory.createPoint(new Coordinate(-38.0, -57.5));
        aulaTest = new Destino();
        aulaTest.setIdDestino("D100");
        aulaTest.setNombreDestino("Aula Test");
        aulaTest.setGeometria(puntoAula);

        Point puntoAula1 = geometryFactory.createPoint(new Coordinate(-40.0, -57.5));
        aulaNueva = new Destino();
        aulaNueva.setIdDestino("D200");
        aulaNueva.setNombreDestino("Aula Nueva");
        aulaNueva.setGeometria(puntoAula1);

        Point puntoAula2 = geometryFactory.createPoint(new Coordinate(-50.0, -57.5));
        aulaTest2 = new Destino();
        aulaTest2.setIdDestino("D101");
        aulaTest2.setNombreDestino("Aula Test2");
        aulaTest2.setGeometria(puntoAula2);
    }

    @Test
    void findDestinoSI() throws NotFoundException {
        String id = "D100";
        DestinoReducidoDTO destinoReducido = new DestinoReducidoDTO(aulaTest.getNombreDestino(), aulaTest.getGeometria());
        Mockito.when(destinoRepository.findDestino(id)).thenReturn(List.of(destinoReducido));

        List<DestinoReducidoDTO> resultados = destinoService.findDestino(id);

        assertNotNull(resultados);
        assertFalse(resultados.isEmpty(), "La lista no deberia estar vacia");
        DestinoReducidoDTO resultado = resultados.get(0);
        assertEquals("Aula Test", resultado.getNombreDestino());
    }

    @Test
    void findDestinoNO() {
        String id = "D999";
        Mockito.when(destinoRepository.findDestino(id)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> destinoService.findDestino(id));
    }

    @Test
    void saveDestinoSI() throws Exception {
        Mockito.when(destinoRepository.existsById(aulaNueva.getIdDestino())).thenReturn(false);
        Mockito.when(destinoRepository.save(aulaNueva)).thenReturn(aulaNueva);

        Destino save = destinoService.saveDestino(aulaNueva);

        assertNotNull(save, "El objeto guardado no deberia ser nulo.");
        assertEquals("D200", save.getIdDestino());
        assertEquals("Aula Nueva", save.getNombreDestino());
    }

    @Test
    void saveDestinoNO() {
        Point puntoAula = geometryFactory.createPoint(new Coordinate(-38.0, -57.5));
        Destino aulaIdExistente = new Destino();
        aulaIdExistente.setIdDestino("D300");
        aulaIdExistente.setNombreDestino("Aula IdExistente");
        aulaIdExistente.setGeometria(puntoAula);
        Mockito.when(destinoRepository.existsById(aulaIdExistente.getIdDestino())).thenReturn(true);

        assertThrows(ArgumentNotValidException.class, () -> destinoService.saveDestino(aulaIdExistente));
    }

    @Test
    void updateDestinoSI() throws Exception {
        String id = "D101";
        Mockito.when(destinoRepository.findById(id)).thenReturn(Optional.of(aulaTest2));
        Mockito.when(destinoRepository.save(aulaTest2)).thenReturn(aulaTest2);

        Destino update = destinoService.updateDestino(id, aulaTest2);

        assertNotNull(update, "El objeto actualizado no deberia ser nulo.");
        assertEquals("D101", update.getIdDestino());
        assertEquals("Aula Test2", update.getNombreDestino());
    }

    @Test
    void updateDestinoNO() {
        String id = "D999";
        Mockito.when(destinoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> destinoService.updateDestino(id, aulaTest2));
    }

    @Test
    void updateDestinoNO_1() {
        Mockito.when(destinoRepository.findById(aulaTest2.getIdDestino())).thenReturn(Optional.of(aulaTest2));

        assertThrows(ArgumentNotValidException.class, () -> destinoService.updateDestino(aulaTest2.getIdDestino(), aulaNueva));
    }

    @Test
    void deleteDestinoSI() throws Exception {
        String id = "D100";
        Mockito.when(destinoRepository.findById(id)).thenReturn(Optional.of(aulaTest));

        destinoService.deleteDestino(id);

        Mockito.verify(destinoRepository, Mockito.times(1)).deleteById(id);
    }
}
