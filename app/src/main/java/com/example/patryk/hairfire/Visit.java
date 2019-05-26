package com.example.patryk.hairfire;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Visit {
    private String salonName;
    private String date;
    private String hour;
    private String salonId;
    private String userId;
    private String photo;
    private String service;

    public Visit() {
    }

    public Visit(String name, String date, String hour, String service) {
        this.salonName = name;
        this.date = date;
        this.hour = hour;
        this.service = service;
    }

    public String getSalonName() {
        return salonName;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getSalonId() {
        return salonId;
    }

    public void setSalonId(String salonId) {
        this.salonId = salonId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Date getDecodedDate() {
        String[] date_parts = this.getDate().split("-");
        String[] hour_parts = this.getHour().split(":");
        String year = date_parts[2];
        String month = date_parts[1];
        String day = date_parts[0].split(" ")[1];
        String hour = hour_parts[1];
        String minute = hour_parts[2];

        String date_string = year + "-" + month + "-" + day + "-" + hour + "-" + minute;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        Date date1 = null;
        try {
            date1 = sdf.parse(date_string);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date1;
    }
}
