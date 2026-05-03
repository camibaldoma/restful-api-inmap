package com.inmap.restfulApiInMap.classes;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TieneAsociadoId implements Serializable {
    private String idPersonal;
    private String idDestino;
}
