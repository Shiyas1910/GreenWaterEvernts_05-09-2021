package com.greenwaterevents.android.greenwaterevents.activities;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.greenwaterevents.android.greenwaterevents.R;
import com.greenwaterevents.android.greenwaterevents.adapters.CandidPhotoGalleryAdapter;
import com.greenwaterevents.android.greenwaterevents.models.CandidModel;
import com.greenwaterevents.android.greenwaterevents.models.YoutubeVideoModel;
import com.greenwaterevents.android.greenwaterevents.utils.MyExceptionHandler;

import java.util.ArrayList;
import java.util.List;

public class ServicesActivity extends AppCompatActivity {
    private List<CandidModel> candidModelList;
    private List<YoutubeVideoModel> youtubeVideoModelList;
    private CandidPhotoGalleryAdapter candidPhotoGalleryAdapter;
    private RecyclerView candidPhotoRecyclerView;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        String toolbarTitle = getIntent().getStringExtra("SENDER_KEY");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(toolbarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        rootRef = FirebaseDatabase.getInstance().getReference();
        candidPhotoRecyclerView = findViewById(R.id.candidPhotoRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        candidPhotoRecyclerView.setLayoutManager(layoutManager);

        switch (toolbarTitle) {
            case "Candid Photography":
                setCandidPhotography("CandidPhotos");
                break;
            case "Candid Videography":
                setCandidPhotography("CandidVideos");
                break;
            case "Highlight Video":
                setCandidPhotography("Highlights");
                break;
            case "Teaser":
                setCandidPhotography("Teaser");
                break;
            case "Wedding Fashion Photography":
                setCandidPhotography("WeddingFashionPhotos");
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void setCandidPhotography(final String type) {
        candidModelList = new ArrayList<>();
        youtubeVideoModelList = new ArrayList<>();

        rootRef.child("GreenWaterEvents").child(type).orderByChild("order")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (type.equals("Teaser") || type.equals("Highlights") || type.equals("CandidVideos")) {
                                youtubeVideoModelList.clear();

                                YoutubeVideoModel youtubeVideoModel;
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    youtubeVideoModel = postSnapshot.getValue(YoutubeVideoModel.class);
                                    if (youtubeVideoModel != null && youtubeVideoModel.isVisibility()) {
                                        youtubeVideoModelList.add(youtubeVideoModel);
                                    }
                                }
                            } else {
                                candidModelList.clear();

                                CandidModel candidModel;
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    candidModel = postSnapshot.getValue(CandidModel.class);
                                    if (candidModel != null && candidModel.isVisibility()) {
                                        candidModelList.add(candidModel);
                                    }
                                }
                            }
                            candidPhotoGalleryAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("TAG", databaseError.getMessage());
                    }
                });

        candidPhotoGalleryAdapter = new CandidPhotoGalleryAdapter(this, candidModelList, youtubeVideoModelList);

        candidPhotoRecyclerView.setHasFixedSize(true);
        candidPhotoRecyclerView.setAdapter(candidPhotoGalleryAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
