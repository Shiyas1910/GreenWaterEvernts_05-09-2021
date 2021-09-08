package com.greenwaterevents.android.greenwaterevents.models;

/**
 * Created by Mohamed Shiyas on 08-02-2018.
 */

public class PhotoGalleryModel {
    private String photoUrl;
    private int order;
    private boolean visibility;
    private boolean fullSpan;

    public PhotoGalleryModel() {

    }

    public PhotoGalleryModel(String photoUrl, boolean fullSpan) {
        this.photoUrl = photoUrl;
        this.fullSpan = fullSpan;
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

    public boolean isFullSpan() {
        return fullSpan;
    }

    public void setFullSpan(boolean fullSpan) {
        this.fullSpan = fullSpan;
    }
}
