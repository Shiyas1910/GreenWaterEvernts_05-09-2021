package com.greenwaterevents.android.greenwaterevents.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.greenwaterevents.android.greenwaterevents.models.Highlights;
import com.greenwaterevents.android.greenwaterevents.R;
import com.greenwaterevents.android.greenwaterevents.models.PhotographyModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mohamed Shiyas on 28-01-2018.
 */

public class PhotographyAdapter extends RecyclerView.Adapter<PhotographyAdapter.MyViewHolder> {
    private Context mContext;
    private List<PhotographyModel> photographyModelList;

    public PhotographyAdapter(Context context, List<PhotographyModel> photoGalleryModels) {
        this.mContext = context;
        this.photographyModelList = photoGalleryModels;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        CardView cardView;

        MyViewHolder(final View view) {
            super(view);
            thumbnail = view.findViewById(R.id.photographyImages);
            cardView = view.findViewById(R.id.photographyCard);
        }
    }

    @Override
    public PhotographyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photography, parent, false);

        return new PhotographyAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PhotographyAdapter.MyViewHolder holder, int position) {
        final PhotographyModel model = photographyModelList.get(position);
//        holder.thumbnail.setImageResource(highlights.getPhotoUrl());
//        Picasso.with(mContext).load(model.getPhotoUrl()).resize(500, 500).centerInside()
//                .into(holder.thumbnail);

        if (model.isVisibility()) {
            Picasso.with(mContext)
                    .load(model.getPhotoUrl())
                    .tag(mContext)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .resize(500, 500)
                    .centerCrop()
                    .into(holder.thumbnail, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(mContext)
                                    .load(model.getPhotoUrl())
                                    .tag(mContext)
                                    .resize(500, 500)
                                    .centerCrop()
                                    .into(holder.thumbnail);
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return photographyModelList.size();
    }
}
