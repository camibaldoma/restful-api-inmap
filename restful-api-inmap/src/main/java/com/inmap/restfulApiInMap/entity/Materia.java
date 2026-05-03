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
@Table(name = "materia") // Debe coincidir exactamente con el nombre en Postgres
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Materia {

    @Id
    @Column(name = "cod_materia", length = 3)
    @NotBlank(message = "El código de la materia no puede estar vacío")
    @NotNull(message = "El el código de la materia es obligatorio")
    private String codMateria;

    @Column(name = "nombre_materia")
    @Size(min = 2, message = "El nombre es muy corto")
    @NotBlank(message = "El nombre no puede estar vacío")
    @NotNull(message = "El nombre es obligatorio")
    private String nombreMateria;
}
