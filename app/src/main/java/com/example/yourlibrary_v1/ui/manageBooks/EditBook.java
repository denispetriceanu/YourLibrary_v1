package com.example.yourlibrary_v1.ui.manageBooks;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yourlibrary_v1.More.Book;
import com.example.yourlibrary_v1.More.Utils;
import com.example.yourlibrary_v1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.Objects;

import static com.example.yourlibrary_v1.More.Utils.addChar;

public class EditBook extends AppCompatActivity {
    private boolean isModEdit = false;
    private String id_book;
    private EditText title, author, category, releaseDate, rating, pagecount, description;
    private Button save;
    private ImageView book_edit_img;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add new book");

        id_book = getIntent().getStringExtra("id_book");
        // from here you can delete, this is for testing
        if (id_book != null) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Edit the book");
            DynamicToast.makeSuccess(getApplicationContext(), "Book:" + id_book).show();
            isModEdit = true;
            getEditInfo();
        }
        init();

    }

    @SuppressLint("SetTextI18n")
    private void init() {
        title = findViewById(R.id.book_title);
        author = findViewById(R.id.book_author);
        category = findViewById(R.id.book_category);
        releaseDate = findViewById(R.id.book_release);
        save = findViewById(R.id.saveBtn);
        book_edit_img = findViewById(R.id.book_edit_img);
        rating= findViewById(R.id.book_rating);
        pagecount=findViewById(R.id.book_page);
        description=findViewById(R.id.book_description);


        if (isModEdit)
            save.setText("Edit book");
        else
            save.setText("Add book");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isModEdit) {
                    // will send for edit
                    if (check())
                        saveDataInFirebase();
                } else {
                    if (check())
                        saveDataInFirebase();
                }
            }
        });
    }

    // if is in mod edit you make a request to the firebase;
    private void getEditInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("books").child(id_book);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Book book = dataSnapshot.getValue(Book.class);
                assert book != null;
                Utils utils = new Utils();
                String author_string = utils.formatCategory(
                        book.getAuthor()
                );
                title.setText(Objects.requireNonNull(dataSnapshot.child("title").getValue()).toString());
                author.setText(author_string);
                category.setText(new Utils().formatCategory(Objects.requireNonNull(dataSnapshot.child("categories").getValue().toString())));
                releaseDate.setText(Objects.requireNonNull(dataSnapshot.child("date_publisher").getValue()).toString());
                Glide.with(getApplicationContext())
                        .load(addChar(Objects.requireNonNull(dataSnapshot.child("thumbnail").getValue()).toString()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(book_edit_img);
                rating.setText(Objects.requireNonNull(dataSnapshot.child("rating").getValue()).toString());
                pagecount.setText(Objects.requireNonNull(dataSnapshot.child("page_count").getValue()).toString());
                description.setText(book.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean check() {
        if (title.getText().length() < 3) {
            DynamicToast.makeError(getApplicationContext(), "The title is to short", Toast.LENGTH_SHORT).show();
            return false;
        } else if (author.getText().length() < 5) {
            DynamicToast.makeError(getApplicationContext(), "The author is to short", Toast.LENGTH_SHORT).show();
            return false;
        } else if (category.getText().length() < 1) {
            DynamicToast.makeError(getApplicationContext(), "The category is to short", Toast.LENGTH_SHORT).show();
            return false;
        } else if (releaseDate.getText().length() == 0) {
            DynamicToast.makeError(getApplicationContext(), "You don't write the date of publisher", Toast.LENGTH_SHORT).show();
            return false;
        } else if (rating.getText().length() == 0 && isModEdit) {
            DynamicToast.makeError(getApplicationContext(), "You don't write the date of publisher", Toast.LENGTH_SHORT).show();
            return false;
        } else if (pagecount.getText().length() < 1){
            DynamicToast.makeError(getApplicationContext(), "You don't write the page", Toast.LENGTH_SHORT).show();
            return false;
        }else if (description.getText().length() < 10) {
            DynamicToast.makeError(getApplicationContext(), "The description is to short", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveDataInFirebase() {
        DatabaseReference reference;
        if (id_book == null) {
            reference = FirebaseDatabase.getInstance().getReference().child("books").push();
        } else {
            reference = FirebaseDatabase.getInstance().getReference().child("books").child(id_book);
        }
        reference.child("title").setValue(title.getText().toString());
        reference.child("author").setValue(author.getText().toString());
        reference.child("categories").setValue(category.getText().toString());
        reference.child("date_publisher").setValue(releaseDate.getText().toString());
        reference.child("thumbnail").setValue("http://books.google.com/books/content?id=2dQ8KL2t99QC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api");
        reference.child("rating").setValue(rating.getText().toString());
        reference.child("page_count").setValue(pagecount.getText().toString());

        onBackPressed();
        if (isModEdit)
            DynamicToast.makeSuccess(getApplicationContext(), "Edited with success!", Toast.LENGTH_SHORT).show();

        else
            DynamicToast.makeSuccess(getApplicationContext(), "Edited with success!", Toast.LENGTH_SHORT).show();

    }
}
