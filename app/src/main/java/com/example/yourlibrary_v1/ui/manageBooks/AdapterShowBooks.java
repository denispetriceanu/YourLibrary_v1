package com.example.yourlibrary_v1.ui.manageBooks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yourlibrary_v1.More.Book;
import com.example.yourlibrary_v1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.yourlibrary_v1.More.Utils.addChar;

public class AdapterShowBooks extends RecyclerView.Adapter<AdapterShowBooks.MyViewHolder> {
    private ArrayList<Book> listBooks;
    private Context context;

    public AdapterShowBooks(ArrayList<Book> listBooks, Context context) {
        this.listBooks = listBooks;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_book_manage, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("books")
                .child(listBooks.get(position).getId_book());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.titleBook.setText(Objects.requireNonNull(dataSnapshot.child("title").getValue()).toString());
                holder.edit_book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context.getApplicationContext(), EditBook.class)
                                .putExtra("id_book", listBooks.get(position).getId_book()));
                    }
                });
                holder.remove_book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new FancyAlertDialog.Builder((Activity) context)
                                .setTitle("Do you wanna delete this book?")
                                .setBackgroundColor(Color.parseColor("#db1d30"))
                                .setNegativeBtnText("Cancel")
                                .setPositiveBtnBackground(Color.parseColor("#FF4081"))
                                .setPositiveBtnText("Yes")
                                .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))
                                .setPositiveBtnBackground(Color.parseColor("#003300"))
                                .setAnimation(Animation.POP)
                                .isCancellable(true)
                                .setIcon(R.drawable.ic_delete_white, Icon.Visible)
                                .OnPositiveClicked(new FancyAlertDialogListener() {
                                    @Override
                                    public void OnClick() {
                                        // Show Dialog
                                        removeBookFormFirebase(listBooks.get(position).getId_book());
                                        listBooks.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, listBooks.size());
                                        DynamicToast.makeError(context, "Book was delete").show();
                                    }
                                })
                                .OnNegativeClicked(new FancyAlertDialogListener() {
                                    @Override
                                    public void OnClick() {
                                        Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .build();
                    }
                });
                Glide.with(context)
                        .load(addChar(Objects.requireNonNull(dataSnapshot.child("thumbnail").getValue()).toString()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.book_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error Adapter show books: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBooks.size();
    }

    private void removeBookFormFirebase(String id) {
        FirebaseDatabase.getInstance().getReference().child("books")
                .child(id).removeValue();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView book_image;
        TextView titleBook;
        ImageView edit_book, remove_book;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_image = itemView.findViewById(R.id.book_img_item);
            titleBook = itemView.findViewById(R.id.book_title_item);
            edit_book = itemView.findViewById(R.id.edit_book_btn);
            remove_book = itemView.findViewById(R.id.remove_book_btn);
        }
    }
}
