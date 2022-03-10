package com.example.cafeteria.Model;

import com.google.gson.annotations.SerializedName;

public class LastNotificationResponse {
    @SerializedName("last_notifications")
    private LastNotificationModel lastNotificationModel;
    @SerializedName("error")
    private boolean error;
    @SerializedName("message_en")
    private String message_en;
    @SerializedName("message_ar")
    private String message_ar;

    public LastNotificationModel getLastNotificationModel() {
        return lastNotificationModel;
    }

    public void setLastNotificationModel(LastNotificationModel lastNotificationModel) {
        this.lastNotificationModel = lastNotificationModel;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage_en() {
        return message_en;
    }

    public void setMessage_en(String message_en) {
        this.message_en = message_en;
    }

    public String getMessage_ar() {
        return message_ar;
    }

    public void setMessage_ar(String message_ar) {
        this.message_ar = message_ar;
    }
}
