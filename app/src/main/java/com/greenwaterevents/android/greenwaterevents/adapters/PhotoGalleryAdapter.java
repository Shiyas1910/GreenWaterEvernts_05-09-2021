package com.greenwaterevents.android.greenwaterevents.adapters;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.dmallcott.dismissibleimageview.DismissibleImageView;
import com.greenwaterevents.android.greenwaterevents.models.PhotoGalleryModel;
import com.greenwaterevents.android.greenwaterevents.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.List;

/**
 * Created by Mohamed Shiyas on 31-01-2018.
 */

public class PhotoGalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<PhotoGalleryModel> photoGalleryModelList;
    //    private ImagePasser imagePasser;
    private int screenWidth;
    private int[] photos_carousel = new int[]{
            R.drawable.photo_1,
            R.drawable.photo_2,
            R.drawable.photo_3,
            R.drawable.photo_4,
            R.drawable.photo_5,
            R.drawable.collapsing_toolbar1};

    public PhotoGalleryAdapter(Context context, List<PhotoGalleryModel> photoGalleryModels) {
        this.mContext = context;
        this.photoGalleryModelList = photoGalleryModels;
//        this.imagePasser = imagePasser;

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        DismissibleImageView thumbnail;
        CardView cardView;

        MyViewHolder(final View view) {
            super(view);
            thumbnail = view.findViewById(R.id.photoGalleryImages);
            cardView = view.findViewById(R.id.photoGalleryCardView);
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
                .inflate(R.layout.item_photo_gallery, parent, false);

        return new PhotoGalleryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int mposition) {
        if (viewHolder instanceof MyViewHolder) {
            final MyViewHolder holder = (MyViewHolder) viewHolder;
            final int position = mposition - 1;
            final PhotoGalleryModel photoGalleryModel = photoGalleryModelList.get(position);

            final StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            holder.cardView.setPreventCornerOverlap(false);
            if (photoGalleryModel.isVisibility()) {
                if (!photoGalleryModel.isFullSpan()) {
                    holder.cardView.setMinimumHeight((screenWidth / 2) - 5);
                    Picasso.with(mContext)
                            .load(photoGalleryModel.getPhotoUrl())
                            .tag(mContext)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .resize(screenWidth / 2, screenWidth / 2)
                            .centerCrop()
                            .into(holder.thumbnail, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(mContext)
                                            .load(photoGalleryModel.getPhotoUrl())
                                            .tag(mContext)
                                            .resize(screenWidth / 2, screenWidth / 2)
                                            .centerCrop()
                                            .into(holder.thumbnail);
                                }
                            });
                    Log.e("SCREEN WIDTH false", screenWidth / 2 + "");

                    layoutParams.setFullSpan(false);
                } else {
                    holder.cardView.setMinimumHeight((int) (screenWidth / 1.25) - 15);
                    Picasso.with(mContext)
                            .load(photoGalleryModel.getPhotoUrl())
                            .tag(mContext)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .resize(screenWidth, (int) (screenWidth / 1.25))
                            .centerCrop()
                            .into(holder.thumbnail, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(mContext)
                                            .load(photoGalleryModel.getPhotoUrl())
                                            .tag(mContext)
                                            .resize(screenWidth, (int) (screenWidth / 1.25))
                                            .centerCrop()
                                            .into(holder.thumbnail);
                                }
                            });
                    Log.e("SCREEN WIDTH true", screenWidth / 1.25 + "");

                    layoutParams.setFullSpan(true);
                }
            }
//            if (mposition == photoGalleryModelList.size()) {
//                FirebaseDatabase.getInstance().getReference()
//                        .child("GreenWaterEvents").child("PhotoGalleryModel").orderByChild("order")
//                        .startAt(photoGalleryModelList.size() + 1).limitToFirst(10)
//                        .addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                if (dataSnapshot.exists()) {
//                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                                        PhotoGalleryModel photoGalleryModel = postSnapshot.getValue(PhotoGalleryModel.class);
//                                        if (photoGalleryModel != null && photoGalleryModel.isVisibility()) {
//                                            photoGalleryModelList.add(photoGalleryModel);
//                                        }
//                                    }
//                                    PhotoGalleryAdapter.this.notifyDataSetChanged();
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//            }
        } else {
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
            final StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
            holder.carouselView.setPageCount(photos_carousel.length);

            holder.carouselView.setImageListener(new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    Picasso.with(mContext)
                            .load(photos_carousel[position])
                            .tag(mContext)
                            .resize(screenWidth, (int) (screenWidth / 2.25))
                            .centerInside()
                            .into(imageView);
                }
            });
        }
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
        return photoGalleryModelList.size() + 1;
    }
}
