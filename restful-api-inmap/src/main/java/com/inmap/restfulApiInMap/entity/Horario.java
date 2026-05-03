package com.inmap.restfulApiInMap.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//@Entity define la entidad como persistible
@Entity
// Esta anotación define contra que tabla de la base de datos la entidad se va a mapear
@Table(name = "horario") // Debe coincidir exactamente con el nombre en Postgres
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Horario {

    @Id
    @Column(name = "id_horario") // Nombre exacto de la columna PK
    @NotBlank(message = "El ID no puede estar vacío")
    @NotNull(message = "El ID es obligatorio")
    private String idHorario;

    @Column(name = "dias")
    @Size(min = 5, message = "El día es muy corto")
    @NotBlank(message = "El día no puede estar vacío")
    @NotNull(message = "El día es obligatorio")
    private String dias;

    @Column(name = "hora_inicio")
    @NotBlank(message = "La hora de inicio no puede estar vacía")
    @NotNull(message = "La hora de inicio es obligatoria")
    private String horaInicio;

    @Column(name = "hora_fin")
    @NotBlank(message = "La hora de finalización no puede estar vacía")
    @NotNull(message = "La hora de finalización es obligatoria")
    private String horaFin;
}
