package com.inmap.restfulApiInMap.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
// Esta anotación define contra que tabla de la base de datos la entidad se va a mapear
@Table(name = "beacons")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Beacon {
    @Id //Marca el atributo como la clave primaria de la entidad
    @Column(name = "idbeacon") // Permite personalizar el mapeo entre el atributo de la clase y la columna en la tabla de la base de datos. Nombre exacto de la columna
    @NotNull(message = "El ID es obligatorio")
    private Long idBeacon;

    @Column(name = "device_name")
    @NotNull(message = "El nombre del dispositivo es obligatorio")
    private String deviceName;

    @Column(name = "beacon_uuid")
    @NotNull(message = "El UUID es obligatorio")
    private UUID beaconUUID;

    @Column(name = "major")
    @NotNull(message = "El major es obligatorio")
    private Integer major;

    @Column(name = "minor")
    @NotNull(message = "El minor es obligatorio")
    private Integer minor;

    @Column(name = "map_x", precision = 10, scale = 2)
    @NotNull(message = "La coordenada X es obligatoria")
    private BigDecimal posicionX;

    @Column(name = "map_y", precision = 10, scale = 2)
    @NotNull(message = "La coordenada Y es obligatoria")
    private BigDecimal posicionY;

    @Column(name = "floor_name")
    @NotNull(message = "El nombre del piso es obligatorio")
    private String floorName;

    @Column(name = "description")
    private String description;

    @Column(name = "boot_count")
    private Integer bootCount;

    @Column(name = "battery_percent")
    private Integer batteryPercent;

    @Column(name = "free_heap_bytes")
    private Integer freeHeapBytes;

    @Column(name = "uptime_ms")
    private Long uptimeMs;

    @Column(name = "accumulated_sleep_seconds")
    private Integer accumulatedSleepSeconds;

    @Column(name = "wakeup_cause")
    private String wakeupCause;

    @Column(name = "reset_reason")
    private String resetReason;

    @Column(name = "last_report_at")
    private OffsetDateTime lastReportAt;

    @Column(name = "is_active")
    @NotNull(message = "El estado es obligatorio")
    private Boolean isActive;

}
