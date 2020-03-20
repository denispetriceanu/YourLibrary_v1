package com.example.yourlibrary_v1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Book_Details extends AppCompatActivity {
    private TextView title, description, thecategory, author;
    private ImageView img;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__details);

        String idBook = Objects.requireNonNull(new Intent().getExtras()).getString("book_id");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        assert idBook != null;
        DatabaseReference myRef = database.getReference("books").child(idBook);
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                System.out.println("Ce am primit: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println("Failed to read value." + error.toException());
            }
        });

        title = (TextView) findViewById(R.id.book_title_id);
        description = (TextView) findViewById(R.id.book_description_id);
        thecategory = (TextView) findViewById(R.id.book_category_id);
        author = (TextView) findViewById(R.id.book_aurhor_id);
        img = (ImageView) findViewById(R.id.book_img_id);

        Intent intent = getIntent();
        String Title = "Test";

        setTitle("Book: " + Title);
        String Description = intent.getExtras().getString("Description");
        String image = intent.getExtras().getString("Thumbnail");
        String category = intent.getExtras().getString("Category");
        String author = intent.getExtras().getString("author");

        thecategory.setText(category);
        title.setText(Title);
        Glide.with(this)
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
