package com.example.patryk.hairfire;

public class User {
    public String name;
    public String surname;
    public boolean client;

    public User() { }

    public User(String name, String surname, boolean client) {
        this.name = name;
        this.surname = surname;
        this.client=client;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isClient() {
        return client;
    }

    public void setClient(boolean client) {
        this.client = client;
    }
}
