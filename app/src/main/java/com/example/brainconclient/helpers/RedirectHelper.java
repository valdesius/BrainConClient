package com.example.brainconclient.helpers;

import android.content.Context;
import android.content.Intent;

import com.example.brainconclient.LoginActivity;
import com.example.brainconclient.SuccessActivity;

public class RedirectHelper {

    public static void goToSuccessActivity(Context context) {
        Intent goToSuccess = new Intent(context, SuccessActivity.class);
        context.startActivity(goToSuccess);
    }

    public static void goToLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
