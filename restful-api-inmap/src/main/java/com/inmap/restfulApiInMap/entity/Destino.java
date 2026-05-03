package com.inmap.restfulApiInMap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inmap.restfulApiInMap.classes.GeoJsonHelper;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point; // Importante para PostGIS

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Entity define la entidad como persistible
@Entity
// Esta anotación define contra que tabla de la base de datos la entidad se va a mapear
@Table(name = "destino")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Destino {
    @Id //Marca el atributo como la clave primaria de la entidad
    @Column(name = "id_destino") // Permite personalizar el mapeo entre el atributo de la clase y la columna en la tabla de la base de datos. Nombre exacto de la columna
    @NotBlank(message = "El ID no puede estar vacío")
    @NotNull(message = "El ID es obligatorio")
    private String idDestino;

    @Column(name = "nombre_destino")
    @Size(min = 2, message = "El nombre es muy corto")
    private String nombreDestino;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //solo se ignore al enviar datos (serialización) pero se permita al recibir (deserialización)
    @JdbcTypeCode(org.hibernate.type.SqlTypes.GEOMETRY) //Usada para especificar manualmente el tipo JDBC exacto para un atributo de entidad cuando Hibernate no puede inferirlo automáticamente
    @Column(name = "geom")
    @NotNull(message = "La geometría es obligatoria")
    private Geometry geometria;

    @JsonProperty("geometria") //Se usa en clases de modelo para mapear nombres de campos Java a nombres de propiedades JSON diferentes
    public Map<String, Object> getGeometriaGeoJson() {
        if (geometria == null) return null;

        Map<String, Object> geoJson = new HashMap<>();
        geoJson.put("type", geometria.getGeometryType());
        geoJson.put("coordinates", GeoJsonHelper.convertToCoordinates(geometria));
        return geoJson;
    }



}
