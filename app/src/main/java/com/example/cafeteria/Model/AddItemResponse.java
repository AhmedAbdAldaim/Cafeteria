package com.example.cafeteria.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddItemResponse {
    @SerializedName("item")
    private AddItemModel addItemModel;
    @SerializedName("error")
    private boolean error;
    @SerializedName("message_en")
    private String message_en;
    @SerializedName("message_ar")
    private String message_ar;

    public AddItemModel getAddItemModel() {
        return addItemModel;
    }

    public void setAddItemModel(AddItemModel addItemModel) {
        this.addItemModel = addItemModel;
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
