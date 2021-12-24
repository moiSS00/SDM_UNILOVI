package com.example.unilovi.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class User implements Parcelable {

    // Géneros posibles (se usan para las preferencias de búsqueda)
    // public static final String MASCULINO = "M";
    // public static final String FEMENINO = "F";
    // public static final String OTRO = "O";

    private String email;
    private String password;
    private String nombre;
    private String apellidos;
    private String fechaNacimiento;
    private String sexo;
    private String facultad;
    private String carrera;
    private String sobreMi;
    private String formaContacto;
    private int edad;
    private List<String> solicitudes = new ArrayList<String>();
    private List<String> matches = new ArrayList<String>();
    private List<String> rechazados = new ArrayList<String>();

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

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
        this.edad = calcularEdad(fechaNacimiento);
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

    public int getEdad() {
        return edad;
    }

    public List<String> getSolicitudes() {
        return solicitudes;
    }

    public void setSolicitudes(List<String> solicitudes) {
        this.solicitudes = solicitudes;
    }

    public List<String> getMatches() {
        return matches;
    }

    public void setMatches(List<String> matches) {
        this.matches = matches;
    }

    public List<String> getRechazados() {
        return rechazados;
    }

    public void setRechazados(List<String> rechazados) {
        this.rechazados = rechazados;
    }

    // --- Métodos para que categoría sea parceable ---

    protected User(Parcel in) {
        email = in.readString();
        password = in.readString();
        nombre = in.readString();
        apellidos = in.readString();
        fechaNacimiento = in.readString();
        sexo = in.readString();
        facultad = in.readString();
        carrera = in.readString();
        sobreMi = in.readString();
        formaContacto = in.readString();
        edad = in.readInt();
        in.readStringList(solicitudes);
        in.readStringList(matches);
        in.readStringList(rechazados);
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
        parcel.writeString(fechaNacimiento);
        parcel.writeString(sexo);
        parcel.writeString(facultad);
        parcel.writeString(carrera);
        parcel.writeString(sobreMi);
        parcel.writeString(formaContacto);
        parcel.writeInt(edad);
        parcel.writeStringList(solicitudes);
        parcel.writeStringList(matches);
        parcel.writeStringList(rechazados);
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", sexo='" + sexo + '\'' +
                ", facultad='" + facultad + '\'' +
                ", carrera='" + carrera + '\'' +
                ", sobreMi='" + sobreMi + '\'' +
                ", formaContacto='" + formaContacto + '\'' +
                ", edad=" + edad +
                '}';
    }

    /**
     * Método que devuelve la edad del usuario
     * @param fechaNacimiento fecha de nacimiento del usuario
     * @return edad del usuario
     */
    private int calcularEdad(String fechaNacimiento) {
        String[] texto = fechaNacimiento.split("/");
        int year = Integer.parseInt(texto[0]);
        int month = Integer.parseInt(texto[1]);
        int day = Integer.parseInt(texto[2]);

        GregorianCalendar nacimiento = new GregorianCalendar();
        GregorianCalendar today = new GregorianCalendar();

        nacimiento.set(year, month, day);

        int age = today.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        return age;
    }
}
