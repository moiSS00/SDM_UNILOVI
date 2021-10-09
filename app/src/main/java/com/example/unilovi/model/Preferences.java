package com.example.unilovi.model;

public class Preferences {

    public String sexo;
    public String facultad;
    public String ciudad;
    public int edadMinima;
    public int edadMaxima;

    public Preferences(String sexo, String facultad, String ciudad, int edadMinima, int edadMaxima) {
        this.sexo = sexo;
        this.facultad = facultad;
        this.ciudad = ciudad;
        this.edadMinima = edadMinima;
        this.edadMaxima = edadMaxima;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public int getEdadMinima() {
        return edadMinima;
    }

    public void setEdadMinima(int edadMinima) {
        this.edadMinima = edadMinima;
    }

    public int getEdadMaxima() {
        return edadMaxima;
    }

    public void setEdadMaxima(int edadMaxima) {
        this.edadMaxima = edadMaxima;
    }
}
