package com.greenwaterevents.android.greenwaterevents.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dmallcott.dismissibleimageview.DismissibleImageView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.greenwaterevents.android.greenwaterevents.R;
import com.greenwaterevents.android.greenwaterevents.models.CandidModel;
import com.greenwaterevents.android.greenwaterevents.models.YoutubeVideoModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mohamed Shiyas on 24-02-2018.
 */

public class CandidPhotoGalleryAdapter extends RecyclerView.Adapter<CandidPhotoGalleryAdapter.MyViewHolder> {
    private Context mContext;
    private List<CandidModel> candidModelList;
    private List<YoutubeVideoModel> youtubeVideoModelList;
    private int screenWidth;

    public CandidPhotoGalleryAdapter(Context context, List<CandidModel> candidModels, List<YoutubeVideoModel> youtubeVideoModels) {
        this.mContext = context;
        this.candidModelList = candidModels;
        this.youtubeVideoModelList = youtubeVideoModels;

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        CardView cardView;
        TextView candidPhotoDesc, videoDuration;
        ProgressBar progressBar;
        RelativeLayout textViewContainer;

        MyViewHolder(final View view) {
            super(view);
            thumbnail = view.findViewById(R.id.candidPhotos);
            cardView = view.findViewById(R.id.candidPhotoCardView);
            candidPhotoDesc = view.findViewById(R.id.candidPhotoDesc);
            videoDuration = view.findViewById(R.id.videoDuration);
            progressBar = view.findViewById(R.id.homeProgress);
            textViewContainer = view.findViewById(R.id.desc_container);
        }
    }

    @Override
    public CandidPhotoGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_candid_photgraphy, parent, false);

        return new CandidPhotoGalleryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CandidPhotoGalleryAdapter.MyViewHolder holder, int position) {
        if (candidModelList.size() != 0) {
//            holder.textViewContainer.setVisibility(View.GONE);
            final CandidModel candidModel = candidModelList.get(position);
            if (candidModel.isVisibility()) {
                Picasso.with(mContext)
                        .load(candidModel.getPhotoUrl())
                        .tag(mContext)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .resize(screenWidth, 500)
                        .centerInside()
                        .into(holder.thumbnail, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(mContext)
                                        .load(candidModel.getPhotoUrl())
                                        .tag(mContext)
                                        .resize(screenWidth, 500)
                                        .centerInside()
                                        .into(holder.thumbnail);
                            }
                        });

//                holder.candidPhotoDesc.setText(candidModel.getDescription());
                holder.textViewContainer.setVisibility(View.GONE);
            }
        } else if (youtubeVideoModelList.size() != 0) {
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.videoDuration.setVisibility(View.VISIBLE);
            final YoutubeVideoModel youtubeVideoModel = youtubeVideoModelList.get(position);
            if (youtubeVideoModel.isVisibility()) {
                Picasso.with(mContext)
                        .load(youtubeVideoModel.getVideoImage())
                        .tag(mContext)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .resize(screenWidth, 500)
                        .centerCrop()
                        .into(holder.thumbnail, new Callback() {
                            @Override
                            public void onSuccess() {
                                holder.progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                Picasso.with(mContext)
                                        .load(youtubeVideoModel.getVideoImage())
                                        .tag(mContext)
                                        .resize(screenWidth, 500)
                                        .centerCrop()
                                        .into(holder.thumbnail, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                holder.progressBar.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onError() {

                                            }
                                        });
                            }
                        });

                holder.videoDuration.setText(youtubeVideoModel.getVideoDuration());
                holder.candidPhotoDesc.setText(youtubeVideoModel.getVideoName());
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) mContext, mContext.getResources().getString(R.string.app_ID),
                                youtubeVideoModel.getVideoUrl());
                        mContext.startActivity(intent);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        if (youtubeVideoModelList.size() != 0) {
            return youtubeVideoModelList.size();
        } else if (candidModelList.size() != 0) {
            return candidModelList.size();
        } else {
            return 0;
        }
    }
}
