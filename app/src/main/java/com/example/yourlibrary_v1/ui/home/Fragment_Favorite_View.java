package com.example.yourlibrary_v1.ui.home;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourlibrary_v1.More.Book;
import com.example.yourlibrary_v1.R;
import com.example.yourlibrary_v1.ui.home.Adapters.BookViewAdapter_Fav_and_Readed;
import com.example.yourlibrary_v1.ui.home.Adapters.HomeRecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class Fragment_Favorite_View extends Fragment {
     ArrayList<Book> lstBook ;
     RecyclerView recycler_view_book;
     DatabaseReference dbFavs;
     HomeRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_view, container, false);
    }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
            super.onViewCreated(view, savedInstanceState);
            lstBook = new ArrayList<>();
            recycler_view_book=view.findViewById(R.id.recycler_view_fav_id);
            adapter = new HomeRecyclerViewAdapter(getActivity(), lstBook);

            recycler_view_book.setHasFixedSize(true);
            recycler_view_book.setLayoutManager(new LinearLayoutManager(getActivity()));
            recycler_view_book.setAdapter(adapter);

            if(FirebaseAuth.getInstance().getCurrentUser() == null){
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, new HomeFragment())
                        .commit();
                return;
            }

            dbFavs =FirebaseDatabase.getInstance().getReference("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("favourites");

            dbFavs.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                        for(DataSnapshot messageSnapshot: dataSnapshot.getChildren()){

                            String image = (String) messageSnapshot.child("thumbnail").getValue();
                            String title = (String) messageSnapshot.child("title").getValue();
                            String category = (String) messageSnapshot.child("categories").getValue();
                            String description = (String) messageSnapshot.child("description").getValue();
                            String author = (String) messageSnapshot.child("author").getValue();

                            Book b= new Book( image, title, category, description, author);
                            b.isFavourite = true;
                            lstBook.add(b);
                        }

                        adapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
}
