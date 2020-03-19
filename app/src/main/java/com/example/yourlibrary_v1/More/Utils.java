package com.example.yourlibrary_v1.More;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

public class Utils {
    public static final String regEx = "^[A-Za-z0-9+_.-]+@(.+)$";
    public static final String login_activity = "Login_Activity";
    public static final String signUp_activity = "SignUp_Activity";
    public static final String forgotPassword_activity = "ForgotPassword_Activity";

    public void updateUI(FirebaseUser currentUser, Context context) {
        if (currentUser == null) {
            DynamicToast.makeWarning(context, "Nu esti conectat", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Esti conectat: " + currentUser.getEmail(), Toast.LENGTH_LONG).show();
        }
    }
}

