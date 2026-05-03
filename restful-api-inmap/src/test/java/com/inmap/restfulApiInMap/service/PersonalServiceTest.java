package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.dto.PersonalReducidoDTO;
import com.inmap.restfulApiInMap.dto.PersonalRequestDTO;
import com.inmap.restfulApiInMap.dto.UbicacionPersonalDTO;
import com.inmap.restfulApiInMap.entity.*;

import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.repository.PersonalRepository;
import org.geolatte.geom.jts.JTS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonalServiceTest {

    @Mock
    private PersonalRepository personalRepository;

    @InjectMocks
    private PersonalServiceImplementation personalService;

    private final GeometryFactory geometryFactory = new GeometryFactory();
    private Personal docente;
    private PersonalRequestDTO docenteRequest;

    @BeforeEach
    void setUp() {
        docente = new Personal();
        docente.setIdPersonal("999");
        docente.setNombrePersonal("Zulema");
        docente.setApellidoPersonal("AAA_Prueba");
        docente.setCargoLaboral("Laboral_A");
        docente.setDni("123456789");

        docenteRequest = new PersonalRequestDTO(
                docente.getNombrePersonal(),
                docente.getApellidoPersonal(),
                docente.getCargoLaboral(),
                docente.getDni()
        );
    }
    @Test
    void findAllOrderByApellidoRetornaListadoReducido() {
        PersonalReducidoDTO primero = new PersonalReducidoDTO("Zulema", "AAA_Prueba", "Laboral_A");
        PersonalReducidoDTO segundo = new PersonalReducidoDTO("Juana", "BBB_Prueba", "Laboral_B");
        when(personalRepository.findAllOrderByApellido()).thenReturn(List.of(primero, segundo));

        List<PersonalReducidoDTO> resultado = personalService.findAllOrderByApellido();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals("Zulema AAA_Prueba", resultado.get(0).getNombreCompleto());
        assertEquals("Juana BBB_Prueba", resultado.get(1).getNombreCompleto());
    }
    @Test
    void findUbicacionCompletaNativeMapeaLaRespuesta() {
        Point puntoAula = geometryFactory.createPoint(new Coordinate(-38.0, -57.5));
        Destino destino = new Destino();
        destino.setIdDestino("D50");
        destino.setNombreDestino("Aula 5");
        destino.setGeometria(puntoAula);

        LinearRing shell = geometryFactory.createLinearRing(new Coordinate[]{
                new Coordinate(-31.41, -64.18, 0.0),
                new Coordinate(-31.41, -64.19, 0.0),
                new Coordinate(-31.42, -64.19, 0.0),
                new Coordinate(-31.42, -64.18, 0.0),
                new Coordinate(-31.41, -64.18, 0.0)
        });
        Polygon poligono = geometryFactory.createPolygon(shell);
        MultiPolygon multiPolygon = geometryFactory.createMultiPolygon(new Polygon[]{poligono});
        Recinto recinto = new Recinto();
        recinto.setIdRecinto("R50");
        recinto.setDestino(destino);
        recinto.setGeometria(multiPolygon);

        org.geolatte.geom.Geometry<?> geoLatteGeom = JTS.from(recinto.getGeometria());
        Object[] fila = new Object[]{"D50", "R50", "Aula 5", "Clase: Sistemas Operativos", geoLatteGeom};
        List<Object[]> resultadosSimulados = new ArrayList<>();
        resultadosSimulados.add(fila);

        when(personalRepository.findUbicacionCompletaNative(eq("999"), anyString(), anyString())).thenReturn(resultadosSimulados);

        List<UbicacionPersonalDTO> resultado = personalService.findUbicacionCompletaNative("999", "Lunes", "09:00:00");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("D50", resultado.get(0).getIdDestino());
        assertEquals("R50", resultado.get(0).getIdRecinto());
        assertEquals("Aula 5", resultado.get(0).getNombreDestino());
        assertEquals("Clase: Sistemas Operativos", resultado.get(0).getMotivo());
        assertNotNull(resultado.get(0).getGeometria());
    }
    @Test
    void findUbicacionCompletaNativeLanzaExcepcionSiExistePeroNoEstaPresente() {
        when(personalRepository.findUbicacionCompletaNative(eq("999"), anyString(), anyString())).thenReturn(List.of());
        when(personalRepository.existsById("999")).thenReturn(true);

        assertThrows(NotFoundException.class, () -> personalService.findUbicacionCompletaNative("999", "Lunes", "09:00:00"));
    }
    @Test
    void findUbicacionCompletaNativeLanzaExcepcionSiElIdNoExiste() {
        when(personalRepository.findUbicacionCompletaNative(eq("000"), anyString(), anyString())).thenReturn(null);
        when(personalRepository.existsById("000")).thenReturn(false);

        assertThrows(NotFoundException.class, () -> personalService.findUbicacionCompletaNative("000", "Lunes", "09:00:00"));
    }

    @Test
    void savePersonalGeneraNuevoId() {
        when(personalRepository.existsByDni("123456789")).thenReturn(false);
        when(personalRepository.findMaxId()).thenReturn(999);
        when(personalRepository.save(any(Personal.class))).thenAnswer(returnsFirstArg());

        Personal resultado = personalService.savePersonal(docenteRequest);

        assertNotNull(resultado);
        assertEquals("1000", resultado.getIdPersonal());
        assertEquals("Zulema", resultado.getNombrePersonal());
        assertEquals("AAA_Prueba", resultado.getApellidoPersonal());
    }

    @Test
    void savePersonalLanzaExcepcionSiElDniYaExiste() {
        when(personalRepository.existsByDni("123456789")).thenReturn(true);

        assertThrows(ArgumentNotValidException.class, () -> personalService.savePersonal(docenteRequest));
    }

    @Test
    void updatePersonalActualizaCamposInformados() {
        PersonalRequestDTO requestActualizado = new PersonalRequestDTO("NombreActualizado", "ApellidoActualizado", "Laboral_B", "987654321");
        when(personalRepository.findById("999")).thenReturn(Optional.of(docente));
        when(personalRepository.save(any(Personal.class))).thenAnswer(returnsFirstArg());

        Personal resultado = personalService.updatePersonal("999", requestActualizado);

        assertEquals("999", resultado.getIdPersonal());
        assertEquals("NombreActualizado", resultado.getNombrePersonal());
        assertEquals("ApellidoActualizado", resultado.getApellidoPersonal());
        assertEquals("Laboral_B", resultado.getCargoLaboral());
        assertEquals("987654321", resultado.getDni());
    }

    @Test
    void updatePersonalLanzaExcepcionSiNoExiste() {
        when(personalRepository.findById("000")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> personalService.updatePersonal("000", docenteRequest));
    }

    @Test
    void deletePersonalEliminaCuandoExiste() {
        when(personalRepository.findById("999")).thenReturn(Optional.of(docente));

        personalService.deletePersonal("999");

        verify(personalRepository).deleteById("999");
    }

    @Test
    void deletePersonalLanzaExcepcionSiNoExiste() {
        when(personalRepository.findById("000")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> personalService.deletePersonal("000"));
    }
}
