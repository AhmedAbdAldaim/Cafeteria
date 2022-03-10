package com.example.cafeteria.Model;

import com.google.gson.annotations.SerializedName;

public class LastNotificationModel {
    @SerializedName("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}