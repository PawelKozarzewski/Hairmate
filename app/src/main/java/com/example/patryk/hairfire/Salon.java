package com.example.patryk.hairfire;

import android.os.Parcel;
import android.os.Parcelable;

public class Salon implements Parcelable {
    private String name;
    private String city;
    private String address;
    private String email;
    private int phone_number;
    private String description;
    private String photo;
    private String user_id;

    public Salon(){}

    public Salon(String name, String city, String address, String email, int phone_number, String description, String photo, String user_id) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.email = email;
        this.phone_number = phone_number;
        this.description = description;
        this.photo = photo;
        this.user_id = user_id;
    }

    public Salon(Parcel in) {
        photo = in.readString();
        name = in.readString();
        address = in.readString();
        city = in.readString();
        phone_number = in.readInt();
        description = in.readString();
        email = in.readString();
        user_id = in.readString();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(int phone_number) {
        this.phone_number = phone_number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUser() {
        return user_id;
    }

    public void setUser(String user_id) {
        this.user_id = user_id;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(photo);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeInt(phone_number);
        dest.writeString(description);
        dest.writeString(email);
        dest.writeString(user_id);
    }

    public static final Parcelable.Creator<Salon> CREATOR = new Parcelable.Creator<Salon>()
    {
        public Salon createFromParcel(Parcel in)
        {
            return new Salon(in);
        }
        public Salon[] newArray(int size)
        {
            return new Salon[size];
        }
    };
}

