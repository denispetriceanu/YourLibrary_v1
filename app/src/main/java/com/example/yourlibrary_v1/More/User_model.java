package com.example.yourlibrary_v1.More;

public class User_model {
    private String name, email, mobile_nr, adress;

    public User_model(String name, String email, String mobile_nr, String adress) {
        this.name = name;
        this.email = email;
        this.mobile_nr = mobile_nr;
        this.adress = adress;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile_nr() {
        return mobile_nr;
    }

    public String getAdress() {
        return adress;
    }
}
