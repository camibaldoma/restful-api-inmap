package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.dto.InformacionRecintoDTO;
import com.inmap.restfulApiInMap.entity.Asignacion;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.entity.Esta;
import com.inmap.restfulApiInMap.entity.Horario;
import com.inmap.restfulApiInMap.entity.Materia;
import com.inmap.restfulApiInMap.entity.Personal;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.error.OverlapException;
import com.inmap.restfulApiInMap.repository.DestinoRepository;
import com.inmap.restfulApiInMap.repository.RecintoRepository;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RecintoServiceTest {

    @InjectMocks
    private RecintoServiceImplementation recintoService;

    @Mock
    private RecintoRepository recintoRepository;

    @Mock
    private DestinoRepository destinoRepository;

    private final GeometryFactory geometryFactory = new GeometryFactory();
    private Recinto recinto1;
    private Recinto recinto;
    private InformacionRecintoDTO informacionRecinto;

    @BeforeEach
    void setUp() {
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
        recinto1.setBloqueado(false);
        recinto1.setGeometria(recintoMultiPoligono);

        Personal docente = new Personal();
        docente.setIdPersonal("80");
        docente.setNombrePersonal("Cami");
        docente.setApellidoPersonal("Baldoma");

        Horario horarioLunes = new Horario();
        horarioLunes.setIdHorario("H200");
        horarioLunes.setDias("Lunes");
        horarioLunes.setHoraInicio("08:00");
        horarioLunes.setHoraFin("10:00");

        Materia materia = new Materia();
        materia.setCodMateria("M1T");
        materia.setNombreMateria("Sistemas Operativos");

        Asignacion asignacion = new Asignacion();
        asignacion.setIdAsignacion("A200");
        asignacion.setDestino(aula5);
        asignacion.setHorario(horarioLunes);
        asignacion.setMateria(materia);

        Esta esta = new Esta();
        esta.setIdPersonal(docente.getIdPersonal());
        esta.setIdAsignacion(asignacion.getIdAsignacion());

        informacionRecinto = new InformacionRecintoDTO(
                aula5.getIdDestino(),
                recinto1.getIdRecinto(),
                aula5.getNombreDestino(),
                materia.getNombreMateria(),
                recinto1.getGeometria()
        );

        Destino aulaNueva = new Destino();
        aulaNueva.setIdDestino("D60");
        aulaNueva.setNombreDestino("Aula Nueva");
        aulaNueva.setGeometria(puntoAula);

        recinto = new Recinto();
        recinto.setIdRecinto("R70");
        recinto.setDestino(aulaNueva);
        recinto.setGeometria(recintoMultiPoligono);
        recinto.setBloqueado(false);
    }

    @Test
    void findRecintoSI() throws NotFoundException {
        String id = "R50";
        Mockito.when(recintoRepository.findRecinto(id)).thenReturn(List.of(recinto1));

        List<Recinto> resultado = recintoService.findRecinto(id);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty(), "La lista no deberia estar vacia");
        Recinto recintoResultado = resultado.get(0);
        assertEquals(id, recintoResultado.getIdRecinto());
    }

    @Test
    void findRecintoNO() {
        String id = "D999";
        Mockito.when(recintoRepository.findRecinto(id)).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> recintoService.findRecinto(id));
    }

    @Test
    void findInformationSI() throws NotFoundException {
        String id = "R50";
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";
        Mockito.when(recintoRepository.findInformation(id, horaConsulta, diaConsulta)).thenReturn(List.of(informacionRecinto));
        Mockito.when(recintoRepository.findById(id)).thenReturn(Optional.of(recinto1));

        List<InformacionRecintoDTO> resultado = recintoService.findInformation(id, horaConsulta, diaConsulta);

        assertThat(resultado).isNotNull();
        InformacionRecintoDTO info = resultado.get(0);
        assertThat(info.getIdDestino()).isEqualTo("D50");
        assertThat(info.getIdRecinto()).isEqualTo("R50");
        assertThat(info.getNombreDestino()).isEqualTo("Aula 5");
        assertThat(info.getNombreMateria()).isEqualTo("Sistemas Operativos");
    }

    @Test
    void findInformationNO() {
        String id = "R50";
        String horaConsulta = "13:00:00";
        String diaConsulta = "Lunes";
        Mockito.when(recintoRepository.findInformation(id, horaConsulta, diaConsulta)).thenReturn(Collections.emptyList());
        Mockito.when(recintoRepository.findById(id)).thenReturn(Optional.of(recinto1));

        assertThrows(NotFoundException.class, () -> recintoService.findInformation(id, horaConsulta, diaConsulta));
    }

    @Test
    void findInformationNO_1() {
        String id = "R999";
        String horaConsulta = "13:00:00";
        String diaConsulta = "Lunes";
        Mockito.when(recintoRepository.findInformation(id, horaConsulta, diaConsulta)).thenReturn(Collections.emptyList());
        Mockito.when(recintoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> recintoService.findInformation(id, horaConsulta, diaConsulta));
    }

    @Test
    void saveRecintoSI() throws Exception {
        Mockito.when(recintoRepository.existsById(recinto1.getIdRecinto())).thenReturn(false);
        Mockito.when(destinoRepository.existsById(recinto1.getDestino().getIdDestino())).thenReturn(false);
        Mockito.when(recintoRepository.existsDestinoInRecinto(recinto1.getDestino().getIdDestino(), recinto1.getIdRecinto())).thenReturn(false);
        Mockito.when(recintoRepository.save(any(Recinto.class))).thenAnswer(returnsFirstArg());

        Recinto save = recintoService.saveRecinto(recinto1);

        assertNotNull(save, "El objeto guardado no deberia ser nulo.");
        assertEquals("R50", save.getIdRecinto());
        assertEquals("Aula 5", save.getDestino().getNombreDestino());
    }

    @Test
    void saveRecintoNO() {
        Point puntoAula = geometryFactory.createPoint(new Coordinate(-38.0, -57.5));
        Destino aulaNueva = new Destino();
        aulaNueva.setIdDestino("D60");
        aulaNueva.setNombreDestino("Aula Nueva");
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
        Recinto recintoIdExistente = new Recinto();
        recintoIdExistente.setIdRecinto("R60");
        recintoIdExistente.setDestino(aulaNueva);
        recintoIdExistente.setGeometria(recintoMultiPoligono);
        recintoIdExistente.setBloqueado(false);
        Mockito.when(recintoRepository.existsById(recintoIdExistente.getIdRecinto())).thenReturn(true);

        assertThrows(ArgumentNotValidException.class, () -> recintoService.saveRecinto(recintoIdExistente));
    }

    @Test
    void saveRecintoNO_1() {
        Mockito.when(recintoRepository.existsById(recinto.getIdRecinto())).thenReturn(false);
        Mockito.when(destinoRepository.existsById(recinto.getDestino().getIdDestino())).thenReturn(true);
        Mockito.when(recintoRepository.existsDestinoInRecinto(recinto.getDestino().getIdDestino(), recinto.getIdRecinto())).thenReturn(true);

        assertThrows(OverlapException.class, () -> recintoService.saveRecinto(recinto));
    }

    @Test
    void updateRecintoSI() throws Exception {
        Point puntoAula = geometryFactory.createPoint(new Coordinate(-38.0, -57.5));
        Destino aulaNueva = new Destino();
        aulaNueva.setIdDestino("D60");
        aulaNueva.setNombreDestino("Aula ACTUALIZADA");
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
        recintoActualizado.setIdRecinto("R60");
        recintoActualizado.setDestino(aulaNueva);
        recintoActualizado.setGeometria(recintoMultiPoligono);
        recintoActualizado.setBloqueado(false);
        Mockito.when(recintoRepository.findById("R60")).thenReturn(Optional.of(recinto1));
        Mockito.when(destinoRepository.existsById(recintoActualizado.getDestino().getIdDestino())).thenReturn(false);
        Mockito.when(recintoRepository.existsDestinoInRecinto(recintoActualizado.getDestino().getIdDestino(), recintoActualizado.getIdRecinto())).thenReturn(false);
        Mockito.when(recintoRepository.save(any(Recinto.class))).thenAnswer(returnsFirstArg());

        Recinto update = recintoService.updateRecinto("R60", recintoActualizado);

        assertNotNull(update, "El objeto actualizado no deberia ser nulo.");
        assertEquals("R50", update.getIdRecinto());
        assertEquals("Aula ACTUALIZADA", update.getDestino().getNombreDestino());
    }

    @Test
    void updateRecintoNO() {
        String id = "R999";
        Mockito.when(recintoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> recintoService.updateRecinto(id, recinto1));
    }

    @Test
    void updateRecintoNO_1() {
        Mockito.when(recintoRepository.findById(recinto1.getIdRecinto())).thenReturn(Optional.of(recinto1));

        assertThrows(ArgumentNotValidException.class, () -> recintoService.updateRecinto(recinto1.getIdRecinto(), recinto));
    }

    @Test
    void updateRecintoNO_2() {
        Mockito.when(recintoRepository.findById(recinto.getIdRecinto())).thenReturn(Optional.of(recinto));
        Mockito.when(destinoRepository.existsById(recinto.getDestino().getIdDestino())).thenReturn(true);
        Mockito.when(recintoRepository.existsDestinoInRecinto(recinto.getDestino().getIdDestino(), recinto.getIdRecinto())).thenReturn(true);

        assertThrows(OverlapException.class, () -> recintoService.updateRecinto(recinto.getIdRecinto(), recinto));
    }

    @Test
    void deleteRecintoSI() throws Exception {
        String id = "R60";
        Mockito.when(recintoRepository.findById(id)).thenReturn(Optional.of(recinto1));

        recintoService.deleteRecinto(id);

        verify(recintoRepository, times(1)).deleteById(id);
    }

    @Test
    void updateStateRecintoSI() throws Exception {
        String id = "R60";
        Boolean newState = true;
        Mockito.when(recintoRepository.findById(id)).thenReturn(Optional.of(recinto1));
        Mockito.when(recintoRepository.save(any(Recinto.class))).thenAnswer(returnsFirstArg());

        recinto1.setBloqueado(newState);
        Recinto resultado = recintoService.updateStateRecinto(id, newState);

        assertNotNull(resultado);
        assertEquals(true, resultado.getBloqueado(), "El estado deberia haber cambiado a true");
        verify(recintoRepository, times(1)).save(any(Recinto.class));
    }

    @Test
    void updateStateRecintoNo() {
        String id = "R999";
        Boolean newState = true;
        Mockito.when(recintoRepository.findById(id)).thenReturn(Optional.empty());
        recinto1.setBloqueado(newState);

        assertThrows(NotFoundException.class, () -> recintoService.updateStateRecinto(id, newState));
    }
}
