package com.example.brainconclient.models;

public class Test {
    private int test_id;
    private String title;
    private String body;
    private String question;
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Test(int test_id, String title, String body, String question, String answer) {
        this.test_id = test_id;
        this.title = title;
        this.body = body;
        this.question = question;
        this.answer = answer;
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
