package com.example.yourlibrary_v1.ui.home.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yourlibrary_v1.Book;
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

        //TODO: in last project you call Book_Activity but now you need to create a fragment and call him
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, Book_Activity.class);
//                intent.putExtra("Title", mData.get(position).getTitle());
//                intent.putExtra("Categories", mData.get(position).getCategory());
//                intent.putExtra("Description", mData.get(position).getDescription());
//                System.out.println(mData.get(position).getThumbnail());
//                intent.putExtra("Thumbnail", addChar(mData.get(position).getThumbnail()));
//
//                mContext.startActivities(new Intent[]{intent});
//            }
//
//        });
        holder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book itemLabel = mData.get(position);
                mData.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mData.size());
//                Toast.makeText(mContext,"Removed:" + itemLabel,Toast.LENGTH_SHORT).show();

            }
        });
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
        ImageButton mRemoveButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_book_title = (TextView) itemView.findViewById(R.id.book_title_id);
            book_author = (TextView) itemView.findViewById(R.id.book_aurhor_id);
            img_book_thumbnail = (ImageView) itemView.findViewById(R.id.book_img_id);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
            mRemoveButton = (ImageButton) itemView.findViewById((R.id.ib_remove));
        }
    }

}
