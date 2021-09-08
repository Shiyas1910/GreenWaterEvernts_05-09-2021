package com.greenwaterevents.android.greenwaterevents.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.greenwaterevents.android.greenwaterevents.adapters.VideoGalleryAdapter;
import com.greenwaterevents.android.greenwaterevents.models.YoutubeVideoModel;
import com.greenwaterevents.android.greenwaterevents.R;
import com.greenwaterevents.android.greenwaterevents.utils.ItemOffsetDecoration;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed Shiyas on 01-02-2018.
 */

public class VideographyFragment extends Fragment {
    private VideoGalleryAdapter videoGalleryAdapter;
    private List<YoutubeVideoModel> youtubeVideoModelList = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;

    public static Fragment newInstance() {
        return new VideographyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // keep the fragment and all its data across screen rotation
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_videography, container, false);
    }

    private void initializeVideoGallery() {
        for (int i = 0; i < 6; i++) {
            youtubeVideoModelList.add(new YoutubeVideoModel());
        }
        videoGalleryAdapter = new VideoGalleryAdapter(getContext(), youtubeVideoModelList);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Videography");
        refreshLayout = view.findViewById(R.id.swiperefresh);

        if (videoGalleryAdapter == null) {
            videoGalleryAdapter = new VideoGalleryAdapter(getContext(), youtubeVideoModelList);

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

            rootRef.child("GreenWaterEvents").child("VideoGalleryModel").orderByChild("order").limitToFirst(4)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                youtubeVideoModelList.clear();
//                                progressBar.setVisibility(View.GONE);
                                YoutubeVideoModel youtubeVideoModel;
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    youtubeVideoModel = postSnapshot.getValue(YoutubeVideoModel.class);
                                    if (youtubeVideoModel != null && youtubeVideoModel.isVisibility()) {
                                        youtubeVideoModelList.add(youtubeVideoModel);
                                    }
                                }
                                videoGalleryAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        videoGalleryAdapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        RecyclerView videoGalleryRecyclerView = view.findViewById(R.id.videoGalleryRecyclerView);
        videoGalleryRecyclerView.setHasFixedSize(true);

        videoGalleryAdapter = new VideoGalleryAdapter(getContext(), youtubeVideoModelList);

        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        videoGalleryRecyclerView.setLayoutManager(gridLayoutManager);

//        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
//        videoGalleryRecyclerView.addItemDecoration(itemDecoration);

        videoGalleryRecyclerView.setAdapter(videoGalleryAdapter);
    }

    @Override
    public void onDestroy() {
        Runtime.getRuntime().gc();
        super.onDestroy();
    }
}
