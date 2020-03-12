package com.example.yourlibrary_v1.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourlibrary_v1.Book;
import com.example.yourlibrary_v1.R;
import com.example.yourlibrary_v1.ui.home.Adapters.HomeRecyclerViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class Fragment_Home_View extends Fragment {
    private SearchView mySearchView;
    private List<Book> lstBook;
    private RecyclerView myrv;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home_view, container, false);

        final ArrayList<String> list;
        final ArrayAdapter<String> adapter;

        lstBook = new ArrayList<>();

        // make connection to firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("books");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String image = (String) messageSnapshot.child("thumbnail").getValue();
                    String title = (String) messageSnapshot.child("title").getValue();
                    String category = (String) messageSnapshot.child("categories").getValue();
                    String description = (String) messageSnapshot.child("description").getValue();
                    String author = (String) messageSnapshot.child("author").getValue();

                    Book ob = new Book(title, category, description, image, author);
                    lstBook.add(ob);
                }

                // generate the list
                mySearchView = (SearchView) root.findViewById(R.id.search_id);
                myrv = (RecyclerView) root.findViewById(R.id.recylclerview_id);
                final HomeRecyclerViewAdapter myAdapter = new HomeRecyclerViewAdapter(getContext(), lstBook);
                myrv.setLayoutManager(new GridLayoutManager(getContext(), 2));
                myrv.setAdapter(myAdapter);

                mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        final HomeRecyclerViewAdapter myAdapter = new HomeRecyclerViewAdapter(getContext(), new Book().filter_book(lstBook, newText));
                        myrv.setAdapter(myAdapter);
                        return false;
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return root;
    }
}
