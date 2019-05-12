package com.example.patryk.hairfire;

public class Service {
    private String salonId;
    private String name;
    private String price;
    private String duration;

    public Service(){

    }

    public Service(String salonId, String name, String price, String duration) {
        this.salonId = salonId;
        this.name = name;
        this.price = price;
        this.duration = duration;
    }

    public String getSalonId() {
        return salonId;
    }

    public void setSalonId(String salonId) {
        this.salonId = salonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
