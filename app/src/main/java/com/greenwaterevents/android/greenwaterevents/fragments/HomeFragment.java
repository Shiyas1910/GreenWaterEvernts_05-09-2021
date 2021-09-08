package com.greenwaterevents.android.greenwaterevents.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.widget.LikeView;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.greenwaterevents.android.greenwaterevents.adapters.AppreciationAdpater;
import com.greenwaterevents.android.greenwaterevents.adapters.OutDoorAdapter;
import com.greenwaterevents.android.greenwaterevents.adapters.PhotographyAdapter;
import com.greenwaterevents.android.greenwaterevents.adapters.VideographyAdapter;
import com.greenwaterevents.android.greenwaterevents.listener.ClickListener;
import com.greenwaterevents.android.greenwaterevents.models.Highlights;
import com.greenwaterevents.android.greenwaterevents.models.OutdoorModel;
import com.greenwaterevents.android.greenwaterevents.models.PhotographyModel;
import com.greenwaterevents.android.greenwaterevents.models.YoutubeVideoModel;
import com.greenwaterevents.android.greenwaterevents.R;
import com.greenwaterevents.android.greenwaterevents.utils.ItemOffsetDecoration;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mohamed Shiyas on 31-01-2018.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {
    private List<Highlights> appreciationList;
    private List<Highlights> outDoorList;
    private PhotographyAdapter photographyAdapter;
    private VideographyAdapter videographyAdapter;
    private OutDoorAdapter outDoorAdapter;
    private AppreciationAdpater appreciationAdpater;
    private TextView customerComments, customerNames;
    private Animation fadeIn, fadeOut;
    private static ClickListener clickListener1;
    int[] covers_carousel = new int[]{
            R.drawable.nav_header,
            R.drawable.highlight1,
            R.drawable.home_2,
            R.drawable.highlight2,
            R.drawable.highlight3,
            R.drawable.highlight4};
    private List<YoutubeVideoModel> youtubeVideoModelList = new ArrayList<>();
    private List<PhotographyModel> photographyModels = new ArrayList<>();
    public static String FACEBOOK_URL = "https://www.facebook.com/GreenWatermovies";
    public static String FACEBOOK_PAGE_ID = "637963462974822";


    public static HomeFragment newInstance(ClickListener clickListener) {
        HomeFragment homeFragment = new HomeFragment();
        clickListener1 = clickListener;
        return homeFragment;
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Green Water Events");

        customerComments = view.findViewById(R.id.customerAppreciationDesc);
        customerNames = view.findViewById(R.id.customerName);
        CardView fb_button = view.findViewById(R.id.fb_button);
        CardView youtube_button = view.findViewById(R.id.youtube_button);
        final TextView photoExpand = view.findViewById(R.id.photogaraphyExpand);
        final TextView videoExpand = view.findViewById(R.id.videogaraphyExpand);
//        button = view.findViewById(R.id.channelButton);
//        button.setOnClickListener(this);
//
//        initInstances(view);
//        initCallbackManager();
//        refreshButtonsState();

        fb_button.setOnClickListener(this);
        youtube_button.setOnClickListener(this);


        initializePhotographyCarosal();
        initializeYoutubeCarosal();

        RecyclerView photographyRecyclerView = view.findViewById(R.id.photographyRecyclerView);
        RecyclerView videographyRecyclerView = view.findViewById(R.id.videographyRecyclerView);
        RecyclerView outDoorRecyclerView = view.findViewById(R.id.outdoorRecyclerView);
        RecyclerView appreciationRecyclerView = view.findViewById(R.id.appreciationrRecyclerView);

        photographyRecyclerView.setHasFixedSize(true);
        videographyRecyclerView.setHasFixedSize(true);
        outDoorRecyclerView.setHasFixedSize(true);
        appreciationRecyclerView.setHasFixedSize(true);

        outDoorList = new ArrayList<>();
        appreciationList = new ArrayList<>();

        photographyAdapter = new PhotographyAdapter(getContext(), photographyModels);
        outDoorAdapter = new OutDoorAdapter(getContext(), outDoorList);
        appreciationAdpater = new AppreciationAdpater(getContext(), appreciationList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        photographyRecyclerView.setLayoutManager(layoutManager);

        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        videographyRecyclerView.setLayoutManager(gridLayoutManager);

        LinearLayoutManager getmLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        outDoorRecyclerView.setLayoutManager(getmLayoutManager);

        SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
        snapHelperStart.attachToRecyclerView(photographyRecyclerView);

        SnapHelper snapHelperStart1 = new GravitySnapHelper(Gravity.START);
        snapHelperStart1.attachToRecyclerView(outDoorRecyclerView);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset2);
        photographyRecyclerView.addItemDecoration(itemDecoration);
        videographyRecyclerView.addItemDecoration(itemDecoration);

        ItemOffsetDecoration itemDecoration1 = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        outDoorRecyclerView.addItemDecoration(itemDecoration1);

        photographyRecyclerView.setAdapter(photographyAdapter);
        videographyRecyclerView.setAdapter(videographyAdapter);
        outDoorRecyclerView.setAdapter(outDoorAdapter);
        appreciationRecyclerView.setAdapter(appreciationAdpater);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.child("GreenWaterEvents").child("VideoGalleryModel").orderByChild("order").limitToFirst(6)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            youtubeVideoModelList.clear();
                            videoExpand.setVisibility(View.VISIBLE);
                            YoutubeVideoModel youtubeVideoModel;
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                youtubeVideoModel = postSnapshot.getValue(YoutubeVideoModel.class);
                                if (youtubeVideoModel != null && youtubeVideoModel.isVisibility()) {
                                    youtubeVideoModelList.add(youtubeVideoModel);
                                }
                            }
                            videographyAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        rootRef.child("GreenWaterEvents").child("PhotographyModel").orderByChild("order").limitToFirst(15)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            photographyModels.clear();
                            photoExpand.setVisibility(View.VISIBLE);
                            PhotographyModel photographyModel;
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                photographyModel = postSnapshot.getValue(PhotographyModel.class);
                                if (photographyModel != null && photographyModel.isVisibility()) {
                                    photographyModels.add(photographyModel);
                                }
                            }
                            photographyAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

