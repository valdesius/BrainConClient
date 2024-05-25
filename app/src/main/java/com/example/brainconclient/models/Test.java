package com.example.brainconclient.models;

public class Test {
    private int test_id;
    private String title;
    private String body;

    public Test(int test_id, String title, String body) {
        this.test_id = test_id;
        this.title = title;
        this.body = body;
    }

    public Test(String title, String body){
        this.title  = title;
        this.body   = body;
    }

    public int getTest_id() {
        return test_id;
    }

    public void setTest_id(int test_id) {
        this.test_id = test_id;
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
