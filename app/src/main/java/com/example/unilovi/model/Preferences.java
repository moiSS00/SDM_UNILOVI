package com.example.unilovi.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Preferences implements Parcelable {

    public ArrayList<String> sexo;
    public String facultad;
    public String ciudad;
    public int edadMinima;
    public int edadMaxima;

    public Preferences(ArrayList<String> sexo, String facultad, String ciudad, int edadMinima, int edadMaxima) {
        this.sexo = sexo;
        this.facultad = facultad;
        this.ciudad = ciudad;
        this.edadMinima = edadMinima;
        this.edadMaxima = edadMaxima;
    }

    protected Preferences(Parcel in) {
        sexo = in.readArrayList(ArrayList.class.getClassLoader());
        facultad = in.readString();
        ciudad = in.readString();
        edadMinima = in.readInt();
        edadMaxima = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(sexo);
        dest.writeString(facultad);
        dest.writeString(ciudad);
        dest.writeInt(edadMinima);
        dest.writeInt(edadMaxima);
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

    public ArrayList<String> getSexo() {
        return sexo;
    }

    public void setSexo(ArrayList<String> sexo) {
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
