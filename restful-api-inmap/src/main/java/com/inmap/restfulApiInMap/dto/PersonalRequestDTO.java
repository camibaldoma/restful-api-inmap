package com.inmap.restfulApiInMap.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalRequestDTO {
    String nombrePersonal;
    String apellidoPersonal;
    String cargoLaboral;
    String dni;
}
