package com.inmap.restfulApiInMap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioRequestDTO {
    String dias;
    String horaInicio;
    String horaFin;

}
