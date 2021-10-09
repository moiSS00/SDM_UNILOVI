package com.example.unilovi.model;

public class User {

    public String name;
    public String email;
    public int age;
    public String school;
    public String career;
    public String photo;

    public User(String name, String email, int age, String school, String career, String photo) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.school = school;
        this.career = career;
        this.photo = photo;
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
                '}';
    }

}
