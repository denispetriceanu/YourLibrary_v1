package com.example.yourlibrary_v1.ui.manageBooks;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.yourlibrary_v1.More.Utils;
import com.example.yourlibrary_v1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.io.IOException;
import java.util.Objects;

import static com.example.yourlibrary_v1.More.Utils.addChar;
import static com.example.yourlibrary_v1.More.Utils.removeChar;

public class EditBook extends AppCompatActivity {
    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;
    private String image_uri;
    private StorageReference storageReference;
    private boolean isModEdit = false, uploadPhoto = false;
    private String id_book;
    private EditText title, author, category, releaseDate, rating, pageCount, description;
    private Button save;
    private ImageView book_edit_img, btnToChangePicture;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        storageReference = FirebaseStorage.getInstance().getReference();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add new book");

        btnToChangePicture = findViewById(R.id.changeImageButton);
        btnToChangePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().length() > 2) {
                    if (!uploadPhoto) {
                        DynamicToast.makeWarning(getApplicationContext(), "Select the photo for this book", Toast.LENGTH_SHORT).show();
                        chooseImage();
                        btnToChangePicture.setImageResource(R.drawable.ic_cloud_upload_black_24dp);
                        uploadPhoto = true;
                    } else {
                        uploadPhoto = false;
                        uploadImage();
                        btnToChangePicture.setImageResource(R.drawable.ic_camera_enhance_black_24dp);
                    }
                } else {
                    DynamicToast.makeError(getApplicationContext(), "Before to upload a image set title", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
        rating = findViewById(R.id.book_rating);
        pageCount = findViewById(R.id.book_page);
        description = findViewById(R.id.book_description);


        if (isModEdit)
            save.setText("Edit book");
        else
            save.setText("Add book");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check())
                    saveDataInFirebase();
            }
        });
    }

    // if is in mod edit you make a request to the firebase;
    private void getEditInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("books").child(id_book);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                title.setText(Objects.requireNonNull(dataSnapshot.child("title").getValue()).toString());
                author.setText(Objects.requireNonNull(dataSnapshot.child("author").getValue()).toString());
                category.setText(new Utils().formatCategory(Objects.requireNonNull(dataSnapshot.child("categories").getValue().toString())));
                releaseDate.setText(Objects.requireNonNull(dataSnapshot.child("date_publisher").getValue()).toString());
                image_uri = Objects.requireNonNull(dataSnapshot.child("thumbnail").getValue()).toString();
                Glide.with(getApplicationContext())
                        .load(addChar(image_uri))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(book_edit_img);
                rating.setText(Objects.requireNonNull(dataSnapshot.child("rating").getValue()).toString());
                pageCount.setText(Objects.requireNonNull(dataSnapshot.child("page_count").getValue()).toString());
                description.setText(Objects.requireNonNull(dataSnapshot.child("description").getValue()).toString());
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
        } else if (pageCount.getText().length() < 1) {
            DynamicToast.makeError(getApplicationContext(), "You don't write the page", Toast.LENGTH_SHORT).show();
            return false;
        } else if (description.getText().length() < 10) {
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
        if (image_uri == null)
            image_uri = "http://firebasestorage.googleapis.com/v0/b/yourlibrary-74f11.appspot.com/o/books_images%2Fbook.png?alt=media&token=88d36c38-9f65-4113-966b-e3c4bc393b6a";

        reference.child("title").setValue(title.getText().toString());
        reference.child("author").setValue(author.getText().toString());
        reference.child("categories").setValue(category.getText().toString());
        reference.child("date_publisher").setValue(releaseDate.getText().toString());
        reference.child("thumbnail").setValue(image_uri);
        reference.child("image").setValue(image_uri);
        reference.child("info_link").setValue(image_uri);
        reference.child("rating").setValue(rating.getText().toString());
        reference.child("page_count").setValue(pageCount.getText().toString());
        reference.child("description").setValue(description.getText().toString());
        reference.child("nr_rating").setValue(0);


        if (isModEdit)
            DynamicToast.makeSuccess(getApplicationContext(), "Edited with success!", Toast.LENGTH_SHORT).show();
        else
            DynamicToast.makeSuccess(getApplicationContext(), "Add with success!", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    private void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(EditBook.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("books_images/" + title.getText().toString());

            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            // remove s from link
                                            image_uri = removeChar(uri.toString());
                                        }
                                    });
                                    DynamicToast.makeSuccess(getApplicationContext(),
                                            "Image Uploaded!!",
                                            Toast.LENGTH_SHORT)
                                            .show();
//                                    change_pic_button.setText("Change photo");
//                                    change_pic_button.setBackgroundResource(R.drawable.inactive_btn);
//                                    edit_info_button.setBackgroundResource(R.drawable.inactive_btn);
//                                    edit_info_button.setText("Edit info");
//                                    change_image = false;
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            DynamicToast.makeError(getApplicationContext(), "Failed " + e.getMessage()).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        @NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            System.out.println(data.getData());
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                Objects.requireNonNull(getApplicationContext()).getContentResolver(),
                                filePath);
                book_edit_img.setImageBitmap(bitmap);

            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
            DynamicToast.makeWarning(Objects.requireNonNull(getApplicationContext()), "You selected the photo, please upload").show();
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
}
