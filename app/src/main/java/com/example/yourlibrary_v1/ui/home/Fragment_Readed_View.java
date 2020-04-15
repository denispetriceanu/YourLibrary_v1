package com.example.yourlibrary_v1.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourlibrary_v1.More.Book;
import com.example.yourlibrary_v1.R;
import com.example.yourlibrary_v1.ui.home.Adapters.BookViewAdapter_Fav_and_Readed;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;
import java.util.Objects;


public class Fragment_Readed_View extends Fragment {

    private ArrayList<Book> lstBook;
    private RecyclerView recycler_view_book;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        lstBook.removeAll(lstBook);
        // every time when de app restart we check if recycler is empty, if is not then we will make empty
        if (recycler_view_book != null) {
            recycler_view_book.removeAllViews();
        }

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new HomeFragment())
                    .commit();
            return;
        }

        DatabaseReference dbFav = FirebaseDatabase.getInstance().getReference("read")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dbFav.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot id_book : dataSnapshot.getChildren()) {
                    showBooksRed(id_book.getKey());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lstBook = new ArrayList<>();
        this.view = view;
    }


    private void showBooksRed(final String id_book) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("books").child(id_book);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = (String) dataSnapshot.child("title").getValue();
                String image = (String) dataSnapshot.child("image").getValue();
                String category = (String) dataSnapshot.child("categories").getValue();
                String author = (String) dataSnapshot.child("author").getValue();

                Book b = new Book(title, category, id_book, image, author);
                System.out.println(b.toString());
                lstBook.add(b);

                recycler_view_book = view.findViewById(R.id.recycler_view_fav_id);
                final BookViewAdapter_Fav_and_Readed myAdapter = new BookViewAdapter_Fav_and_Readed(getContext(), lstBook, "read");
                recycler_view_book.setLayoutManager(new GridLayoutManager(getContext(), 2));
                recycler_view_book.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                DynamicToast.makeWarning(Objects.requireNonNull(getContext()), "Something went wrong")
                        .show();
            }
        });
    }
}


