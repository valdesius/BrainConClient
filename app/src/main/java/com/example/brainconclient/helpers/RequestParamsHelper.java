package com.example.brainconclient.helpers;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestParamsHelper {

    @Nullable
    public static JSONObject getRegistrationParams(String firstName, String lastName, String email, String password, boolean isStudent, boolean isMentor) {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("first_name", firstName);
            jsonParams.put("last_name", lastName);
            jsonParams.put("email", email);
            jsonParams.put("password", password);

            if (isStudent) {
                jsonParams.put("role", "STUDENT");
            } else if (isMentor) {
                jsonParams.put("role", "MENTOR");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonParams;
    }
}