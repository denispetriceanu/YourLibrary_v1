package com.example.yourlibrary_v1.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourlibrary_v1.Book;
import com.example.yourlibrary_v1.R;
import com.example.yourlibrary_v1.ui.home.Adapters.ButtonViewAdapterHome;
import com.example.yourlibrary_v1.ui.home.Adapters.HomeRecyclerViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class Fragment_Home_View extends Fragment {
    private static ArrayList<Book> listBook;
    private static View root_copy;
    private static Context context;
    private ArrayList<Book> lstBook;
    private ArrayList<String> lstCategory;
    private RecyclerView recycler_view_book;

    public static ArrayList<Book> getLstBook() {
        return listBook;
    }

    public static View getRoot() {
        return root_copy;
    }

    public static Context getContextM() {
        return context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home_view, container, false);
        root_copy = root;
        context = getContext();
        lstBook = new ArrayList<>();
        lstCategory = new ArrayList<>();
        // we add first category
        lstCategory.add("ALL");

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
                    assert author != null;
                    // remove " and []
                    author = author.replace("[", "").replace("]", "").replace("\"", "");
                    // get just first author
                    if (!author.equals("-")) {
                        if (author.contains(",")) {
                            author = author.replace(author.substring(author.indexOf(",")), "");
                        }
                    }
                    // truncate the title
                    assert title != null;
                    if (title.length() > 27) {
                        title = title.replace(title.substring(27), "");
                        title = title.concat("...");
                    }

                    // generate the list of categories
                    System.out.println("Category:" + category);
                    assert category != null;
                    category = category.replace("]", "").replace("[", "").replace("\"", "");
                    if (category.equals("-")) category = "Undefined";
                    category = category.toUpperCase();
                    if (!lstCategory.contains(category)) lstCategory.add(category);

                    // generate the element book and add to object
                    Book ob = new Book(title, category, description, image, author);
                    lstBook.add(ob);
                }

                // show the list with books
                recycler_view_book = root.findViewById(R.id.recycler_view_id);
                final HomeRecyclerViewAdapter myAdapter = new HomeRecyclerViewAdapter(getContext(), lstBook);
                recycler_view_book.setLayoutManager(new GridLayoutManager(getContext(), 2));
                recycler_view_book.setAdapter(myAdapter);

                // sort lstCategory alphabetical
                Collections.sort(lstCategory);

                // show the list with categories
                RecyclerView recycler_view_category = root.findViewById(R.id.recycler_home_category);
                ButtonViewAdapterHome adapter = new ButtonViewAdapterHome(getContext(), lstCategory, lstBook, root);
                recycler_view_category.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                recycler_view_category.setAdapter(adapter);

                // make a copy for search box
                listBook = lstBook;
//
//                mySearchView = getActivity().findViewById(R.id.app_bar_search);
//                mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                    @Override
//                    public boolean onQueryTextSubmit(String query) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onQueryTextChange(String newText) {
//                        final HomeRecyclerViewAdapter myAdapter = new HomeRecyclerViewAdapter(getContext(), new Book().filter_book(lstBook, newText));
//                        Fragment_Home_View.this.recycler_view_book.setAdapter(myAdapter);
//                        return false;
//                    }
//                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return root;
    }
}
