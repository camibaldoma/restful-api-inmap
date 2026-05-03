package com.inmap.restfulApiInMap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmap.restfulApiInMap.dto.DestinoReducidoDTO;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.error.RestResponseEntityExceptionHandler;
import com.inmap.restfulApiInMap.service.DestinoService;
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
class DestinoControllerTest {

    @Mock
    private DestinoService destinoService;

    @InjectMocks
    private DestinoController destinoController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private final GeometryFactory geometryFactory = new GeometryFactory();
    private DestinoReducidoDTO destinoReducido;
    private Destino aulaNueva;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JtsModule());

        ReflectionTestUtils.setField(destinoController, "destinoService", destinoService);
        mockMvc = MockMvcBuilders.standaloneSetup(destinoController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        Point puntoAula = geometryFactory.createPoint(new Coordinate(-38.0, -57.5));
        Destino aulaTest = new Destino();
        aulaTest.setIdDestino("D100");
        aulaTest.setNombreDestino("Aula Test");
        aulaTest.setGeometria(puntoAula);

        Point puntoAula1 = geometryFactory.createPoint(new Coordinate(-40.0, -57.5));
        aulaNueva = new Destino();
        aulaNueva.setIdDestino("D200");
        aulaNueva.setNombreDestino("Aula Nueva");
        aulaNueva.setGeometria(puntoAula1);

        destinoReducido = new DestinoReducidoDTO(aulaTest.getNombreDestino(), aulaTest.getGeometria());
    }

    @Test
    void findDestino() throws Exception {
        String id = "D100";
        Mockito.when(destinoService.findDestino(id)).thenReturn(List.of(destinoReducido));

        mockMvc.perform(get("/destinos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreDestino").value("Aula Test"));
    }

    @Test
    void saveDestino() throws Exception {
        Mockito.when(destinoService.saveDestino(aulaNueva)).thenReturn(aulaNueva);

        mockMvc.perform(post("/guardarDestino")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aulaNueva)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idDestino").value("D200"))
                .andExpect(jsonPath("$.nombreDestino").value("Aula Nueva"));
    }

    @Test
    void updateDestino() throws Exception {
        String id = "D200";
        Point puntoAula1 = geometryFactory.createPoint(new Coordinate(-40.0, -57.5));
        Destino aulaNuevaActualizada = new Destino();
        aulaNuevaActualizada.setIdDestino("D200");
        aulaNuevaActualizada.setNombreDestino("Aula Nueva Actualizada");
        aulaNuevaActualizada.setGeometria(puntoAula1);
        Mockito.when(destinoService.updateDestino(id, aulaNuevaActualizada)).thenReturn(aulaNuevaActualizada);

        mockMvc.perform(put("/actualizarDestino/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aulaNuevaActualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDestino").value("D200"))
                .andExpect(jsonPath("$.nombreDestino").value("Aula Nueva Actualizada"));
    }

    @Test
    void deleteDestino() throws Exception {
        String id = "D200";

        mockMvc.perform(delete("/eliminarDestino/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(destinoService, times(1)).deleteDestino(id);
    }
}
