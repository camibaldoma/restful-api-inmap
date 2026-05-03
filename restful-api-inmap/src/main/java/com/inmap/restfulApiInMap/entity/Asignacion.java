package com.inmap.restfulApiInMap.entity;


import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity define la entidad como persistible
@Entity
//Esta anotación define contra que tabla de la base de datos la entidad se va a mapear
@Table(name = "asignacion")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Asignacion {

    @Id //Marca el atributo como la clave primaria de la entidad
    @Column(name = "id_asignacion") // Permite personalizar el mapeo entre el atributo de la clase y la columna en la tabla de la base de datos. Nombre exacto de la columna
    @NotBlank(message = "El ID no puede estar vacío")
    @NotNull(message = "El ID es obligatorio")
    private String idAsignacion;

    @ManyToOne //Se encarga de generar una relación de muchos a uno
    @JoinColumn(name = "id_destino") //Sirve en JPA para hacer referencia a la columna que es clave externa en la tabla y que se encarga de definir la relación
    @Valid  //Activa la validación en cascada
    @NotNull(message = "El objeto destino es obligatorio")
    private Destino destino;

    @ManyToOne
    @JoinColumn(name = "cod_materia")
    @Valid  //Activa la validación en cascada
    @NotNull(message = "El objeto materia es obligatorio")
    private Materia materia;

    @ManyToOne
    @JoinColumn(name = "id_horario")
    @Valid  //Activa la validación en cascada
    @NotNull(message = "El objeto horario es obligatorio")
    private Horario horario;
}
