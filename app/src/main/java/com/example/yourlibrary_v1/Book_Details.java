package com.example.yourlibrary_v1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yourlibrary_v1.More.Book;
import com.example.yourlibrary_v1.log.login;
import com.example.yourlibrary_v1.ui.home.Adapters.HomeRecyclerViewAdapter;
import com.example.yourlibrary_v1.ui.home.Fragment_Favorite_View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.lang.reflect.Member;
import java.util.Objects;

public class Book_Details extends AppCompatActivity {
    private FirebaseAuth mAuth;
    DatabaseReference reff;



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

        Button addFavButton = findViewById(R.id.checkbox_favourite);
        addFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation(getApplicationContext());
            }

            public void checkValidation(Context applicationContext) {
                if(FirebaseAuth.getInstance().getCurrentUser() == null )
                    DynamicToast.makeError(applicationContext, "Must login!.").show();
                else{
                    DynamicToast.makeError(applicationContext, "Succes").show();

                }
            }
        });



        final String book_id = getIntent().getStringExtra("book_id");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        assert book_id != null;
        DatabaseReference myRef = database.getReference("books").child(book_id);

        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Book book = dataSnapshot.getValue(Book.class);
                assert book != null;
                author.setText(book.getAuthor());
                category.setText(Objects.requireNonNull(dataSnapshot.child("categories").getValue()).toString());
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

//    public class MyHolder extends RecyclerView.ViewHolder implements
//            View.OnClickListener, CompoundButton.OnCheckedChangeListener {
//        Button button;
//
//        private MyHolder(@NonNull View Button) {
//            super(Button);
//            button = Button.findViewById(R.id.checkbox_favourite);
//            button.setOnClickListener(this);
//        }
//        @Override
//        public void onClick(View v) {
//
//        }
//
//        @Override
//        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//            if(FirebaseAuth.getInstance().getCurrentUser() == null ){
//                startActivity(new Intent(getApplicationContext(), login.class));
//                buttonView.setChecked(false);
//                return;
//            }
//
//            int position = getAdapterPosition();
//            Book b= mData.get(position);
//
//            DatabaseReference dbFavs = FirebaseDatabase.getInstance().getReference("users")
//                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                    .child("favourites")
//                    .child(b.category);
//

//            if(isChecked){
//                dbFavs.child(b.id).setValue(b);
//            }else{
//                dbFavs.child(b.id).setValue(null);
//            }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
