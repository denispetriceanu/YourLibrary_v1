package com.example.yourlibrary_v1.ui.home.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yourlibrary_v1.Book_Details;
import com.example.yourlibrary_v1.More.Book;
import com.example.yourlibrary_v1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.yourlibrary_v1.ui.home.Adapters.HomeRecyclerViewAdapter.addChar;

public class BookViewAdapter_Fav_and_Readed extends RecyclerView.Adapter<BookViewAdapter_Fav_and_Readed.MyViewHolder> {
    private Context context;
    private ArrayList<Book> book_list;
    // this var we help to know from which page receive the info
    private String which_page;


    public BookViewAdapter_Fav_and_Readed(Context context, ArrayList<Book> lstBook, String which_page) {
        this.context = context;
        this.book_list = lstBook;
        this.which_page = which_page;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cardview_item_book_fav, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.title.setText(book_list.get(position).getTitle());
        // TODO: delete request on server (firebase) to delete this book from list
        // TODO: will be need to send a parameter to know from which list (readed/fav) will be deleted book
        holder.rmv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               deleteBook(book_list.get(position).getId_book());
                book_list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, book_list.size());
                Toast.makeText(context, "Removed:" + getItemCount(), Toast.LENGTH_SHORT).show();
            }
        });

        // convert url to image
        String url = addChar(book_list.get(position).getThumbnail());
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.cover);

        // set onclick listener for card view, and send id of book
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = book_list.get(position).getId_book();
                Intent intent = new Intent(context, Book_Details.class);
                intent.putExtra("book_id", id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return book_list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView cover, rmv_btn;
        TextView title;
        CardView cardView;

        private MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.book_img_fav_id);
            rmv_btn = itemView.findViewById(R.id.btn_remove);
            title = itemView.findViewById(R.id.book_title_fav_id);
            cardView = itemView.findViewById(R.id.cardview_fav_id);

        }

    }
    private void deleteBook(String book_id) {
        DatabaseReference deBook = FirebaseDatabase.getInstance().getReference("favorites");
        deBook=deBook.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        deBook.child(book_id);

        deBook.removeValue();


    }
}

