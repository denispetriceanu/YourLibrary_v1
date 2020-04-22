package com.example.yourlibrary_v1.More;

import com.google.firebase.auth.FirebaseUser;

public class Utils {
    public static final String regEx = "^[A-Za-z0-9+_.-]+@(.+)$";

    public static String addChar(String str) {
        return str.substring(0, 4) + "s" + str.substring(4);
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

    public void updateUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            System.out.println("You are not connected");
        } else {
            System.out.println("You are connected as: " + currentUser.getEmail());
        }
    }


}

