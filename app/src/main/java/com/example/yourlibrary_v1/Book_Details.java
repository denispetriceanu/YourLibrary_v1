package com.example.yourlibrary_v1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.yourlibrary_v1.ui.home.Adapters.HomeRecyclerViewAdapter.addChar;

public class Book_Details extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__details);

        final TextView Title= findViewById(R.id.book_title_id);
        final TextView author= findViewById(R.id.book_author_id);
        final TextView category= findViewById(R.id.book_category_id);
        final ImageView image = findViewById(R.id.book_img_id);
        final TextView description= findViewById(R.id.book_description_id);
        final TextView release= findViewById(R.id.book_releas_id);
          // this line of code set the title for new activity
        Objects.requireNonNull(getSupportActionBar()).setTitle("Book details");

        final String book_id = getIntent().getStringExtra("book_id");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        assert book_id != null;
        DatabaseReference myRef = database.getReference("books").child(book_id);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Book book = dataSnapshot.getValue(Book.class);
                author.setText(book.getAuthor());
                Title.setText(book.getTitle());
//                Picasso.with(this).load(Uri.parse(book.getLargeCoverUrl())).error(R.id.book_img_id).into(image);
                category.setText(book.getCategory());
                description.setText(book.getDescription());
                release.setText(book.getData_publisher());
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
