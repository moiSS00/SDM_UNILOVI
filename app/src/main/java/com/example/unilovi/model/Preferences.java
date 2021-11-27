package com.example.unilovi.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Preferences implements Parcelable {

    private int edadMinima;
    private int edadMaxima;
    private ArrayList<String> sexos;
    private String facultad;
    private String carrera;


    public Preferences() {

    }

    protected Preferences(Parcel in) {
        edadMinima = in.readInt();
        edadMaxima = in.readInt();
        sexos = in.readArrayList(ArrayList.class.getClassLoader());
        facultad = in.readString();
        carrera = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(edadMinima);
        dest.writeInt(edadMaxima);
        dest.writeList(sexos);
        dest.writeString(facultad);
        dest.writeString(carrera);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Preferences> CREATOR = new Creator<Preferences>() {
        @Override
        public Preferences createFromParcel(Parcel in) {
            return new Preferences(in);
        }

        @Override
        public Preferences[] newArray(int size) {
            return new Preferences[size];
        }
    };

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

    public ArrayList<String> getSexos() {
        return sexos;
    }

    public void setSexos(ArrayList<String> sexos) {
        this.sexos = sexos;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }
}
