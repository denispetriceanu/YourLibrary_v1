package com.example.yourlibrary_v1.More;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

public class Utils {
    public static final String regEx = "^[A-Za-z0-9+_.-]+@(.+)$";
    public static final String login_activity = "Login_Activity";
    public static final String signUp_activity = "SignUp_Activity";
    public static final String forgotPassword_activity = "ForgotPassword_Activity";

    public void updateUI(FirebaseUser currentUser, Context context) {
        if (currentUser == null) {
            System.out.println("You are not connected");
//            Toast.makeText(context, "Nu esti conectat", Toast.LENGTH_LONG).show();
        } else {
            System.out.println("You are connected as: " + currentUser.getEmail());
//            Toast.makeText(context, "Esti conectat: " + currentUser.getEmail(), Toast.LENGTH_LONG).show();
        }
    }

    public String truncateTitle(String initialTitle) {
        String returnTitle;
        if (initialTitle.length() > 27) {
            returnTitle = initialTitle.replace(initialTitle.substring(27), "");
            return returnTitle.concat("...");
        } else {
            return initialTitle;
        }
    }

    public String formatCategory(String category) {
        category = category.replace("]", "")
                .replace("[", "").replace("\"", "");
        if (category.equals("-")) category = "Undefined";
        category = category.toUpperCase();
        return category;
    }
}

