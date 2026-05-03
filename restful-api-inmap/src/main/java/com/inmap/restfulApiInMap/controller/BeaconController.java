package com.inmap.restfulApiInMap.controller;


import com.inmap.restfulApiInMap.dto.AsignacionRequestDTO;
import com.inmap.restfulApiInMap.dto.BeaconDTO;
import com.inmap.restfulApiInMap.entity.Asignacion;
import com.inmap.restfulApiInMap.entity.Beacon;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.error.OverlapException;
import com.inmap.restfulApiInMap.service.BeaconService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(
        originPatterns = "*",
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = "*"
)
public class BeaconController {
    @Autowired
    BeaconService beaconService;
    @PostMapping("/reportWifi")
    public void reportBeacon(@Valid @RequestBody BeaconDTO beaconDTO) {
        beaconService.receiveReportJSON(beaconDTO);
    }
    @GetMapping("/obtenerReporte")
    public List<Beacon> obtenerReporte() {
        return beaconService.obtenerTodosBeacons();
    }
}
