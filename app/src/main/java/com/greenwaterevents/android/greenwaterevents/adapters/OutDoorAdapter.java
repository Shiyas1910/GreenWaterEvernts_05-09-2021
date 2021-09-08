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
 * Created by Mohamed Shiyas on 28-01-2018.
 */

public class OutDoorAdapter extends RecyclerView.Adapter<OutDoorAdapter.MyViewHolder> {
    private Context mContext;
    private List<Highlights> highlightsList;

    public OutDoorAdapter(Context context, List<Highlights> highlights) {
        this.mContext = context;
        this.highlightsList = highlights;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;

        MyViewHolder(final View view) {
            super(view);
            thumbnail = view.findViewById(R.id.outDoorImages);
        }
    }

    @Override
    public OutDoorAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_outdoor, parent, false);

        return new OutDoorAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OutDoorAdapter.MyViewHolder holder, int position) {
        Highlights highlights = highlightsList.get(position);
//        holder.thumbnail.setImageResource(highlights.getPhotoUrl());
        Picasso.with(mContext).load(highlights.getThumbnail()).resize(500, 500).centerInside()
                .transform(new CircleTransformation()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return highlightsList.size();
    }
}

