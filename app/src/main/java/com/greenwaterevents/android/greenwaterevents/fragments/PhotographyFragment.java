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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.greenwaterevents.android.greenwaterevents.adapters.PhotoGalleryAdapter;
import com.greenwaterevents.android.greenwaterevents.models.PhotoGalleryModel;
import com.greenwaterevents.android.greenwaterevents.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed Shiyas on 31-01-2018.
 */

public class PhotographyFragment extends Fragment { //implements ImagePasser {
    private PhotoGalleryAdapter photoGalleryAdapter;
    private List<PhotoGalleryModel> photoGalleryList = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;

    public static PhotographyFragment newInstance() {
        return new PhotographyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photography, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Photography");
        refreshLayout = view.findViewById(R.id.swiperefresh);

//        initializePhotoGallery();

        if (photoGalleryAdapter == null) {
            photoGalleryAdapter = new PhotoGalleryAdapter(getContext(), photoGalleryList);

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

            rootRef.child("GreenWaterEvents").child("PhotoGalleryModel").orderByChild("order")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                photoGalleryList.clear();
//                                progressBar.setVisibility(View.GONE);
                                PhotoGalleryModel photoGalleryModel;
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    photoGalleryModel = postSnapshot.getValue(PhotoGalleryModel.class);
                                    if (photoGalleryModel != null && photoGalleryModel.isVisibility()) {
                                        photoGalleryList.add(photoGalleryModel);
                                    }
                                }
                                photoGalleryAdapter.notifyDataSetChanged();
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
                        photoGalleryAdapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        RecyclerView photoGalleryRecyclerView = view.findViewById(R.id.photoGalleryRecyclerView);
        photoGalleryRecyclerView.setHasFixedSize(true);
        photoGalleryRecyclerView.setItemViewCacheSize(20);
        photoGalleryRecyclerView.setDrawingCacheEnabled(true);
        photoGalleryRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        photoGalleryRecyclerView.setLayoutManager(gridLayoutManager);
        photoGalleryRecyclerView.setAdapter(photoGalleryAdapter);
    }

    private void initializePhotoGallery() {
        for (int i = 0; i < 6; i++) {
            photoGalleryList.add(new PhotoGalleryModel());
        }
        photoGalleryAdapter = new PhotoGalleryAdapter(getContext(), photoGalleryList);
    }

//    @Override
//    public void passImage(int pos) {
//    }

    @Override
    public void onDestroy() {
        Runtime.getRuntime().gc();
        super.onDestroy();
    }

}