//        rootRef.child("GreenWaterEvents").child("OutdoorPhotos").orderByChild("order")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            outDoorList.clear();
//                            OutdoorModel outdoorModel;
//                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                                outdoorModel = postSnapshot.getValue(OutdoorModel.class);
//                                if (outdoorModel != null && outdoorModel.isVisibility()) {
//                                    outDoorList.add(outdoorModel);
//                                }
//                            }
//                            outDoorAdapter.notifyDataSetChanged();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });

        final String[] customerCommentsArray = getContext().getResources().getStringArray(R.array.appreciations);
        final String[] customerNamesArray = getContext().getResources().getStringArray(R.array.customerNames);

        CarouselView carouselView = view.findViewById(R.id.carouselView);

        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(covers_carousel[position]);
            }
        });
        carouselView.setPageCount(covers_carousel.length);

        customerComments.setText(customerCommentsArray[0]);
        customerNames.setText(customerNamesArray[0]);

        final CarouselLayoutManager carouselLayoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL,
                true);
        carouselLayoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        appreciationRecyclerView.setLayoutManager(carouselLayoutManager);
        appreciationRecyclerView.setHasFixedSize(true);
        appreciationRecyclerView.addOnScrollListener(new CenterScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {
                    customerComments.startAnimation(fadeOut);
                    customerComments.setVisibility(View.INVISIBLE);
                    customerNames.startAnimation(fadeOut);
                    customerNames.setVisibility(View.INVISIBLE);
                }
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    customerComments.setText(customerCommentsArray[carouselLayoutManager.getCenterItemPosition()]);
                    customerComments.startAnimation(fadeIn);
                    customerComments.setVisibility(View.VISIBLE);
                    customerNames.setText(customerNamesArray[carouselLayoutManager.getCenterItemPosition()]);
                    customerNames.startAnimation(fadeIn);
                    customerNames.setVisibility(View.VISIBLE);
                }
            }
        });

        photoExpand.setOnClickListener(this);
        videoExpand.setOnClickListener(this);

        fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);

        prepareOutDoor();
        prepareAppreciationItems();
    }

    private void initializeYoutubeCarosal() {
        for (int i = 0; i < 6; i++) {
            youtubeVideoModelList.add(new YoutubeVideoModel());
        }
        videographyAdapter = new VideographyAdapter(getContext(), youtubeVideoModelList);
    }

    private void initializePhotographyCarosal() {
        for (int i = 0; i < 6; i++) {
            photographyModels.add(new PhotographyModel());
        }
        photographyAdapter = new PhotographyAdapter(getContext(), photographyModels);
    }

    private void prepareAppreciationItems() {
        int[] covers = new int[]{
                R.drawable.customer1,
                R.drawable.customer2,
                R.drawable.customer3,
                R.drawable.customer4,
                R.drawable.sample5,
                R.drawable.sample6,
                R.drawable.sample7,
                R.drawable.sample8,
                R.drawable.sample9};
        for (int cover : covers) {
            Highlights a = new Highlights(cover);
            appreciationList.add(a);
        }
        appreciationAdpater.notifyDataSetChanged();
    }

    private void prepareOutDoor() {
        int[] covers = new int[]{
                R.drawable.outdoor1,
                R.drawable.outdoor2,
                R.drawable.outdoor3,
                R.drawable.outdoor4,
                R.drawable.outdoor5};
        for (int cover : covers) {
            Highlights a = new Highlights(cover);
            outDoorList.add(a);
        }
        outDoorAdapter.notifyDataSetChanged();
    }

