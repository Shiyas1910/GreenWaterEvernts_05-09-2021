package com.greenwaterevents.android.greenwaterevents.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greenwaterevents.android.greenwaterevents.adapters.ServiceAdapter;
import com.greenwaterevents.android.greenwaterevents.models.ServiceModel;
import com.greenwaterevents.android.greenwaterevents.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed Shiyas on 31-01-2018.
 */

public class ServicesFragment extends Fragment {
    private List<ServiceModel> serviceModelList;
    private ServiceAdapter serviceAdapter;

    public static ServicesFragment newInstance()
    {
        return new ServicesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_services_new, container, false);

        serviceModelList = new ArrayList<>();

        RecyclerView serviceRecyclerView = v.findViewById(R.id.serviceRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        serviceRecyclerView.setLayoutManager(layoutManager);

        serviceAdapter = new ServiceAdapter(getContext(), serviceModelList);
        serviceRecyclerView.setAdapter(serviceAdapter);

        prepareService();

        return v;
    }

    private void prepareService() {
        ServiceModel serviceModel = new ServiceModel("Candid Photography",
                "3456 Results", R.drawable.camera_icon, R.drawable.sample11);
        serviceModelList.add(serviceModel);
//        serviceModel = new ServiceModel("Candid Videography",////////
//                "3126 Results", R.drawable.video_icon, R.drawable.sample4);
//        serviceModelList.add(serviceModel);
        serviceModel = new ServiceModel("Highlight Video",
                "2167 Results", R.drawable.highlights_icon, R.drawable.sample5);
        serviceModelList.add(serviceModel);
        serviceModel = new ServiceModel("Teaser",
                "1778 Results", R.drawable.teaser_icon, R.drawable.sample6);
        serviceModelList.add(serviceModel);
        serviceModel = new ServiceModel("Wedding Fashion Photography",
                "1180 Results",R.drawable.fashion_icon, R.drawable.sample7);
        serviceModelList.add(serviceModel);
        serviceAdapter.notifyDataSetChanged();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Our Services");
    }

    @Override
    public void onDestroy() {
        Runtime.getRuntime().gc();
        super.onDestroy();
    }
}
