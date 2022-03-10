package com.example.cafeteria.Network;
import com.example.cafeteria.Model.AddItemResponse;
import com.example.cafeteria.Model.CategoriesResponse;
import com.example.cafeteria.Model.DeleteItemResponse;
import com.example.cafeteria.Model.EditItemResponse;
import com.example.cafeteria.Model.EditProfileResponse;
import com.example.cafeteria.Model.ItemByCategoryIdResponse;
import com.example.cafeteria.Model.ItemsResponse;
import com.example.cafeteria.Model.LastNotificationResponse;
import com.example.cafeteria.Model.LoginResponse;
import com.example.cafeteria.Model.LogoutResponse;
import com.example.cafeteria.Model.NotificationsResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
public interface RequestInterface {

    // <-- Login Api-->
    @FormUrlEncoded
    @POST("login/{mac}")
    Call<LoginResponse> Login(
            @Path("mac") String device_id,
            @Field("phone") String phone,
            @Field("password") String password);

    // <-- Edit Profiel Api -->
    @FormUrlEncoded
    @PUT("profile/edit")
    Call<EditProfileResponse> EditProfile(
            @Field("phone") String phone,
            @Field("cafeteria") String cafeteria,
            @Field("location") String location,
            @Field("password") String password,
            @Header("Authorization") String authorization);

    // <-- Edit Password Api -->
    @FormUrlEncoded
    @PUT("profile/edit")
    Call<EditProfileResponse> EditProfilePassword(
            @Field("password") String password,
            @Header("Authorization") String authorization);

    // <-- Get All Categories Api -->
    @GET("allcategories")
    Call<CategoriesResponse> GetCategories(
            @Header("Authorization") String authorization);

    // <-- Get All Categories Spinner -->
    @GET("categories")
    Call<CategoriesResponse> GetCategories_Spinner(
            @Header("Authorization") String authorization);


    // <-- Get All Items By Category id  Api -->
    @GET("categories/{category_id}")
    Call<ItemByCategoryIdResponse> GetItemByCategoryId(
            @Path("category_id") String category_id,
            @Header("Authorization") String authorization);

    // <-- Add Item Api -->
    @FormUrlEncoded
    @POST("item")
    Call<AddItemResponse> AddItem(
            @Field("name") String name,
            @Field("price") String price,
            @Field("category_id") String category_id,
            @Header("Authorization") String authorization);

    // <-- Edit Item Api -->
    @FormUrlEncoded
    @PUT("item/{item_id}")
    Call<EditItemResponse> EditItem(
            @Path("item_id") String item_id,
            @Field("name") String name,
            @Field("price") String price,
            @Field("category_id") String category_id,
            @Header("Authorization") String authorization);

    // <-- Delete Item Api -->
    @DELETE("item/{item_id}")
    Call<DeleteItemResponse> DeleteItem(
            @Path("item_id") String item_id,
            @Header("Authorization") String authorization);

    // <-- Get All Notifications Api -->
    @GET("notifications")
    Call<NotificationsResponse> GelAllNotifications(
            @Header("Authorization") String authorization);

    // <-- Get Last Notification Api -->
    @GET("last/notifications")
    Call<LastNotificationResponse> GetLastNotification(
            @Header("Authorization") String authorization);

    // <-- Logout Api -->
    @POST("logoutApi")
    Call<LogoutResponse> LogoutApi(
            @Header("Authorization") String authorization);
}


