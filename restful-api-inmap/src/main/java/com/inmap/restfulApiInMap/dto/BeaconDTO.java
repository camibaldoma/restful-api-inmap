package com.inmap.restfulApiInMap.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeaconDTO {
    Long idBeacon;
    UUID beaconUUID;
    Integer major;
    Integer minor;
    Integer bootCount;
    Integer batteryPercent;
    Integer freeHeapBytes;
    Long uptimeMs;
    Integer accumulatedSleepSeconds;
    String wakeupCause;
    String resetReason;
    Boolean isActive;
}
