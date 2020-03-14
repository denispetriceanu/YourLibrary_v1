package com.example.yourlibrary_v1.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.yourlibrary_v1.R;


public class Fragment_Favorite_View extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_favorite_view, container, false);
        TextView txt = root.findViewById(R.id.textView3);
        return root;
    }
}
