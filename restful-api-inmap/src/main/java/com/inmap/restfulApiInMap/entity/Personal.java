package com.inmap.restfulApiInMap.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
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
@Table(name = "personal")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Personal {
    @Id
    @Column(name = "id_personal") // Nombre exacto de la columna PK
    @NotBlank(message = "El ID no puede estar vacío")
    @NotNull(message = "El ID es obligatorio")
    private String idPersonal;

    @Column(name = "nombre_personal")
    @Size(min = 2, message = "El nombre es muy corto")
    @NotBlank(message = "El nombre no puede estar vacío")
    @NotNull(message = "El nombre es obligatorio")
    private String nombrePersonal;

    @Column(name = "apellido_personal")
    @Size(min = 2, message = "El apellido es muy corto")
    @NotBlank(message = "El apellido no puede estar vacío")
    @NotNull(message = "El apellido es obligatorio")
    private String apellidoPersonal;

    @Column(name = "cargo_laboral")
    @NotBlank(message = "El cargo laboral no puede estar vacío")
    @NotNull(message = "El cargo laboral es obligatorio")
    private String cargoLaboral;

    @Column(name = "dni")
    @NotBlank(message = "El DNI no puede estar vacío")
    @NotNull(message = "El DNI es obligatorio")
    private String dni;

    @JsonIgnore // para que no se duplique en el JSON
    private String nombre_completo;

    // Este metodo genera la columna para el JSON de forma dinámica
    @JsonProperty("nombreCompleto") // Esto hace que se incluya en el JSON
    public String getNombreCompleto() {
        // Si el SQL nativo llenó 'nombre_completo', se usa.
        // Si no (por ejemplo en un findAll común), se calcula.
        if (nombre_completo != null) {
            return nombre_completo.trim();
        }
        return (nombrePersonal != null ? nombrePersonal.trim() : "") + " " +
                (apellidoPersonal != null ? apellidoPersonal.trim() : "");
    }
}
