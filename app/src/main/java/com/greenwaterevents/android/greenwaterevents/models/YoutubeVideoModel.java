package com.greenwaterevents.android.greenwaterevents.models;

/**
 * Created by Mohamed Shiyas on 05-02-2018.
 */

public class YoutubeVideoModel {
    private String videoName;
    private String videoUrl;
    private String videoImage;
    private int viewCount;
    private String videoDuration;
    private int order;
    private boolean visibility;

    public YoutubeVideoModel() {

    }

    public YoutubeVideoModel(String videoName, String videoUrl, int views, String duration) {
        this.videoName = videoName;
        this.videoUrl = videoUrl;
        this.viewCount = views;
        this.videoDuration = duration;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
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

    public String getVideoImage() {
        return videoImage;
    }

    public void setVideoImage(String videoImage) {
        this.videoImage = videoImage;
    }
}
