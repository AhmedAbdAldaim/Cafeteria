package com.example.cafeteria.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoriesResponse {
    @SerializedName("categories")
    private List<CategoriesModel> categoriesModelList;
    @SerializedName("error")
    private boolean error;
    @SerializedName("status")
    private String status;
    @SerializedName("message_en")
    private String message_en;
    @SerializedName("message_ar")
    private String message_ar;

    public List<CategoriesModel> getCategoriesModelList() {
        return categoriesModelList;
    }

    public void setCategoriesModelList(List<CategoriesModel> categoriesModelList) {
        this.categoriesModelList = categoriesModelList;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
