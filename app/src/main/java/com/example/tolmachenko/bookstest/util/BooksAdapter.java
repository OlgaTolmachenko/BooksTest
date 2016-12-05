package com.example.tolmachenko.bookstest.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tolmachenko.bookstest.R;
import com.example.tolmachenko.bookstest.model.Book;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Olga Tolmachenko on 03.12.16.
 */

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BooksViewHolder> implements AdapterView.OnItemClickListener {

    private Context context;
    private List<Book> booksList;


    public BooksAdapter(Context context, List<Book> booksList) {
        this.context = context;
        this.booksList = booksList;
    }

    @Override
    public BooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View bookView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);
        return new BooksViewHolder(bookView);
    }

    @Override
    public void onBindViewHolder(BooksViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookPreviewLink = booksList.get(position).getInfoLink();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(bookPreviewLink));
                v.getContext().startActivity(intent);
            }
        });
        holder.title.setText(booksList.get(position).getTitle());
        Picasso.with(context)
                .load(booksList.get(position).getThumbnail())
                .error(R.drawable.default_placeholder)
                .placeholder(R.drawable.default_placeholder)
                .transform(new RoundedCornersTransform())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return (booksList != null && booksList.size() != 0) ? booksList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String bookPreviewLink = booksList.get(position).getInfoLink();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(bookPreviewLink));
        view.getContext().startActivity(intent);
    }

    public void loadNewData(List<Book> newBooks) {
        booksList = newBooks;
        notifyDataSetChanged();
    }

    public static class BooksViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnail;
        public TextView title;

        public BooksViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.book_thumbnail);
            title = (TextView) itemView.findViewById(R.id.book_title);
        }
    }
}


