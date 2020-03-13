package com.example.yourlibrary_v1.ui.home.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourlibrary_v1.Book;
import com.example.yourlibrary_v1.R;

import java.util.ArrayList;

public class ButtonViewAdapterHome extends RecyclerView.Adapter<ButtonViewAdapterHome.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<String> imageModelArrayList;
    private ArrayList<Book> listBook;
    private ArrayList<View> itemViewList = new ArrayList<>();
    private View root;
    private Context context;

    public ButtonViewAdapterHome(Context ctx, ArrayList<String> imageModelArrayList, ArrayList<Book> listBook, View root) {
        inflater = LayoutInflater.from(ctx);
        this.imageModelArrayList = imageModelArrayList;
        this.listBook = listBook;
        this.root = root;
        context = ctx;
    }

    @NonNull
    @Override
    public ButtonViewAdapterHome.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.cardview_item_category, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ButtonViewAdapterHome.MyViewHolder holder, final int position) {
        final String category = imageModelArrayList.get(position);
        // set selected first element
        if (position == 0) {
            holder.category.setBackgroundResource(R.drawable.border_active_category);
        }

        // add all items in this array
        itemViewList.add(holder.category);

        holder.category.setText(category);
        holder.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set the border for button pressed

                for (View item : itemViewList) {
                    item.setBackgroundResource(R.drawable.border_dezactivated_category);
                }

                // generate the correct list with books, after category
                holder.category.setBackgroundResource(R.drawable.border_active_category);
                ArrayList<Book> good_list_book = new ArrayList<>();
                for (int i = 0; i < listBook.size(); i++) {
                    if (category.equals("ALL")) {
                        good_list_book.add(listBook.get(i));
                    } else {
                        if (category.equals(listBook.get(i).getCategory())) {
                            good_list_book.add(listBook.get(i));
                        }
                    }

                }
                RecyclerView recycler_view_book;
                recycler_view_book = root.findViewById(R.id.recycler_view_id);
                final HomeRecyclerViewAdapter myAdapter = new HomeRecyclerViewAdapter(context, good_list_book);
                recycler_view_book.setLayoutManager(new GridLayoutManager(context, 2));
                recycler_view_book.setAdapter(myAdapter);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageModelArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView category;

        private MyViewHolder(View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.category_item);
        }

    }
}