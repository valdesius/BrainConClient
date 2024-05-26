package com.example.brainconclient.models;

public class Course {
    private boolean isFavorite;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    private int course_id;
    private String title;
    private String body;

    public Course(int note_id, String title, String body) {
        this.course_id = note_id;
        this.title = title;
        this.body = body;
    }

    public Course(String title, String body){
        this.title  = title;
        this.body   = body;
    }

    public int getNote_id() {
        return course_id;
    }

    public void setNote_id(int note_id) {
        this.course_id = note_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
