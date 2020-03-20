package com.example.yourlibrary_v1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Book_Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__details);

        // this line of code set the title for new activity
        Objects.requireNonNull(getSupportActionBar()).setTitle("Book details");

        String book_id = getIntent().getStringExtra("book_id");
        System.out.println("Id book received:" + book_id);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        assert book_id != null;
        DatabaseReference myRef = database.getReference("books").child(book_id);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String author = Objects.requireNonNull(dataSnapshot.child("author").getValue()).toString();
//                String categories = Objects.requireNonNull(dataSnapshot.child("categories").getValue()).toString();
//                String date_publisher = Objects.requireNonNull(dataSnapshot.child("date_publisher").getValue()).toString();
//                String description = Objects.requireNonNull(dataSnapshot.child("description").getValue()).toString();
//                String image = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();
//                String info_link = Objects.requireNonNull(dataSnapshot.child("info_link").getValue()).toString();
//                long rating = (long) Objects.requireNonNull(dataSnapshot.child("rating").getValue());
//                long nr_rating = (long) Objects.requireNonNull(dataSnapshot.child("nr_rating").getValue());
//                long page_count = (long) Objects.requireNonNull(dataSnapshot.child("page_count").getValue());
//                String thumbnail = Objects.requireNonNull(dataSnapshot.child("thumbnail").getValue()).toString();
//                String title = Objects.requireNonNull(dataSnapshot.child("title").getValue()).toString();

//                System.out.println("Value" + title + ", " + author + ", " + description);
                Book book = dataSnapshot.getValue(Book.class);
                System.out.println("Titlul este: " + book.getAuthor());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error: " + database.toString());
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
