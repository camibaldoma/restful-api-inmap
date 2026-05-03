package com.inmap.restfulApiInMap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.entity.Materia;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.error.RestResponseEntityExceptionHandler;
import com.inmap.restfulApiInMap.service.MateriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.n52.jackson.datatype.jts.JtsModule;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MateriaControllerTest {

    @Mock
    private MateriaService materiaService;

    @InjectMocks
    private MateriaController materiaController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private final GeometryFactory geometryFactory = new GeometryFactory();
    private Recinto recinto1;
    private Materia materia;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(materiaController, "materiaService", materiaService);
        mockMvc = MockMvcBuilders.standaloneSetup(materiaController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JtsModule());

        Point puntoAula = geometryFactory.createPoint(new Coordinate(-38.0, -57.5));
        Destino aula5 = new Destino();
        aula5.setIdDestino("D50");
        aula5.setNombreDestino("Aula 5");
        aula5.setGeometria(puntoAula);

        LinearRing shell = geometryFactory.createLinearRing(new Coordinate[]{
                new Coordinate(-31.41, -64.18, 0.0),
                new Coordinate(-31.41, -64.19, 0.0),
                new Coordinate(-31.42, -64.19, 0.0),
                new Coordinate(-31.42, -64.18, 0.0),
                new Coordinate(-31.41, -64.18, 0.0)
        });
        Polygon poligonoAula = geometryFactory.createPolygon(shell);
        MultiPolygon recintoMultiPoligono = geometryFactory.createMultiPolygon(new Polygon[]{poligonoAula});
        recinto1 = new Recinto();
        recinto1.setIdRecinto("R50");
        recinto1.setDestino(aula5);
        recinto1.setGeometria(recintoMultiPoligono);

        materia = new Materia();
        materia.setCodMateria("M1T");
        materia.setNombreMateria("Sistemas Operativos");
    }

    @Test
    void findMateria() throws Exception {
        String idMateria = "M1T";
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";
        Mockito.when(materiaService.findMateria(idMateria, horaConsulta, diaConsulta)).thenReturn(List.of(recinto1));

        mockMvc.perform(get("/materia/{id}/{hora}/{dia}", idMateria, horaConsulta, diaConsulta))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idRecinto").value("R50"));
    }

    @Test
    void saveMateria() throws Exception {
        Mockito.when(materiaService.saveMateria(materia)).thenReturn(materia);

        mockMvc.perform(post("/guardarMateria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(materia)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codMateria").value("M1T"))
                .andExpect(jsonPath("$.nombreMateria").value("Sistemas Operativos"));
    }

    @Test
    void updateMateria() throws Exception {
        String id = "M1T";
        Materia materiaTest = new Materia();
        materiaTest.setCodMateria(id);
        materiaTest.setNombreMateria("Materia Test");
        Mockito.when(materiaService.updateMateria(id, materiaTest)).thenReturn(materiaTest);

        mockMvc.perform(put("/actualizarMateria/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(materiaTest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codMateria").value("M1T"))
                .andExpect(jsonPath("$.nombreMateria").value("Materia Test"));
    }

    @Test
    void deleteMateria() throws Exception {
        String id = "M1T";

        mockMvc.perform(delete("/eliminarMateria/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(materiaService, times(1)).deleteMateria(id);
    }
}
