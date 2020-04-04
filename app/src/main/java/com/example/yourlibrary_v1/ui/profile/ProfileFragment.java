package com.example.yourlibrary_v1.ui.profile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yourlibrary_v1.More.User_model;
import com.example.yourlibrary_v1.R;
import com.example.yourlibrary_v1.log.login;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class ProfileFragment extends Fragment {
    private final int PICK_IMAGE_REQUEST = 22;
    private boolean change_image = false, change_info = false;
    private ImageView image_upload;
    private Button change_pic_button, edit_info_button;
    private EditText name, phone, email, address;
    private String name_, phone_, email_, address_;
    private StorageReference storageReference;
    private Uri filePath;
    private FirebaseAuth mAuth;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getUid() == null) {
            startActivity(new Intent(getContext(), login.class));
        }

        // get info about storage Firebase
        storageReference = FirebaseStorage.getInstance().getReference();

        init_textView(root);
        get_data_firebase();

        change_pic_button = root.findViewById(R.id.changePicture);
        edit_info_button = root.findViewById(R.id.edit_info);
        edit_info_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (!change_image) {
                    if (!change_info) {
                        propertyChange();
                        change_info = true;
                        edit_info_button.setText("Send Info");
                        edit_info_button.setBackgroundResource(R.drawable.active_btn);
                        change_pic_button.setText("Cancel");
                        change_pic_button.setBackgroundResource(R.drawable.active_btn);
                    } else {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference().child("users");
                        myRef.child(Objects.requireNonNull(mAuth.getUid())).setValue(new User_model(name.getText().toString(),
                                email.getText().toString(), phone.getText().toString(), address.getText().toString()));
                        DynamicToast.makeSuccess(Objects.requireNonNull(getContext()), "Modificated with success").show();
                        changeFormatBtn();
                    }
                } else {
                    chooseImage();
                }
            }
        });

        change_pic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!change_image) {
                    if (change_info)
                        restore_info();
                    else
                        chooseImage();
                } else
                    uploadImage();
            }
        });
        return root;
    }

    private void restore_info() {
        name.setText(name_);
        email.setText(email_);
        phone.setText(phone_);
        address.setText(address_);
        DynamicToast.makeWarning(Objects.requireNonNull(getContext()), "Unsaved changes").show();
        changeFormatBtn();
    }

    // this function I user for retrieve initial design buttons
    @SuppressLint("SetTextI18n")
    private void changeFormatBtn() {
        change_info = false;
        edit_info_button.setText("Edit info");
        edit_info_button.setBackgroundResource(R.drawable.inactive_btn);
        change_pic_button.setText("Change picture");
        change_pic_button.setBackgroundResource(R.drawable.inactive_btn);

        name.setClickable(false);
        name.setFocusableInTouchMode(false);
        name.setCursorVisible(false);
        name.setFocusable(false);

        phone.setClickable(false);
        phone.setFocusableInTouchMode(false);
        phone.setCursorVisible(false);
        phone.setFocusable(false);

        address.setClickable(false);
        address.setFocusableInTouchMode(false);
        address.setCursorVisible(false);
        address.setFocusable(false);
    }

    private void propertyChange() {
        name.setClickable(true);
        name.setFocusableInTouchMode(true);
        name.setCursorVisible(true);
        name.setFocusable(true);

        email.setClickable(true);
        email.setFocusableInTouchMode(true);
        email.setCursorVisible(true);
        email.setFocusable(true);

        phone.setClickable(true);
        phone.setFocusableInTouchMode(true);
        phone.setCursorVisible(true);
        phone.setFocusable(true);

        address.setClickable(true);
        address.setFocusableInTouchMode(true);
        address.setCursorVisible(true);
        address.setFocusable(true);
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @SuppressLint("SetTextI18n")
    @Override
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
                                Objects.requireNonNull(getActivity()).getContentResolver(),
                                filePath);
                image_upload.setImageBitmap(bitmap);
                image_upload.setVisibility(View.VISIBLE);
                change_pic_button.setBackgroundResource(R.drawable.active_btn);
                change_pic_button.setText("Upload photo");
                edit_info_button.setBackgroundResource(R.drawable.active_btn);
                edit_info_button.setText("Another photo");
                change_image = true;
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
            DynamicToast.makeWarning(Objects.requireNonNull(getContext()), "You selected the photo, please upload").show();
        }
    }


    private void init_textView(View root) {
        name = root.findViewById(R.id.name_profile);
        phone = root.findViewById(R.id.phone_profile);
        email = root.findViewById(R.id.email_profile);
        address = root.findViewById(R.id.location_profile);
        image_upload = root.findViewById(R.id.image_profile);
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        storageReference.child(Objects.requireNonNull("images/" + mAuth.getUid())).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(Objects.requireNonNull(getContext()))
                                .load(uri)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(image_upload);
                    }
                });
    }

    private void get_data_firebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        try {
            DatabaseReference myRef = database.getReference("users").child(Objects.requireNonNull(mAuth.getUid()));
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    name_ = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                    name.setText(name_);
                    email_ = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();
                    email.setText(email_);
                    phone_ = Objects.requireNonNull(dataSnapshot.child("mobile_nr").getValue()).toString();
                    phone.setText(phone_);
                    address_ = Objects.requireNonNull(dataSnapshot.child("adress").getValue()).toString();
                    address.setText(address_);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        } catch (Exception e) {
            System.out.println(e.toString());
            DynamicToast.makeWarning(Objects.requireNonNull(getContext()), "You are not connected").show();
        }
    }

    private void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            StorageReference ref = storageReference.child("images/" + mAuth.getUid());

            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    DynamicToast.makeSuccess(Objects.requireNonNull(getContext()),
                                            "Image Uploaded!!",
                                            Toast.LENGTH_SHORT)
                                            .show();
                                    change_pic_button.setText("Change photo");
                                    change_pic_button.setBackgroundResource(R.drawable.inactive_btn);
                                    edit_info_button.setBackgroundResource(R.drawable.inactive_btn);
                                    edit_info_button.setText("Edit info");
                                    change_image = false;
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            DynamicToast.makeError(Objects.requireNonNull(getContext()), "Failed " + e.getMessage()).show();
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
}

