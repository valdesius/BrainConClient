package com.example.brainconclient.helpers;

public class ApiLinksHelper {
    private static final String BASE_URL = "http://192.168.0.35:8080/api/v1/";

    public static String authUserApiUri() {
        return BASE_URL + "auth/authenticate";
    }

    public static String registerUserApiUri() {
        return BASE_URL + "auth/register";
    }

}