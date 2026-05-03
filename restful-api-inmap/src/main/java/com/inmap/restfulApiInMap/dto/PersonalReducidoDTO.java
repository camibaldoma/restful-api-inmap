package com.inmap.restfulApiInMap.dto;

public class PersonalReducidoDTO {
    private String nombre;
    private String apellido;
    private String cargo;

    public PersonalReducidoDTO(String nombre, String apellido, String cargo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cargo = cargo;
    }

    public String getNombreCompleto() {
        return this.nombre + " " + this.apellido;
    }

    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getCargo() { return cargo; }
}
