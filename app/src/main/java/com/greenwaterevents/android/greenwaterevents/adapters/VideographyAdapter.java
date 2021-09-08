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
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.greenwaterevents.android.greenwaterevents.models.YoutubeVideoModel;
import com.greenwaterevents.android.greenwaterevents.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mohamed Shiyas on 28-01-2018.
 */

public class VideographyAdapter extends RecyclerView.Adapter<VideographyAdapter.MyViewHolder> {
    private Context mContext;
    private List<YoutubeVideoModel> youtubeVideoModels;
    private int screenWidth;

    public VideographyAdapter(Context context, List<YoutubeVideoModel> youtubeVideoModelList) {
        this.mContext = context;
        this.youtubeVideoModels = youtubeVideoModelList;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView thumbnail;
        ProgressBar progressBar;
        ImageView youTubeThumbnailView;
        TextView videoViews, videoDuration;

        MyViewHolder(final View view) {
            super(view);
//            thumbnail = view.findViewById(R.id.videographyImages);

            progressBar = itemView.findViewById(R.id.homeProgress_home);
            videoViews = itemView.findViewById(R.id.viewsText_home);
            videoDuration = itemView.findViewById(R.id.durationText_home);
            youTubeThumbnailView = itemView.findViewById(R.id.youtube_thumbnail_home);
            youTubeThumbnailView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (youtubeVideoModels.get(getAdapterPosition()).getVideoUrl() != null) {
                Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) mContext, mContext.getResources().getString(R.string.app_ID),
                        youtubeVideoModels.get(getAdapterPosition()).getVideoUrl());
                mContext.startActivity(intent);
            }
        }
    }

    @Override
    public VideographyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_videography, parent, false);

        return new VideographyAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VideographyAdapter.MyViewHolder holder, int position) {
        final YoutubeVideoModel youtubeVideoModel = youtubeVideoModels.get(position);

//        final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
//            @Override
//            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
//
//            }
//
//            @Override
//            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
//                youTubeThumbnailView.setVisibility(View.VISIBLE);
//                if (holder.progressBar != null) {
//                    holder.progressBar.setVisibility(View.GONE);
//                }
//            }
//        };
//        if(youtubeVideoModel.getVideoUrl()!=null) {
//            holder.youTubeThumbnailView.initialize(mContext.getResources().getString(R.string.app_ID), new YouTubeThumbnailView.OnInitializedListener() {
//                @Override
//                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
//
//                    youTubeThumbnailView.animate();
//
//                    youTubeThumbnailLoader.setVideo(youtubeVideoModel.getVideoUrl());
//                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
//
//                    if (youTubeThumbnailLoader.hasPrevious()) {
//                        youTubeThumbnailLoader.release();
//                    }
//                }
//
//                @Override
//                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
//                    //write something for failure
//                }
//            });
//        }

        Picasso.with(mContext)
                .load(youtubeVideoModel.getVideoImage())
                .tag(mContext)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .resize(500, 600)
                .centerCrop()
                .into(holder.youTubeThumbnailView, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (holder.progressBar != null) {
                            holder.progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError() {
                        Picasso.with(mContext)
                                .load(youtubeVideoModel.getVideoImage())
                                .tag(mContext)
                                .resize(500, 600)
                                .centerCrop()
                                .into(holder.youTubeThumbnailView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        if (holder.progressBar != null) {
                                            holder.progressBar.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });
                    }
                });

        holder.videoDuration.setText(youtubeVideoModel.getVideoDuration());
        holder.videoViews.setText(String.valueOf(youtubeVideoModel.getViewCount()));
    }

    @Override
    public int getItemCount() {
        return youtubeVideoModels.size();
    }
}
