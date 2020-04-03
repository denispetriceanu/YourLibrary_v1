package com.example.yourlibrary_v1.ui.home.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import java.util.List;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Book> mData;

    public HomeRecyclerViewAdapter(Context mContext, List<Book> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public static String addChar(String str) {
        return str.substring(0, 4) + "s" + str.substring(4);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.cardview_item_book, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.tv_book_title.setText(mData.get(position).getTitle());
        holder.book_author.setText(mData.get(position).getAuthor());
        String url = mData.get(position).getThumbnail();
        url = addChar(url);

        Glide.with(mContext)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img_book_thumbnail);

        // TODO: send correct id book
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String id = "01";
                // open new fragment for view more details about book
                Intent intent = new Intent(mContext, Book_Details.class);
                intent.putExtra("book_id", mData.get(position).getId_book());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        TextView tv_book_title;
        TextView book_author;
        ImageView img_book_thumbnail;
        CardView cardView;
        CheckBox checkBoxFav;
        Context mCtx;

        private MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_book_title = itemView.findViewById(R.id.book_title_id);
            book_author = itemView.findViewById(R.id.book_aurhor_id);
            img_book_thumbnail = itemView.findViewById(R.id.book_img_id);
            cardView = itemView.findViewById(R.id.cardview_id);
            checkBoxFav = itemView.findViewById(R.id.checkbox_favorite);
            checkBoxFav.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(FirebaseAuth.getInstance().getCurrentUser() == null ){
                Toast.makeText(mCtx, "Please login first", Toast.LENGTH_LONG).show();
                buttonView.setChecked(false);
                return;
            }
//
//            int position = getAdapterPosition();
//            Book b= mData.get(position);
//
//            DatabaseReference dbFavs = FirebaseDatabase.getInstance().getReference("users")
//                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                    .child("favourites")
//                    .child(b.category);
//

//            if(isChecked){
//                dbFavs.child(b.id).setValue(b);
//            }else{
//                dbFavs.child(b.id).setValue(null);
//            }

        }
    }

}
