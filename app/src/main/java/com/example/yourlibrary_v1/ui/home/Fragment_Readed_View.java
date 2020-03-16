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
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourlibrary_v1.Book;
import com.example.yourlibrary_v1.R;
import com.example.yourlibrary_v1.ui.home.Adapters.BookViewAdapter_Fav_and_Readed;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class Fragment_Readed_View extends Fragment {

    private ArrayList<Book> lstBook = new ArrayList<>();
    private RecyclerView recycler_view_book;
    private View root;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         root = inflater.inflate(R.layout.fragment_readed_view, container, false);
        FirebaseDatabase database= FirebaseDatabase.getInstance();
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

                    // generate the element book and add to object
                    Book ob = new Book(title, category, description, image, author);
                    lstBook.add(ob);
                }
                recycler_view_book = root.findViewById(R.id.recycler_view_fav_id);
                final BookViewAdapter_Fav_and_Readed myAdapter = new BookViewAdapter_Fav_and_Readed(getContext(), lstBook, "fav");
                context = getContext();
                recycler_view_book.setLayoutManager(new GridLayoutManager(getContext(), 2));
                recycler_view_book.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return root;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchManager searchManager = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);
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
                recycler_view_book = root.findViewById(R.id.recycler_view_fav_id);
                final BookViewAdapter_Fav_and_Readed myAdapter = new BookViewAdapter_Fav_and_Readed(getContext(), new Book().filter_book(lstBook, newText), "fav");
                context = getContext();
                recycler_view_book.setLayoutManager(new GridLayoutManager(getContext(), 2));
                recycler_view_book.setAdapter(myAdapter);
                return false;
            }
        });
    }
}

