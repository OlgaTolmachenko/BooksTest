package com.example.tolmachenko.bookstest.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tolmachenko.bookstest.R;
import com.example.tolmachenko.bookstest.model.Book;
import com.squareup.picasso.Picasso;

import io.realm.RealmResults;

/**
 * Created by Olga Tolmachenko on 03.12.16.
 */

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BooksViewHolder> implements View.OnClickListener {

    private Context context;
    private RealmResults<Book> booksList;

    public BooksAdapter(Context context, RealmResults<Book> booksList) {
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
    public void onBindViewHolder(BooksViewHolder holder, int position) {

            holder.title.setText(booksList.get(position).getTitle());
            Picasso.with(context)
                    .load(booksList.get(position).getThumbnail())
                    .placeholder(holder.thumbnail.getDrawable())
                    .fit()
                    .transform(new RoundedCornersTransform())
                    .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

    @Override
    public void onClick(View v) {
        //TODO implement onLClick() foreach item
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


