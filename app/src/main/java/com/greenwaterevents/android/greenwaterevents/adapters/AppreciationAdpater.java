package com.greenwaterevents.android.greenwaterevents.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.greenwaterevents.android.greenwaterevents.models.Highlights;
import com.greenwaterevents.android.greenwaterevents.R;
import com.greenwaterevents.android.greenwaterevents.utils.CircleTransformation;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mohamed Shiyas on 29-01-2018.
 */

public class AppreciationAdpater extends RecyclerView.Adapter<AppreciationAdpater.MyViewHolder> {
    private Context mContext;
    private List<Highlights> highlightsList;

    public AppreciationAdpater(Context context, List<Highlights> highlights) {
        this.mContext = context;
        this.highlightsList = highlights;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;

        MyViewHolder(final View view) {
            super(view);
            thumbnail = view.findViewById(R.id.appreciationImages);
        }
    }

    @Override
    public AppreciationAdpater.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appreciation, parent, false);

        return new AppreciationAdpater.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AppreciationAdpater.MyViewHolder holder, int position) {
        Highlights highlights = highlightsList.get(position);
        Picasso.with(mContext).load(highlights.getThumbnail()).resize(400, 150).centerInside()
                .transform(new CircleTransformation()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return highlightsList.size();
    }
}

