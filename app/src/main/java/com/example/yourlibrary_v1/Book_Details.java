package com.example.yourlibrary_v1;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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

        final TextView Title = findViewById(R.id.book_title_id);
        final TextView author = findViewById(R.id.book_author_id);
        final TextView category = findViewById(R.id.book_category_id);
        final ImageView image = findViewById(R.id.book_img_id);
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
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Book book = dataSnapshot.getValue(Book.class);

                // ToDo: Trebuie facuta verificare la fiecare data de genul: if(title != "")...else(txtView.setTitle("Undefined")
                assert category != null;
                author.setText(book.getAuthor());
                if(!category.equals("-")){
                    category.setText("Undifenid");
                }
                Title.setText(book.getTitle());
//                Picasso.with(this).load(Uri.parse(book.getLargeCoverUrl())).error(R.id.book_img_id).into(image);
                // rezolvam ca in cazul ratingului, vezi mai jos
                category.setText(dataSnapshot.child("categories").getValue().toString());
                description.setText(book.getDescription());
                release.setText(dataSnapshot.child("date_publisher").getValue().toString());
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
