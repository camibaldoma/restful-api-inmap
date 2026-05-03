package com.inmap.restfulApiInMap.dto;


import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.entity.Horario;
import com.inmap.restfulApiInMap.entity.Materia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsignacionRequestDTO {
    Materia materia;
    Destino destino;
    Horario horario;
}
