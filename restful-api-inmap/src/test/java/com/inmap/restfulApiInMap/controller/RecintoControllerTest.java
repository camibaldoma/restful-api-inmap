package com.inmap.restfulApiInMap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmap.restfulApiInMap.dto.InformacionRecintoDTO;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.error.RestResponseEntityExceptionHandler;
import com.inmap.restfulApiInMap.service.RecintoService;
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
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
class RecintoControllerTest {

    @Mock
    private RecintoService recintoService;

    @InjectMocks
    private RecintoController recintoController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private final GeometryFactory geometryFactory = new GeometryFactory();
    private Recinto recinto1;
    private InformacionRecintoDTO informacionRecinto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JtsModule());

        ReflectionTestUtils.setField(recintoController, "recintoService", recintoService);
        mockMvc = MockMvcBuilders.standaloneSetup(recintoController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

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

        informacionRecinto = new InformacionRecintoDTO(
                aula5.getIdDestino(),
                recinto1.getIdRecinto(),
                aula5.getNombreDestino(),
                "Sistemas Operativos",
                recinto1.getGeometria()
        );
    }

    @Test
    void findRecinto() throws Exception {
        String idRecinto = "R50";
        Mockito.when(recintoService.findRecinto(idRecinto)).thenReturn(List.of(recinto1));

        mockMvc.perform(get("/recintos/{id}", idRecinto))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idRecinto").value("R50"));
    }

    @Test
    void findInformation() throws Exception {
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";
        String idRecinto = "R50";
        Mockito.when(recintoService.findInformation(idRecinto, horaConsulta, diaConsulta)).thenReturn(List.of(informacionRecinto));

        mockMvc.perform(get("/informacionRecintos/{id}/{hora}/{dia}", idRecinto, horaConsulta, diaConsulta))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idRecinto").value("R50"));
    }

    @Test
    void saveRecinto() throws Exception {
        Mockito.when(recintoService.saveRecinto(recinto1)).thenReturn(recinto1);

        mockMvc.perform(post("/guardarRecinto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recinto1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idRecinto").value("R50"));
    }

    @Test
    void updateRecinto() throws Exception {
        String id = "R50";
        Point puntoAula = geometryFactory.createPoint(new Coordinate(-38.0, -57.5));
        Destino aulaNueva = new Destino();
        aulaNueva.setIdDestino("D50");
        aulaNueva.setNombreDestino("Aula Nueva Actualizada");
        aulaNueva.setGeometria(puntoAula);

        LinearRing shell = geometryFactory.createLinearRing(new Coordinate[]{
                new Coordinate(-31.41, -64.18, 0.0),
                new Coordinate(-31.41, -64.19, 0.0),
                new Coordinate(-31.42, -64.19, 0.0),
                new Coordinate(-31.42, -64.18, 0.0),
                new Coordinate(-31.41, -64.18, 0.0)
        });
        Polygon poligonoAula = geometryFactory.createPolygon(shell);
        MultiPolygon recintoMultiPoligono = geometryFactory.createMultiPolygon(new Polygon[]{poligonoAula});
        Recinto recintoActualizado = new Recinto();
        recintoActualizado.setIdRecinto("R50");
        recintoActualizado.setDestino(aulaNueva);
        recintoActualizado.setGeometria(recintoMultiPoligono);
        Mockito.when(recintoService.updateRecinto(id, recintoActualizado)).thenReturn(recintoActualizado);

        mockMvc.perform(put("/actualizarRecinto/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recintoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idRecinto").value("R50"))
                .andExpect(jsonPath("$.destino.nombreDestino").value("Aula Nueva Actualizada"));
    }

    @Test
    void deleteRecinto() throws Exception {
        String id = "R50";

        mockMvc.perform(delete("/eliminarRecinto/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(recintoService, times(1)).deleteRecinto(id);
    }
}
