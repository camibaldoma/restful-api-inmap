package com.inmap.restfulApiInMap.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inmap.restfulApiInMap.classes.GeoJsonHelper;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.locationtech.jts.geom.Geometry;

import java.util.HashMap;
import java.util.Map;


@Entity
// Esta anotación define contra que tabla de la base de datos la entidad se va a mapear
@Table(name = "zonas_a_bloquear")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Zonas_a_bloquear {
    @Id
    @Column(name = "id_zona") // Nombre exacto de la columna PK
    @NotBlank(message = "El ID no puede estar vacío")
    @NotNull(message = "El ID es obligatorio")
    private String idZona;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JdbcTypeCode(org.hibernate.type.SqlTypes.GEOMETRY)
    @Column(name = "geom")
    @NotNull(message = "La geometría es obligatoria")
    private Geometry geometria;

    @JsonProperty("geometria")
    public Map<String, Object> getGeometriaGeoJson() {
        if (geometria == null) return null;

        Map<String, Object> geoJson = new HashMap<>();
        geoJson.put("type", geometria.getGeometryType());
        geoJson.put("coordinates", GeoJsonHelper.convertToCoordinates(geometria));
        return geoJson;
    }

    @Column(name = "bloqueado")
    private Boolean bloqueado;

    @Column(name = "bloqueo_permanente")
    private Boolean bloqueo_permanente;
}
