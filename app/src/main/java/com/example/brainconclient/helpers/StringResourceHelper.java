package com.example.brainconclient.helpers;

public class StringResourceHelper {

    private static final String USER_DETAIL_PREF = "USER_INFO";


    public static boolean regexEmailValidationPattern(String email) {

        String regex = "([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})";

        if (email.matches(regex)) {
            return true;
        }
        return false;
    }

    public static String getUserDetailPrefName() {
        return USER_DETAIL_PREF;
    }
}
