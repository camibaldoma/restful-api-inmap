package com.inmap.restfulApiInMap.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class EstaId implements Serializable {
    private String idAsignacion;
    private String idPersonal;
}
