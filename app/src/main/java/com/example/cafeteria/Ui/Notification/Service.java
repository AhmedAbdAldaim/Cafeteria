package com.example.cafeteria.Ui.Notification;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.example.cafeteria.Local_DB.LocalSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cafeteria.Local_DB.NotificationLocalSession;
import com.example.cafeteria.Model.LastNotificationResponse;
import com.example.cafeteria.Network.ApiClient;
import com.example.cafeteria.Network.RequestInterface;
import com.example.cafeteria.R;

import com.example.cafeteria.Ui.Activity.MainActivity;


import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Callback;

public class Service extends IntentService {
    public static boolean service_run = false;
    RequestQueue requestQueue;
    public static int newid=0 ;
    String name,notification,url;
    NotificationLocalSession notificationLocalSession;

    public Service() {
        super("mywebrequestservice");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        notificationLocalSession = new NotificationLocalSession(this);

        while (service_run) {
            final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
            Call<LastNotificationResponse> call = requestInterface.GetLastNotification("Bearer " + LocalSession.getToken());
            call.enqueue(new Callback<LastNotificationResponse>()
            {
                @Override
                public void onResponse(Call<LastNotificationResponse> call, retrofit2.Response<LastNotificationResponse> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().isError())
                        {
                            newid = Integer.parseInt(response.body().getLastNotificationModel().getId());
                            url = "http://cafeterias.herokuapp.com/api/user/new/notifications/" + newid;

                            if(newid>NotificationLocalSession.getIdNot()&&notificationLocalSession.getIdNot()!=0)
                            {
                                int count_of_notifications = newid-notificationLocalSession.getIdNot();
                                MainActivity.chipNavigationBar.showBadge(R.id.notification,count_of_notifications);
                            }
                            else if(notificationLocalSession.getIdNot()==0)
                            {
                                notificationLocalSession.createSession(newid);
                            }

                        } else {
                        }
                    } else {
                    }
                }

                @Override
                public void onFailure(Call<LastNotificationResponse> call, Throwable t) {
                }
            });


            try {
             notificationLocalSession.getIdNot();

                    name = "";
                    notification = "";
                    requestQueue = Volley.newRequestQueue(Service.this);
                    final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                JSONArray jsonArray = jsonObject.getJSONArray("new_notifications");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    name += object.getString("name");
                                    notification += object.getString("notification");
                                    newid = object.getInt("id");
                                    if(newid>NotificationLocalSession.getIdNot() &&  object.length() != 0)
                                    {
                                        int count_of_notifications = newid-notificationLocalSession.getIdNot();
                                        MainActivity.chipNavigationBar.showBadge(R.id.notification,count_of_notifications);
                                    }

                                    Intent intent1 = new Intent(Service.this, BroadCast.class);
                                    intent1.setAction("com.example.Broadcast");
                                    intent1.putExtra("name", name);
                                    intent1.putExtra("notification", notification);
                                    sendBroadcast(intent1);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("Authorization","Bearer "+ LocalSession.getToken());
                            return hashMap;
                        }
                    };
                    requestQueue.add(jsonObjectRequest);

                } catch (Exception e) {
                notificationLocalSession.getIdNot();

                name = "";
                notification = "";
                requestQueue = Volley.newRequestQueue(Service.this);
                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("new_notifications");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                name += object.getString("name");
                                notification += object.getString("notification");
                                newid = object.getInt("id");
                                if(newid>NotificationLocalSession.getIdNot() &&  object.length() != 0)
                                {
                                    int count_of_notifications = newid-notificationLocalSession.getIdNot();
                                    MainActivity.chipNavigationBar.showBadge(R.id.notification,count_of_notifications);
                                }

                                Intent intent1 = new Intent(Service.this, BroadCast.class);
                                intent1.setAction("com.example.Broadcast");
                                intent1.putExtra("name", name);
                                intent1.putExtra("notification", notification);
                                sendBroadcast(intent1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String,String> hashMap = new HashMap<>();
                        hashMap.put("Authorization","Bearer "+ LocalSession.getToken());
                        return hashMap;
                    }
                };
                requestQueue.add(jsonObjectRequest);
                }
                try {
                    Thread.sleep(10000);
                } catch (Exception ex) {
                }
            }
        }
    }
