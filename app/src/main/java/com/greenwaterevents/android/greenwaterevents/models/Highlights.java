package com.greenwaterevents.android.greenwaterevents.models;

/**
 * Created by Mohamed Shiyas on 26-01-2018.
 */

public class Highlights {
    private int thumbnail;

    public Highlights(int cover) {
        this.thumbnail = cover;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
