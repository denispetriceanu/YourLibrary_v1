package com.example.yourlibrary_v1.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.yourlibrary_v1.R;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        // this line two lines is for change color of bottom-menu
        final BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) root.findViewById(R.id.nav_view_home);

        final BottomNavigationItemView home = root.findViewById(R.id.navigation_home);
        final BottomNavigationItemView search = root.findViewById(R.id.navigation_search);
        final BottomNavigationItemView profile = root.findViewById(R.id.navigation_profile);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment_Home_View fragment = new Fragment_Home_View();
                fragmentTransaction.add(R.id.nav_host_fragment_home, fragment);
                fragmentTransaction.commit();
                // change the color of element when is active
                bottomNavigationView.setSelectedItemId(R.id.navigation_home);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment_Search_View fragment = new Fragment_Search_View();
                fragmentTransaction.add(R.id.nav_host_fragment_home, fragment);
                fragmentTransaction.commit();
                // change the color of element when is active
                bottomNavigationView.setSelectedItemId(R.id.navigation_search);

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment_Profile_View fragment = new Fragment_Profile_View();
                fragmentTransaction.add(R.id.nav_host_fragment_home, fragment);
                fragmentTransaction.commit();
                // change the color of element when is active
                bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
            }
        });
        return root;
    }
}
