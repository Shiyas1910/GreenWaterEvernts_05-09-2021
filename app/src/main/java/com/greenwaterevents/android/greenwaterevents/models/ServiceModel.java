package com.greenwaterevents.android.greenwaterevents.models;

/**
 * Created by mohamed on 06-02-2018.
 */

public class ServiceModel {
    private String serviceName;
    private String resultCount;
    private int serviceIcon;
    private int serviceBackground;

    public ServiceModel(String serviceName, String resultCount, int serviceIcon, int serviceBackground) {
        this.serviceName = serviceName;
        this.resultCount = resultCount;
        this.serviceIcon = serviceIcon;
        this.serviceBackground = serviceBackground;
    }

    public int getServiceBackground() {
        return serviceBackground;
    }

    public void setServiceBackground(int serviceBackground) {
        this.serviceBackground = serviceBackground;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getResultCount() {
        return resultCount;
    }

    public void setResultCount(String resultCount) {
        this.resultCount = resultCount;
    }

    public int getServiceIcon() {
        return serviceIcon;
    }

    public void setServiceIcon(int serviceIcon) {
        this.serviceIcon = serviceIcon;
    }
}
