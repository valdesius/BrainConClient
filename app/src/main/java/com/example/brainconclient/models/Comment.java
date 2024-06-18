package com.example.brainconclient.models;

import java.util.Date;

public class Comment {

    private int comment_id;

    private String content;
    private Date createdAt;

    private String username;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public Comment(int comment_id, String content, Date createdAt) {
        this.comment_id = comment_id;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Comment(int comment_id, String username, String content) {
        this.comment_id = comment_id;
        this.username = username;
        this.content = content;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
