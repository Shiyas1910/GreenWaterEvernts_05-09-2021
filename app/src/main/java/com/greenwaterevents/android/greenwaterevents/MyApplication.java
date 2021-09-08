package com.greenwaterevents.android.greenwaterevents;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.google.firebase.database.FirebaseDatabase;
import com.greenwaterevents.android.greenwaterevents.reciever.ConnectivityReceiver;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Cache;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

/**
 * Created by Mohamed Shiyas on 08-02-2018.
 */

public class MyApplication extends Application {
    private static MyApplication mInstance;
    private static final int PICASSO_DISK_CACHE_SIZE = 1024 * 1024 * 10;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        FacebookSdk.sdkInitialize(getApplicationContext());

        Cache memoryCache = new LruCache(PICASSO_DISK_CACHE_SIZE);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Picasso.Builder builder = new Picasso.Builder(this).memoryCache(memoryCache);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        Picasso.setSingletonInstance(built);

    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }
}
