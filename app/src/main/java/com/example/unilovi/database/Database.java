package com.example.unilovi.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Database {

    private List<String> listaFacultades;
    private HashMap<String, List<String>> tablaCarreras;
    private List<String> listaCiudades;

    public Database() {
        listaFacultades = new ArrayList<String>();
        tablaCarreras = new HashMap<>();
        listaCiudades = new ArrayList<String>();
    }

    // Inicializa el modelo de datos
    public void init() {
        listaFacultades.add("Sin definir");
        listaFacultades.add("Escuela de ingeniería informática");
        listaFacultades.add("Facultad de ciencias");
        listaFacultades.add("Facultad de derecho");

        tablaCarreras.put("Sin definir", new ArrayList<String>(Arrays.asList("Sin definir")));
        tablaCarreras.put("Escuela de ingeniería informática", new ArrayList<String>(Arrays.asList("Ingeniería Informática")));
        tablaCarreras.put("Facultad de ciencias", new ArrayList<String>(Arrays.asList("Sin definir", "Matemáticas", "Física", "...")));
        tablaCarreras.put("Facultad de derecho", new ArrayList<String>(Arrays.asList("Sin definir", "Derecho", "Derecho y Economía", "...")));

        listaCiudades.add("Sin definir");
        listaCiudades.add("Gijón");
        listaCiudades.add("Oviedo");
        listaCiudades.add("Avilés");
        listaCiudades.add("...");
    }

    public List<String> getListaFacultades() {
        return listaFacultades;
    }

    public HashMap<String, List<String>> getTablaCarreras() {
        return tablaCarreras;
    }

    public List<String> getListaCiudades() {
        return listaCiudades;
    }
}