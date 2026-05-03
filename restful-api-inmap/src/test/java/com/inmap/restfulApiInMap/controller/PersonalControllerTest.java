package com.inmap.restfulApiInMap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmap.restfulApiInMap.dto.PersonalReducidoDTO;
import com.inmap.restfulApiInMap.dto.PersonalRequestDTO;
import com.inmap.restfulApiInMap.dto.UbicacionPersonalDTO;
import com.inmap.restfulApiInMap.entity.Personal;
import com.inmap.restfulApiInMap.error.RestResponseEntityExceptionHandler;
import com.inmap.restfulApiInMap.service.PersonalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.n52.jackson.datatype.jts.JtsModule;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PersonalControllerTest {

    @Mock
    private PersonalService personalService;

    @InjectMocks
    private PersonalController personalController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Personal personal;
    private PersonalRequestDTO personalRequestDTO;
    private UbicacionPersonalDTO ubicacion;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(personalController, "personalService", personalService);
        mockMvc = MockMvcBuilders.standaloneSetup(personalController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JtsModule());

        personal = new Personal();
        personal.setIdPersonal("999");
        personal.setNombrePersonal("Zulema");
        personal.setApellidoPersonal("AAA_Prueba");
        personal.setCargoLaboral("Laboral_A");
        personal.setDni("123456789");

        personalRequestDTO = new PersonalRequestDTO("Zulema", "AAA_Prueba", "Laboral_A", "123456789");

        GeometryFactory geometryFactory = new GeometryFactory();
        LinearRing shell = geometryFactory.createLinearRing(new Coordinate[]{
                new Coordinate(-31.41, -64.18, 0.0),
                new Coordinate(-31.41, -64.19, 0.0),
                new Coordinate(-31.42, -64.19, 0.0),
                new Coordinate(-31.42, -64.18, 0.0),
                new Coordinate(-31.41, -64.18, 0.0)
        });
        Polygon poligono = geometryFactory.createPolygon(shell);
        MultiPolygon multiPolygon = geometryFactory.createMultiPolygon(new Polygon[]{poligono});
        ubicacion = new UbicacionPersonalDTO("D50", "R50", "Aula 5", "Clase: Sistemas Operativos", multiPolygon);
    }

    @Test
    void findAllOrderByApellidoRetornaListado() throws Exception {
        PersonalReducidoDTO primero = new PersonalReducidoDTO("Zulema", "AAA_Prueba", "Laboral_A");
        PersonalReducidoDTO segundo = new PersonalReducidoDTO("Juana", "BBB_Prueba", "Laboral_B");
        when(personalService.findAllOrderByApellido()).thenReturn(List.of(primero, segundo));

        mockMvc.perform(get("/personal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreCompleto").value("Zulema AAA_Prueba"))
                .andExpect(jsonPath("$[1].nombreCompleto").value("Juana BBB_Prueba"));
    }

    @Test
    void findUbicacionCompletaNativeRetornaUbicacion() throws Exception {
        when(personalService.findUbicacionCompletaNative(eq("999"), anyString(), anyString())).thenReturn(List.of(ubicacion));

        mockMvc.perform(get("/personal/{id}/{hora}/{dia}", "999", "09:00:00", "Lunes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idDestino").value("D50"))
                .andExpect(jsonPath("$[0].idRecinto").value("R50"))
                .andExpect(jsonPath("$[0].nombreDestino").value("Aula 5"))
                .andExpect(jsonPath("$[0].motivo").value("Clase: Sistemas Operativos"));
    }

    @Test
    void savePersonalRetornaCreated() throws Exception {
        when(personalService.savePersonal(personalRequestDTO)).thenReturn(personal);

        mockMvc.perform(post("/guardarPersonal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personalRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPersonal").value("999"))
                .andExpect(jsonPath("$.nombrePersonal").value("Zulema"))
                .andExpect(jsonPath("$.apellidoPersonal").value("AAA_Prueba"));
    }

    @Test
    void updatePersonalRetornaOk() throws Exception {
        Personal actualizado = new Personal("999", "NombreTest", "ApellidoTest", "Laboral_B", "987654321", null);
        PersonalRequestDTO request = new PersonalRequestDTO("NombreTest", "ApellidoTest", "Laboral_B", "987654321");
        when(personalService.updatePersonal("999", request)).thenReturn(actualizado);

        mockMvc.perform(put("/actualizarPersonal/{id}", "999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPersonal").value("999"))
                .andExpect(jsonPath("$.nombrePersonal").value("NombreTest"))
                .andExpect(jsonPath("$.apellidoPersonal").value("ApellidoTest"));
    }

    @Test
    void deletePersonalInvocaAlService() throws Exception {
        mockMvc.perform(delete("/eliminarPersonal/{id}", "999"))
                .andExpect(status().isOk());

        verify(personalService, times(1)).deletePersonal("999");
    }
}
