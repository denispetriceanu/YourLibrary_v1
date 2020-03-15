package com.example.yourlibrary_v1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class Book_Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__details);

        // this line of code set the title for new activity
        Objects.requireNonNull(getSupportActionBar()).setTitle("Book details");


        System.out.println("Text received:" + getIntent().getStringExtra("book_id"));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
