package com.example.unilovi.model;

import android.os.Parcel;
import android.os.Parcelable;


public class User implements Parcelable {

    // Géneros posibles (se usan para las preferencias de búsqueda)
    // public static final String MASCULINO = "M";
    // public static final String FEMENINO = "F";
    // public static final String OTRO = "O";

    private String email;
    private String password;
    private String nombre;
    private String apellidos;
    private String uriFoto;
    private String fechaNacimiento;
    private String sexo;
    private String facultad;
    private String carrera;
    private Preferences preferences;
    private String sobreMi;
    private String formaContacto;

    // private int age;

    public User() {

    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getUriFoto() {
        return uriFoto;
    }

    public void setUriFoto(String uriFoto) {
        this.uriFoto = uriFoto;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo.substring(0, 1);
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

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public String getSobreMi() {
        return sobreMi;
    }

    public void setSobreMi(String sobreMi) {
        this.sobreMi = sobreMi;
    }

    public String getFormaContacto() {
        return formaContacto;
    }

    public void setFormaContacto(String formaContacto) {
        this.formaContacto = formaContacto;
    }

    // --- Métodos para que categoría sea parceable ---

    protected User(Parcel in) {
        email = in.readString();
        password = in.readString();
        nombre = in.readString();
        apellidos = in.readString();
        uriFoto = in.readString();
        fechaNacimiento = in.readString();
        sexo = in.readString();
        facultad = in.readString();
        carrera = in.readString();
        preferences = in.readParcelable(Preferences.class.getClassLoader());
        sobreMi = in.readString();
        formaContacto = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int i) {
            return new User[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(nombre);
        parcel.writeString(apellidos);
        parcel.writeString(uriFoto);
        parcel.writeString(fechaNacimiento);
        parcel.writeString(sexo);
        parcel.writeString(facultad);
        parcel.writeString(carrera);
        parcel.writeParcelable(preferences, i);
        parcel.writeString(sobreMi);
        parcel.writeString(formaContacto);
    }
}
