package com.example.yourlibrary_v1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yourlibrary_v1.More.Book;
import com.example.yourlibrary_v1.More.Utils;
import com.example.yourlibrary_v1.log.login;
import com.example.yourlibrary_v1.ui.home.Adapters.HomeRecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.Objects;

public class Book_Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__details);

        // with this line we get the book id from last activity
        final String book_id = getIntent().getStringExtra("book_id");

        final TextView Title = findViewById(R.id.book_title_id);
        final TextView author = findViewById(R.id.book_author_id);
        final TextView category = findViewById(R.id.book_category_id);
        final ImageView image = findViewById(R.id.book_details_img);
        final TextView description = findViewById(R.id.book_description_id);
        final TextView release = findViewById(R.id.book_releas_id);
        final TextView rating = findViewById(R.id.book_rating_id);


        // add click listener to add fav btn
        final Button addFavButton = findViewById(R.id.checkbox_favourite);
        addFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    DynamicToast.makeError(getApplicationContext(), "Must login!").show();
                    startActivity(new Intent(Book_Details.this, login.class));
                } else {
                    addToFavoritesList(book_id);
                    addFavButton.setText("Remove from FAV");

                }
            }
        });

        // add click listener to add read btn
        final Button addReadList = findViewById(R.id.addBtn);
        addReadList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    DynamicToast.makeError(getApplicationContext(), "Must login!").show();
                    startActivity(new Intent(Book_Details.this, login.class));
                } else {
                    addReadList.setText("Remove from ADD");

                }
            }
        });






                // create a instance for database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        assert book_id != null;
        // navigate in table "books", on the "row" where is "book_id"
        // row of a table is as a child
        // in firebase we don't have table (but we say generic "table"), this is a tree, more exactly a JSON format
        // create a reference
        DatabaseReference myRef = database.getReference("books").child(book_id);

        // get data from thar reference
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Book book = dataSnapshot.getValue(Book.class);
                assert book != null;

                Title.setText(book.getTitle());

                // in class Utils we have function generateCategory
                // this function need receive a string (category in initial form)
                // and that return correct form

                // create initial object
                Utils utils = new Utils();
                // cal function for category
                String author_string = utils.formatCategory(
                        // here we put our string which receive from firebase
                        book.getAuthor()
                );
                author.setText(author_string);

                category.setText(new Utils().formatCategory(Objects.requireNonNull(dataSnapshot.child("categories").getValue().toString())));


                // this line of code set the title for activity
                Objects.requireNonNull(getSupportActionBar()).setTitle(book.getTitle());

                String url = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();
                url = HomeRecyclerViewAdapter.addChar(url);
                // use Glide for generate the image from url
                Glide.with(getApplicationContext())
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(image);
                description.setText(book.getDescription());
                release.setText(Objects.requireNonNull(dataSnapshot.child("date_publisher").getValue()).toString());
                String rating_converted = "Rating: " + dataSnapshot.child("rating").getValue();
                rating.setText(rating_converted);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                DynamicToast.makeWarning(getApplicationContext(), "Something went wrong").show();
                System.out.println("Error: " + database.toString());
            }
        });

    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // function to add book to read list
    private void addToFavoritesList(String book_id) {
        // we create a reference for table favorites
        // if table don't exists will be create automatic
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("favorites");
        // i can write in one line (FirebaseDatabase.getInstance().getReference("favorites").child(user_id))
        // but i divide for you can understood
        // the child for this table will be user
        // can get user from FirebaseAuth
        firebaseDatabase = firebaseDatabase.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        // now we can add book in this route
        // child is a new row
        // if don't put set value, don't work, the value can be anything
        // I choose to put value the date, maybe we use that...idk
        firebaseDatabase.child(book_id).setValue(System.currentTimeMillis());
        // System.currentTimeMillis() --  return a time, but in a format like a timestamp
    }

    public void checkInDatabase(String location, final String id_book) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(location)
                .child(FirebaseAuth.getInstance().getUid());
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(" Am fost aici "+ dataSnapshot);
                if (dataSnapshot.hasChild(id_book)){
                    return ;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

