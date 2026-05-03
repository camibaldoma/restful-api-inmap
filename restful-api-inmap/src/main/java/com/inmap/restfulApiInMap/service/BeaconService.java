package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.dto.BeaconDTO;
import com.inmap.restfulApiInMap.entity.Beacon;

import java.time.OffsetDateTime;
import java.util.List;

public interface BeaconService {
    void receiveReportJSON(BeaconDTO beaconDTO);
    void updateStateBeacon(OffsetDateTime time, BeaconDTO beaconDTO);
    List<Beacon> obtenerTodosBeacons();
}
