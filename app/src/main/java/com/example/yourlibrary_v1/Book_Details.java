package com.example.yourlibrary_v1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yourlibrary_v1.More.Book;
import com.example.yourlibrary_v1.ui.home.Adapters.HomeRecyclerViewAdapter;
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

        final TextView Title = findViewById(R.id.book_title_id);
        final TextView author = findViewById(R.id.book_author_id);
        final TextView category = findViewById(R.id.book_category_id);
        final ImageView image = findViewById(R.id.book_details_img);
        final TextView description = findViewById(R.id.book_description_id);
        final TextView release = findViewById(R.id.book_releas_id);
        final TextView rating = findViewById(R.id.book_rating_id);
        // this line of code set the title for new activity
        Objects.requireNonNull(getSupportActionBar()).setTitle("Book details");

        final String book_id = getIntent().getStringExtra("book_id");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        assert book_id != null;
        DatabaseReference myRef = database.getReference("books").child(book_id);

        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Book book = dataSnapshot.getValue(Book.class);

                // ToDo: Trebuie facuta verificare la fiecare data de genul: if(title != "")...else(txtView.setTitle("Undefined")
                assert category != null;
                assert book != null;
                author.setText(book.getAuthor());
                category.setText(Objects.requireNonNull(dataSnapshot.child("categories").getValue()).toString());
                if (!category.getText().equals("-")) {
                    category.setText("Undefined");
                }

                Title.setText(book.getTitle());
                String url = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();
                url = HomeRecyclerViewAdapter.addChar(url);
                System.out.println(url);
                Glide.with(getApplicationContext())
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(image);

                // rezolvam ca in cazul ratingului, vezi mai jos
                description.setText(book.getDescription());
                release.setText(Objects.requireNonNull(dataSnapshot.child("date_publisher").getValue()).toString());
                // nu stiu de ce nu prelua din book (probabil nu converteste bine datele, preluam direct
                // prin punerea (int) in fata facem cast (adica convertim) din long in String (facem asta deoarece TextView-ul nu
                // permite decat int sau String
                String rating_converted = "Rating: " + dataSnapshot.child("rating").getValue();
                rating.setText(rating_converted);
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
