package com.example.unilovi.model;

import android.os.Parcel;
import android.os.Parcelable;


public class User implements Parcelable {

    // Géneros posibles
    public static final String MASCULINO = "M";
    public static final String FEMENINO = "F";
    public static final String NO_BINARIO = "NB";

    // Combinaciones de géneros para las preferencias de búsqueda
    public static final String MASCULINO_FEMENINO = "MF";
    public static final String MASCULINO_NO_BINARIO = "MNB";
    public static final String FEMENINO_NO_BINARIO = "FNB";
    public static final String TODOS = "MFNB";

    public String name;
    public String email;
    public int age;
    public String school;
    public String career;
    public String photo;
    public Preferences preferences;
    public String sexo;
    public String city;

    public User(String name, String email, int age, String school, String career, String photo, Preferences preferences, String sexo, String city) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.school = school;
        this.career = career;
        this.photo = photo;
        this.preferences = preferences;
        this.sexo = sexo;
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", school='" + school + '\'' +
                ", career='" + career + '\'' +
                ", photo='" + photo + '\'' +
                ", sexo='" + sexo + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

// --- Métodos para que categoría sea parceable ---

    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
        age = in.readInt();
        school = in.readString();
        career = in.readString();
        photo = in.readString();
        preferences = in.readParcelable(Preferences.class.getClassLoader());
        sexo = in.readString();
        city = in.readString();
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
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeInt(age);
        parcel.writeString(school);
        parcel.writeString(career);
        parcel.writeString(photo);
        parcel.writeParcelable(preferences, i);
        parcel.writeString(sexo);
        parcel.writeString(city);
    }
}
