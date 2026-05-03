package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Asignacion;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.entity.Esta;
import com.inmap.restfulApiInMap.entity.Horario;
import com.inmap.restfulApiInMap.entity.Materia;
import com.inmap.restfulApiInMap.entity.Personal;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.repository.MateriaRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MateriaServiceTest {

    @InjectMocks
    MateriaServiceImplementation materiaService;

    @Mock
    MateriaRepository materiaRepository;

    private final GeometryFactory geometryFactory = new GeometryFactory();
    private Materia materia;
    private Materia materiaTest;
    private Recinto recinto1;

    @BeforeEach
    void setUp() {
        Personal docenteTest = new Personal();
        docenteTest.setIdPersonal("999");
        docenteTest.setNombrePersonal("Camila");
        docenteTest.setApellidoPersonal("Baldoma");

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

        Horario horarioLunes = new Horario();
        horarioLunes.setIdHorario("H200");
        horarioLunes.setDias("Lunes");
        horarioLunes.setHoraInicio("08:00");
        horarioLunes.setHoraFin("10:00");

        materia = new Materia();
        materia.setCodMateria("M1T");
        materia.setNombreMateria("Sistemas Operativos");

        Asignacion asignacion = new Asignacion();
        asignacion.setIdAsignacion("A200");
        asignacion.setDestino(aula5);
        asignacion.setHorario(horarioLunes);
        asignacion.setMateria(materia);

        Esta esta = new Esta();
        esta.setIdPersonal(docenteTest.getIdPersonal());
        esta.setIdAsignacion(asignacion.getIdAsignacion());

        materiaTest = new Materia();
        materiaTest.setCodMateria("M9P");
        materiaTest.setNombreMateria("Materia test");
    }

    @Test
    void findMateriaSI() {
        String id = "M1T";
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";
        Mockito.when(materiaRepository.findMateria(id, horaConsulta, diaConsulta)).thenReturn(List.of(recinto1));

        List<Recinto> resultados = materiaService.findMateria(id, horaConsulta, diaConsulta);

        Recinto resultado = resultados.get(0);
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty(), "La lista no deberia estar vacia");
        assertEquals("R50", resultado.getIdRecinto());
        assertEquals("Aula 5", resultado.getDestino().getNombreDestino());
    }

    @Test
    void findMateriaNO() {
        String idInexistente = "M9F";
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";
        Mockito.when(materiaRepository.findMateria(idInexistente, horaConsulta, diaConsulta)).thenReturn(null);
        Mockito.when(materiaRepository.existsById(idInexistente)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> materiaService.findMateria(idInexistente, horaConsulta, diaConsulta));
    }

    @Test
    void findMateriaNO_1() {
        String idExistente = "M9P";
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";
        Mockito.when(materiaRepository.findMateria(idExistente, horaConsulta, diaConsulta)).thenReturn(null);
        Mockito.when(materiaRepository.existsById(idExistente)).thenReturn(true);

        assertThrows(NotFoundException.class, () -> materiaService.findMateria(idExistente, horaConsulta, diaConsulta));
    }

    @Test
    void saveMateriaSI() throws Exception {
        Mockito.when(materiaRepository.existsById(materia.getCodMateria())).thenReturn(false);
        Mockito.when(materiaRepository.save(any(Materia.class))).thenAnswer(returnsFirstArg());

        Materia save = materiaService.saveMateria(materia);

        assertNotNull(save, "El objeto guardado no deberia ser nulo.");
        assertEquals("M1T", save.getCodMateria());
        assertEquals("Sistemas Operativos", save.getNombreMateria());
    }

    @Test
    void saveMateriaNO() {
        Mockito.when(materiaRepository.existsById(materiaTest.getCodMateria())).thenReturn(true);

        assertThrows(ArgumentNotValidException.class, () -> materiaService.saveMateria(materiaTest));
    }

    @Test
    void updateMateriaSI() throws Exception {
        String cod = "M1T";
        Materia materiaActualizada = new Materia();
        materiaActualizada.setCodMateria(cod);
        materiaActualizada.setNombreMateria("Materia test");
        Mockito.when(materiaRepository.findById(cod)).thenReturn(Optional.of(materia));
        Mockito.when(materiaRepository.save(any(Materia.class))).thenAnswer(returnsFirstArg());

        Materia update = materiaService.updateMateria(cod, materiaActualizada);

        assertNotNull(update, "El objeto actualizado no deberia ser nulo.");
        assertEquals("M1T", update.getCodMateria());
        assertEquals("Materia test", update.getNombreMateria());
    }

    @Test
    void updateMateriaNO() {
        String idInexistente = "M9F";
        Mockito.when(materiaRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> materiaService.updateMateria(idInexistente, materia));
    }

    @Test
    void updateMateriaNO_1() {
        String idExistente = "M9P";
        Mockito.when(materiaRepository.findById(idExistente)).thenReturn(Optional.of(materiaTest));

        assertThrows(ArgumentNotValidException.class, () -> materiaService.updateMateria(idExistente, materia));
    }

    @Test
    void deleteMateriaSI() throws Exception {
        String id = "M1T";
        Mockito.when(materiaRepository.findById(id)).thenReturn(Optional.of(materia));

        materiaService.deleteMateria(id);

        verify(materiaRepository, times(1)).deleteById(id);
    }
}
