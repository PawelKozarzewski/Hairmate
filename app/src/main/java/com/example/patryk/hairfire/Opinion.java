package com.example.patryk.hairfire;


public class Opinion {
    private String salon_id;
    private String photo;
    private String title;
    private String content;
    private String author;
    private String date;

    public Opinion(String salon_id, String photo, String title, String content, String author, String date) {
        this.salon_id = salon_id;
        this.photo = photo;
        this.title = title;
        this.content = content;
        this.author = author;
        this.date = date;
    }

    public Opinion(){ }

    public String getPhoto() {
        return photo;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSalon_id() {
        return salon_id;
    }

    public void setSalon_id(String salon_id) {
        this.salon_id = salon_id;
    }
}
