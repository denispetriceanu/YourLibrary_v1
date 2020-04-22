package com.example.yourlibrary_v1.ui.manageBooks;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yourlibrary_v1.R;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

public class EditBook extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String id_book = getIntent().getStringExtra("id_book");
        // from here you can delete, this is for testing
        if (id_book != null)
            DynamicToast.makeSuccess(getApplicationContext(), "Book:" + id_book).show();
        setContentView(R.layout.activity_edit_book);
    }
}
