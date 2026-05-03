package com.inmap.restfulApiInMap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//@Entity define la entidad como persistible
@Entity
// Esta anotación define contra que tabla de la base de datos la entidad se va a mapear
@Table(name = "recinto")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Recinto {
    @Id
    @Column(name = "id_recinto") // Nombre exacto de la columna PK
    @NotBlank(message = "El ID no puede estar vacío")
    @NotNull(message = "El ID es obligatorio")
    private String idRecinto;

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
    @OneToOne(cascade = CascadeType.ALL) //Cuando se haga recintoRepository.save(recinto),
    // Hibernate mirará el objeto Destino que está adentro. Si el idDestino es nuevo, lo insertará automáticamente.
    // Si ya existe, simplemente hará el vínculo.
    @JoinColumn(name = "id_destino")
    @Valid  //Activa la validación en cascada
    @NotNull(message = "El objeto destino es obligatorio")
    private Destino destino;

    @Column(name = "bloqueado", nullable = false)
    private Boolean bloqueado;

}
