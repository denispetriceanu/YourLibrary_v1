package com.example.yourlibrary_v1.More;

import com.google.firebase.database.Exclude;

public class Favourite {


    private String title;
    private String author;
    private String image;

    public Favourite() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