//    private void initInstances(View view) {
//        btnLoginToLike = view.findViewById(R.id.btnToLogin);
//        loginButton = view.findViewById(R.id.login_button);
//        likeView = view.findViewById(R.id.likeView);
//        likeView.setLikeViewStyle(LikeView.Style.STANDARD);
//        likeView.setFragment(this);
//        likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);
//
//        String pageUrlToLike = "https://www.facebook.com/GreenWatermovies";
//        likeView.setObjectIdAndType(pageUrlToLike, LikeView.ObjectType.OPEN_GRAPH);
//
//
//        btnLoginToLike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile"));
//            }
//        });
//    }

//    private void initCallbackManager() {
//        callbackManager = CallbackManager.Factory.create();
//        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                refreshButtonsState();
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException e) {
//
//            }
//        });
//    }
//
//    private void refreshButtonsState() {
//        if (!isLoggedIn()) {
//            loginButton.setVisibility(View.VISIBLE);
//            likeView.setVisibility(View.GONE);
//        } else {
//            loginButton.setVisibility(View.GONE);
//            likeView.setVisibility(View.VISIBLE);
//        }
//    }
//
//    public boolean isLoggedIn() {
//        return AccessToken.getCurrentAccessToken() != null;
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // Handle Facebook Login Result
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.photogaraphyExpand:
                clickListener1.getClick("photo");
                break;
            case R.id.videogaraphyExpand:
                clickListener1.getClick("video");
                break;
            case R.id.youtube_button:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/channel/UC2xgxcJ_GQDbbvpN8vgDyWQ?sub_confirmation=1"));
                startActivity(intent);
                break;
            case R.id.fb_button:
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getFacebookPageURL(getContext())));
                startActivity(facebookIntent);
                break;
        }
    }

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            boolean activated = packageManager.getApplicationInfo("com.facebook.katana", 0).enabled;
            if (activated) {
                if (versionCode >= 3002850) { //newer versions of fb app
//                    return "fb://facewebmodal/f?href=" + FACEBOOK_PAGE_ID;
                    return "fb://page/" + FACEBOOK_PAGE_ID;
                } else { //older versions of fb app
                    return "fb://page/" + FACEBOOK_PAGE_ID;
                }
            } else {
                return FACEBOOK_URL;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    @Override
    public void onDestroy() {
        Runtime.getRuntime().gc();
        super.onDestroy();
    }
}
