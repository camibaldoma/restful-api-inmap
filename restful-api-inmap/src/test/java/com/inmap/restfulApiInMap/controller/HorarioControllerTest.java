package com.inmap.restfulApiInMap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmap.restfulApiInMap.dto.HorarioRequestDTO;
import com.inmap.restfulApiInMap.entity.Horario;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.RestResponseEntityExceptionHandler;
import com.inmap.restfulApiInMap.service.HorarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class HorarioControllerTest {

    @Mock
    private HorarioService horarioService;

    @InjectMocks
    private HorarioController horarioController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(horarioController, "horarioService", horarioService);
        mockMvc = MockMvcBuilders.standaloneSetup(horarioController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void saveHorarioRetornaCreated() throws Exception {
        HorarioRequestDTO request = new HorarioRequestDTO("Lunes", "08:00", "10:00");
        Horario response = new Horario("H1", "Lunes", "08:00", "10:00");
        when(horarioService.saveHorario(request)).thenReturn(response);

        mockMvc.perform(post("/guardarHorario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idHorario").value("H1"))
                .andExpect(jsonPath("$.dias").value("Lunes"));
    }

    @Test
    void saveHorarioRetornaBadRequestSiElServiceFalla() throws Exception {
        HorarioRequestDTO request = new HorarioRequestDTO("Lunes", "08:00", "10:00");
        when(horarioService.saveHorario(request)).thenThrow(new ArgumentNotValidException("Horario duplicado"));

        mockMvc.perform(post("/guardarHorario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Horario duplicado"));
    }

    @Test
    void updateHorarioRetornaOk() throws Exception {
        String id = "H1";
        HorarioRequestDTO request = new HorarioRequestDTO("Martes", "09:00", "11:00");
        Horario response = new Horario(id, "Martes", "09:00", "11:00");
        when(horarioService.updateHorario(id, request)).thenReturn(response);

        mockMvc.perform(put("/actualizarHorario/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idHorario").value("H1"))
                .andExpect(jsonPath("$.dias").value("Martes"));
    }

    @Test
    void deleteHorarioInvocaAlService() throws Exception {
        String id = "H1";

        mockMvc.perform(delete("/eliminarHorario/{id}", id))
                .andExpect(status().isOk());

        verify(horarioService, times(1)).deleteHorario(id);
    }
}
