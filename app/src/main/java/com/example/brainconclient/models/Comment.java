package com.example.brainconclient.models;

import java.util.Date;

public class Comment {


    private int comment_id;

    private String content;
    private String createdAt;

    private String username;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public Comment(String content,String username, String createdAt) {
        this.content = content;
        this.username = username;
        this.createdAt = createdAt;
    }

    public Comment(String content) {
        this.content = content;

    }


    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
