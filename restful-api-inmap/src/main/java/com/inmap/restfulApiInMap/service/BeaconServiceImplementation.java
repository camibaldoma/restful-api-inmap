package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.dto.BeaconDTO;
import com.inmap.restfulApiInMap.entity.Beacon;
import com.inmap.restfulApiInMap.entity.Usuario;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.repository.BeaconRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class BeaconServiceImplementation implements BeaconService {
    @Autowired
    BeaconRepository beaconRepository;
    @Override
    public void receiveReportJSON(BeaconDTO beaconDTO) throws NotFoundException {
        //Entrada del POST enviado por el dispositivo
        OffsetDateTime time = OffsetDateTime.now();
        updateStateBeacon(time,beaconDTO);
        //No sé si crear un endpoint para que el administrador consulte la tabla de Beacons
        //O cuando hay una actualización avisa a la app
    }

    @Override
    public void updateStateBeacon(OffsetDateTime time, BeaconDTO beaconDTO) throws NotFoundException {
        //Busca por el id el iBeacon en la tabla
        Beacon beacon = beaconRepository.findById(beaconDTO.getIdBeacon()).orElseThrow(()-> new NotFoundException("Beacon no encontrado"));
        //Verifica que coincidan el uuid, major y minor
        if(beaconDTO.getBeaconUUID().equals(beacon.getBeaconUUID()) && beaconDTO.getMajor()==beacon.getMajor() && beaconDTO.getMinor()==beacon.getMinor()) {
            //Si está verificado en la tabla, actualiza los campos dinámicos.
            //Actualiza siempre la fila del dispositivo, la sobreescribe
            beacon.setBootCount(beaconDTO.getBootCount());
            beacon.setBatteryPercent(beaconDTO.getBatteryPercent());
            beacon.setFreeHeapBytes(beaconDTO.getFreeHeapBytes());
            beacon.setUptimeMs(beaconDTO.getUptimeMs());
            beacon.setAccumulatedSleepSeconds(beaconDTO.getAccumulatedSleepSeconds());
            beacon.setWakeupCause(beaconDTO.getWakeupCause());
            beacon.setResetReason(beaconDTO.getResetReason());
            beacon.setLastReportAt(time);
            beacon.setIsActive(beaconDTO.getIsActive());
            beaconRepository.save(beacon);
        }
        else {
            throw new NotFoundException("Beacon no encontrado");
        }
    }

    @Override
    public List<Beacon> obtenerTodosBeacons() {
        return beaconRepository.findAll();
    }
}
