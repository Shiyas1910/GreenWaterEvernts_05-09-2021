package com.greenwaterevents.android.greenwaterevents.models;

/**
 * Created by Mohamed Shiyas on 13-02-2018.
 */

public class PhotographyModel {
    private String photoUrl;
    private int order;

    public PhotographyModel() {

    }

    public PhotographyModel(String photoUrl, int order, boolean visibility) {
        this.photoUrl = photoUrl;
        this.order = order;
        this.visibility = visibility;
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

    private boolean visibility;
}
