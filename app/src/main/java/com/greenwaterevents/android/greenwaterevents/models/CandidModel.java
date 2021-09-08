package com.greenwaterevents.android.greenwaterevents.models;

/**
 * Created by Mohamed Shiyas on 24-02-2018.
 */

public class CandidModel {
    private String videoUrl;
    private String photoUrl;
    private int order;
    private boolean visibility;
    private String description;

    public CandidModel() {

    }

    public CandidModel(String videoUrl, String photoUrl, int order, boolean visibility, String description) {
        this.videoUrl = videoUrl;
        this.photoUrl = photoUrl;
        this.order = order;
        this.visibility = visibility;
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
