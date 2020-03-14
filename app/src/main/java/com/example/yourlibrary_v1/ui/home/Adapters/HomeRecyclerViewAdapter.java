package com.example.yourlibrary_v1.ui.home.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yourlibrary_v1.Book;
import com.example.yourlibrary_v1.Book_Details;
import com.example.yourlibrary_v1.R;

import java.util.List;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Book> mData;

    public HomeRecyclerViewAdapter(Context mContext, List<Book> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_book, parent, false);
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

        // TODO: pass the id books for download from firebase;
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open new fragment for view more details about book
                Intent intent = new Intent(mContext, Book_Details.class);
                mContext.startActivity(intent);
            }
        });

//        holder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Book itemLabel = mData.get(position);
//                mData.remove(position);
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, mData.size());
//                Toast.makeText(mContext,"Removed:" + itemLabel,Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private String addChar(String str) {
        return str.substring(0, 4) + "s" + str.substring(4);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_book_title;
        TextView book_author;
        ImageView img_book_thumbnail;
        CardView cardView;
//        ImageButton mRemoveButton;

        private MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_book_title = itemView.findViewById(R.id.book_title_id);
            book_author = itemView.findViewById(R.id.book_aurhor_id);
            img_book_thumbnail = itemView.findViewById(R.id.book_img_id);
            cardView = itemView.findViewById(R.id.cardview_id);
//            mRemoveButton = itemView.findViewById((R.id.ib_remove));
        }
    }

}
