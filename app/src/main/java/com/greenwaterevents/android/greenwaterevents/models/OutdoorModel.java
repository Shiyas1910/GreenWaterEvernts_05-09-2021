package com.greenwaterevents.android.greenwaterevents.models;

/**
 * Created by Mohamed Shiyas on 18-03-2018.
 */

public class OutdoorModel {
    private String photoUrl;
    private int order;
    private boolean visibility;

    public OutdoorModel() {

    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
}
