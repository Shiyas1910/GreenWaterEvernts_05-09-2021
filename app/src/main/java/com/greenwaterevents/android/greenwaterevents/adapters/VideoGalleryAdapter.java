package com.greenwaterevents.android.greenwaterevents.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.greenwaterevents.android.greenwaterevents.models.YoutubeVideoModel;
import com.greenwaterevents.android.greenwaterevents.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.List;

/**
 * Created by Mohamed Shiyas on 04-02-2018.
 */

public class VideoGalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<YoutubeVideoModel> youtubeVideoModels;
    private int screenWidth;
    private int[] videos_carousel = new int[]{
            R.drawable.video_1,
            R.drawable.video_2,
            R.drawable.video_3,
            R.drawable.video_4,
            R.drawable.video_5,
            R.drawable.video_6};

    public VideoGalleryAdapter(Context context, List<YoutubeVideoModel> youtubeVideoModelList) {
        this.mContext = context;
        this.youtubeVideoModels = youtubeVideoModelList;

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    public class VideoInfoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar progressBar;
        private ImageView youTubeThumbnailView;
        private TextView videoName, videoViews, videoDuration;

        VideoInfoHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.homeProgress);
            videoName = itemView.findViewById(R.id.videoTitleText);
            videoViews = itemView.findViewById(R.id.viewsText);
            videoDuration = itemView.findViewById(R.id.durationText);
            youTubeThumbnailView = itemView.findViewById(R.id.youtube_thumbnail);
            youTubeThumbnailView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) mContext, mContext.getResources().getString(R.string.app_ID),
                    youtubeVideoModels.get(getAdapterPosition() - 1).getVideoUrl(), 0, true, true);
            mContext.startActivity(intent);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        CarouselView carouselView;
        ProgressBar progressBar;

        HeaderViewHolder(final View view) {
            super(view);
            carouselView = view.findViewById(R.id.carouselPhotoView);
            progressBar = view.findViewById(R.id.photoProgress);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_header, parent, false);

            return new HeaderViewHolder(itemView);
        }
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_gallery, parent, false);
        return new VideoInfoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int mposition) {
        if (viewHolder instanceof VideoInfoHolder) {
            final VideoInfoHolder videoInfoHolder = (VideoInfoHolder) viewHolder;
            final int position = mposition - 1;
            final YoutubeVideoModel youtubeVideoModel = youtubeVideoModels.get(position);

            Picasso.with(mContext)
                    .load(youtubeVideoModel.getVideoImage())
                    .tag(mContext)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .resize(500, 500)
                    .centerCrop()
                    .into(videoInfoHolder.youTubeThumbnailView, new Callback() {
                        @Override
                        public void onSuccess() {
                            if (videoInfoHolder.progressBar != null) {
                                videoInfoHolder.progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError() {
                            Picasso.with(mContext)
                                    .load(youtubeVideoModel.getVideoImage())
                                    .tag(mContext)
                                    .resize(500, 500)
                                    .centerCrop()
                                    .into(videoInfoHolder.youTubeThumbnailView, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            if (videoInfoHolder.progressBar != null) {
                                                videoInfoHolder.progressBar.setVisibility(View.GONE);
                                            }
                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });
                        }
                    });

            videoInfoHolder.videoName.setText(youtubeVideoModel.getVideoName());
            videoInfoHolder.videoDuration.setText(youtubeVideoModel.getVideoDuration());
            videoInfoHolder.videoViews.setText(String.valueOf(youtubeVideoModel.getViewCount()));

            if (mposition == youtubeVideoModels.size()) {
                FirebaseDatabase.getInstance().getReference()
                        .child("GreenWaterEvents").child("VideoGalleryModel").orderByChild("order")
                        .startAt(youtubeVideoModels.size() + 1).limitToFirst(10)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        YoutubeVideoModel videoModel = postSnapshot.getValue(YoutubeVideoModel.class);
                                        youtubeVideoModels.add(videoModel);
                                    }
                                    VideoGalleryAdapter.this.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        } else {
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
            final StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
            holder.carouselView.setPageCount(videos_carousel.length);

            holder.carouselView.setImageListener(new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    Picasso.with(mContext)
                            .load(videos_carousel[position])
                            .tag(mContext)
                            .resize(screenWidth, (int) (screenWidth / 2.25))
                            .centerInside()
                            .into(imageView);
                }
            });
        }
//        final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
//            @Override
//            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
//                Log.e("ERROR", String.valueOf(errorReason));
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
//
//        holder.youTubeThumbnailView.initialize(mContext.getResources().getString(R.string.app_ID), new YouTubeThumbnailView.OnInitializedListener() {
//            @Override
//            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
//
//                youTubeThumbnailView.animate();
//
//                youTubeThumbnailLoader.setVideo(youtubeVideoModel.getVideoUrl());
//                youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
//
//                if (youTubeThumbnailLoader.hasPrevious()) {
//                    youTubeThumbnailLoader.release();
//                }
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
//                Log.e("ERROR", "Some Failure Occured!");
//            }
//        });
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        }
        return 2;
    }

    @Override
    public int getItemCount() {
        return youtubeVideoModels.size() + 1;
    }
}
