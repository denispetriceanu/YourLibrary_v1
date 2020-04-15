package com.example.yourlibrary_v1.ui.manageBooks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.yourlibrary_v1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ManageBooks extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_manage_books, container, false);
        TextView textView = root.findViewById(R.id.text_slideshow);
        textView.setText("Slideshow");

        FloatingActionButton btn = root.findViewById(R.id.addBook);
        // todo: open new activity for add book
        return root;
    }
}
