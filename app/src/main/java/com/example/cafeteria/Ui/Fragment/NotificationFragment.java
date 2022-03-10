package com.example.cafeteria.Ui.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cafeteria.Local_DB.LocalSession;
import com.example.cafeteria.Model.CategoriesModel;
import com.example.cafeteria.Model.CategoriesResponse;
import com.example.cafeteria.Model.NotificationsResponse;
import com.example.cafeteria.Network.ApiClient;
import com.example.cafeteria.Network.RequestInterface;
import com.example.cafeteria.R;
import com.example.cafeteria.Ui.Activity.AddItem;
import com.example.cafeteria.Ui.Activity.MainActivity;
import com.example.cafeteria.Ui.Adapter.Categories_Adapter;
import com.example.cafeteria.Ui.Adapter.Notifications_Adapter;
import com.example.cafeteria.Utilty.Utility;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    Notifications_Adapter notifications_adapter;
    LinearLayout liner_empty_date;
    ImageView  img_bad_connection;
    TextView tv_connect;
    Button button_connect;
    ShimmerFrameLayout shimmerFrameLayout;
    LocalSession localSession;

    private static final String TAG_server = "Server";
    private static final String Tag_failure = "failure";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.notification_fragment, container, false);

        img_bad_connection = root.findViewById(R.id.img_bad_connection);
        tv_connect = root.findViewById(R.id.connection);
        button_connect = root.findViewById(R.id.btnconnection);
        recyclerView = root.findViewById(R.id.rec);
        liner_empty_date = root.findViewById(R.id.liner_empty_date);
        swipeRefreshLayout = root.findViewById(R.id.swipe);
        shimmerFrameLayout = root.findViewById(R.id.shimmemr);

        localSession = new LocalSession(getActivity());
        ConnectivityManager connectivityManager = ((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (recyclerView.getVisibility() == View.INVISIBLE) {
                    img_bad_connection.setVisibility(View.INVISIBLE);
                    tv_connect.setVisibility(View.INVISIBLE);
                    button_connect.setVisibility(View.INVISIBLE);
                    shimmerFrameLayout.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.showShimmer(true);
                    swipeRefreshLayout.setRefreshing(false);
                    GetAllNotifications();
                }else if(recyclerView.getVisibility() == View.VISIBLE){
                    recyclerView.setVisibility(View.INVISIBLE);
                    shimmerFrameLayout.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.showShimmer(true);
                    swipeRefreshLayout.setRefreshing(false);
                    GetAllNotifications();
                }
            }
        });


        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected())
        {
            GetAllNotifications();
        }
        else
        {
            img_bad_connection.setVisibility(View.VISIBLE);
            tv_connect.setText(R.string.connect_internet);
            tv_connect.setVisibility(View.VISIBLE);
            button_connect.setVisibility(View.VISIBLE);
            shimmerFrameLayout.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            liner_empty_date.setVisibility(View.INVISIBLE);
            button_connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    img_bad_connection.setVisibility(View.INVISIBLE);
                    tv_connect.setVisibility(View.INVISIBLE);
                    button_connect.setVisibility(View.INVISIBLE);
                    shimmerFrameLayout.setVisibility(View.VISIBLE);
                    GetAllNotifications();
                }
            });
        }
        return root;
    }


    //<--  Git All Notification -->
    public void GetAllNotifications() {
        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<NotificationsResponse> call = requestInterface.GelAllNotifications("Bearer " + localSession.getToken());
        call.enqueue(new Callback<NotificationsResponse>() {
            @Override
            public void onResponse(Call<NotificationsResponse> call, Response<NotificationsResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        notifications_adapter = new Notifications_Adapter(response.body().getNotificatiosModels(), getActivity());
                        if (notifications_adapter.getItemCount() == 0)
                        {
                            liner_empty_date.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setRefreshing(false);
                            recyclerView.setVisibility(View.INVISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.hideShimmer();
                            shimmerFrameLayout.setVisibility(View.INVISIBLE);
                        }
                        else if (notifications_adapter.getItemCount() > 0 )
                        {
                            liner_empty_date.setVisibility(View.INVISIBLE);
                            notifications_adapter.notifyDataSetChanged();
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.hideShimmer();
                            shimmerFrameLayout.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(notifications_adapter);
                        }

                    }
                    else
                     {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.hideShimmer();
                        shimmerFrameLayout.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), getActivity());
                    }
                }
                else
                {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.hideShimmer();
                    shimmerFrameLayout.setVisibility(View.INVISIBLE);
                    Log.i(TAG_server, response.errorBody().toString());
                    Utility.showAlertDialog(getString(R.string.error), getString(R.string.servererror), getActivity());
                }
            }

            @Override
            public void onFailure(Call<NotificationsResponse> call, Throwable t) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.hideShimmer();
                shimmerFrameLayout.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                Utility.printLog(Tag_failure, t.getMessage());
                img_bad_connection.setVisibility(View.VISIBLE);
                tv_connect.setText(R.string.connect_internet_slow);
                tv_connect.setVisibility(View.VISIBLE);
                button_connect.setVisibility(View.VISIBLE);
                liner_empty_date.setVisibility(View.INVISIBLE);
                button_connect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        img_bad_connection.setVisibility(View.INVISIBLE);
                        tv_connect.setVisibility(View.INVISIBLE);
                        button_connect.setVisibility(View.INVISIBLE);
                        shimmerFrameLayout.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.showShimmer(true);
                        GetAllNotifications();
                    }
                });
            }
        });
    }
}
