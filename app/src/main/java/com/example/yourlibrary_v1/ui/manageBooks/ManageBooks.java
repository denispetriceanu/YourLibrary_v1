package com.example.yourlibrary_v1.ui.manageBooks;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourlibrary_v1.More.Book;
import com.example.yourlibrary_v1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ManageBooks extends Fragment {
    private ArrayList<Book> list_book;
    private RecyclerView recyclerView;

    @Override
    public void onStart() {
        getBooks();
        super.onStart();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_manage_books, container, false);
        setHasOptionsMenu(true);

        list_book = new ArrayList<>();
        recyclerView = root.findViewById(R.id.listBooksManager);
        FloatingActionButton btn = root.findViewById(R.id.addBook);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditBook.class));
            }
        });

        return root;
    }

    private void getBooks() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("books");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot book : dataSnapshot.getChildren()) {
                    list_book.add(new Book(Objects.requireNonNull(book.child("title").getValue()).toString(), book.getKey()));
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                AdapterShowBooks adapterShowBooks = new AdapterShowBooks(list_book, getContext());
                recyclerView.setAdapter(adapterShowBooks);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error Manage Books: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        System.out.println("I'm here in search");
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                System.out.println("AICI");
                return false;
            }
        });
        SearchManager searchManager = (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        assert searchView != null;
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final AdapterShowBooks myAdapter = new AdapterShowBooks(new Book().filter_book(list_book, newText), getContext());
                recyclerView.setAdapter(myAdapter);
                return false;
            }
        });
    }
}
