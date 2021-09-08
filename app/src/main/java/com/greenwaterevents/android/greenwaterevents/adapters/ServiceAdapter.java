package com.greenwaterevents.android.greenwaterevents.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greenwaterevents.android.greenwaterevents.activities.HomeScreen;
import com.greenwaterevents.android.greenwaterevents.activities.ServicesActivity;
import com.greenwaterevents.android.greenwaterevents.models.ServiceModel;
import com.greenwaterevents.android.greenwaterevents.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mohamed on 06-02-2018.
 */

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> {
    private Context mContext;
    private List<ServiceModel> serviceModelList;
    private String intentString;

    public ServiceAdapter(Context context, List<ServiceModel> modelList) {
        this.mContext = context;
        this.serviceModelList = modelList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout serviceContainer;
        ImageView serviceIcon, imageItem;
        TextView serviceName, resultCount;
        View divider;

        MyViewHolder(final View view) {
            super(view);
            serviceContainer = view.findViewById(R.id.serviceContainer);
            imageItem = view.findViewById(R.id.imageItem);
            serviceIcon = view.findViewById(R.id.serviceIcon);
            serviceName = view.findViewById(R.id.serviceText);
            resultCount = view.findViewById(R.id.resultCount);
            divider = view.findViewById(R.id.divider);
        }
    }

    @Override
    public ServiceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_services, parent, false);

        return new ServiceAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ServiceAdapter.MyViewHolder holder, final int position) {
        ServiceModel serviceModel = serviceModelList.get(position);
        Picasso.with(mContext).load(serviceModel.getServiceBackground()).resize(600, 300)
                .centerInside()
                .into(holder.imageItem);
        holder.serviceIcon.setImageResource(serviceModel.getServiceIcon());
        holder.serviceName.setText(serviceModel.getServiceName());
        holder.resultCount.setText(serviceModel.getResultCount());

        if (position == serviceModelList.size() - 1) {
            holder.divider.setVisibility(View.GONE);
        }

        holder.serviceContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, ServicesActivity.class);
                switch (position) {
                    case 0:
                        intentString = "Candid Photography";
                        break;
                    case 1:
                        intentString = "Highlight Video";
                        break;
                    case 2:
                        intentString = "Teaser";
                        break;
                    case 3:
                        intentString = "Wedding Fashion Photography";
                        break;
                    default:
                        intentString = "";
                        break;

                }
                i.putExtra("SENDER_KEY", intentString);
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return serviceModelList.size();
    }
}


