package com.inmap.restfulApiInMap.service;


import com.inmap.restfulApiInMap.dto.HorarioRequestDTO;
import com.inmap.restfulApiInMap.entity.Horario;
import com.inmap.restfulApiInMap.entity.Materia;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.repository.HorarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HorarioServiceTest {


    @Mock
    private HorarioRepository horarioRepository;

    @InjectMocks
    private HorarioServiceImplementation horarioService;

    private Horario horarioPersistido;
    private HorarioRequestDTO horarioRequestDTO;

    @BeforeEach
    void setUp() {
        horarioPersistido = new Horario();
        horarioPersistido.setIdHorario("H3");
        horarioPersistido.setDias("Lunes");
        horarioPersistido.setHoraInicio("08:00:00");
        horarioPersistido.setHoraFin("10:00:00");

        horarioRequestDTO = new HorarioRequestDTO("Lunes", "08:00:00", "10:00:00");
    }
    @Test
    void saveHorarioGeneraNuevoIdYGuarda() {
        when(horarioRepository.existsByHoraInicioAndHoraFinAndDias("08:00:00", "10:00:00", "Lunes")).thenReturn(false);
        when(horarioRepository.findLastId()).thenReturn("H3");
        when(horarioRepository.save(any(Horario.class))).thenAnswer(returnsFirstArg());

        Horario resultado = horarioService.saveHorario(horarioRequestDTO);

        assertNotNull(resultado);
        assertEquals("H4", resultado.getIdHorario());
        assertEquals("Lunes", resultado.getDias());
        assertEquals("08:00:00", resultado.getHoraInicio());
        assertEquals("10:00:00", resultado.getHoraFin());
    }
    @Test
    void saveHorarioLanzaExcepcionSiLaFranjaYaExiste() {
        when(horarioRepository.existsByHoraInicioAndHoraFinAndDias("08:00:00", "10:00:00", "Lunes")).thenReturn(true);

        assertThrows(ArgumentNotValidException.class, () -> horarioService.saveHorario(horarioRequestDTO));
        verify(horarioRepository, never()).save(any(Horario.class));
    }
    @Test
    void updateHorarioActualizaCamposInformados() {
        HorarioRequestDTO horarioActualizado = new HorarioRequestDTO("Martes", "09:00:00", "11:00:00");
        when(horarioRepository.findById("H3")).thenReturn(Optional.of(horarioPersistido));
        when(horarioRepository.save(any(Horario.class))).thenAnswer(returnsFirstArg());

        Horario resultado = horarioService.updateHorario("H3", horarioActualizado);

        assertEquals("H3", resultado.getIdHorario());
        assertEquals("Martes", resultado.getDias());
        assertEquals("09:00:00", resultado.getHoraInicio());
        assertEquals("11:00:00", resultado.getHoraFin());
    }

    @Test
    void updateHorarioLanzaExcepcionSiNoExiste() {
        when(horarioRepository.findById("H99")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> horarioService.updateHorario("H99", horarioRequestDTO));
    }

    @Test
    void deleteHorarioEliminaCuandoExiste() {
        when(horarioRepository.findById("H3")).thenReturn(Optional.of(horarioPersistido));

        horarioService.deleteHorario("H3");

        verify(horarioRepository).deleteById("H3");
    }

    @Test
    void deleteHorarioLanzaExcepcionSiNoExiste() {
        when(horarioRepository.findById("H99")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> horarioService.deleteHorario("H99"));
    }
}
