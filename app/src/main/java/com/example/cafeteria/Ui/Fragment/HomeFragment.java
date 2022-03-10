package com.example.cafeteria.Ui.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cafeteria.Model.CategoriesModel;
import com.example.cafeteria.Model.ListviewProfileModel;
import com.example.cafeteria.Ui.Activity.AboutApp;
import com.example.cafeteria.Ui.Activity.AddItem;
import com.example.cafeteria.Local_DB.LocalSession;
import com.example.cafeteria.Model.CategoriesResponse;
import com.example.cafeteria.Network.ApiClient;
import com.example.cafeteria.Network.RequestInterface;
import com.example.cafeteria.R;
import com.example.cafeteria.Ui.Adapter.Categories_Adapter;
import com.example.cafeteria.Ui.Adapter.ListviewProfileAdapter;
import com.example.cafeteria.Ui.Notification.BroadCast;
import com.example.cafeteria.Ui.Notification.Service;
import com.example.cafeteria.Utilty.Utility;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

   public static FloatingActionButton floatingActionButton;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    Categories_Adapter categories_adapter;
    LinearLayout liner_empty_date;
    TextView tv_connect;
    ImageView img_bad_connection;
    public static TextView tv_categories;
    EditText ed_Search;
    Button button_connect,btn_call;
    ShimmerFrameLayout shimmerFrameLayout;
    LocalSession localSession;

    private static final String TAG_server = "Server";
    private static final String Tag_failure = "failure";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.home_fragment, container, false);

        // <-- Notification -->
        if(Service.service_run==false){
            Service.service_run = true;
            Intent intent = new Intent(getActivity(),Service.class);
            getActivity().startService(intent);
        }

        img_bad_connection = root.findViewById(R.id.img_bad_connection);
        tv_connect = root.findViewById(R.id.connection);
        button_connect = root.findViewById(R.id.btnconnection);
        liner_empty_date = root.findViewById(R.id.liner_empty_date);
        recyclerView = root.findViewById(R.id.rec);
        ed_Search = root.findViewById(R.id.search);
        btn_call = root.findViewById(R.id.call);
        floatingActionButton = root.findViewById(R.id.floatingActionButton);
        swipeRefreshLayout = root.findViewById(R.id.swipe);
        shimmerFrameLayout = root.findViewById(R.id.shimmemr);
        tv_categories = root.findViewById(R.id.categories);
        localSession = new LocalSession(getActivity());
        ConnectivityManager connectivityManager = ((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE));


        //  <-- SEARCH -->
        ed_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    categories_adapter.getFilter().filter(charSequence);
                }catch (Exception e){ }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    categories_adapter.getFilter().filter(charSequence);
                }catch (Exception e){ }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        //      <---EditText Hidden EditText Cursor When OnClick Done On Keyboard-->
        ed_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==ed_Search.getId()){
                    ed_Search.setCursorVisible(true);
                }
            }
        });
        ed_Search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                ed_Search.setCursorVisible(false);
                if(event !=null &&(event.getKeyCode()==KeyEvent.KEYCODE_ENTER)){
                    InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(ed_Search.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });


        // <-- Onclick floatingActionButton -->
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddItem.class));
            }
        });


        // <-- Refresh -- >
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                if (recyclerView.getVisibility() == View.INVISIBLE)
                {
                    liner_empty_date.setVisibility(View.INVISIBLE);
                    img_bad_connection.setVisibility(View.INVISIBLE);
                    tv_connect.setVisibility(View.INVISIBLE);
                    button_connect.setVisibility(View.INVISIBLE);
                    shimmerFrameLayout.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.showShimmer(true);
                    swipeRefreshLayout.setRefreshing(false);
                    GetAllCategories();
                }
                else if(recyclerView.getVisibility() == View.VISIBLE)
                {
                    liner_empty_date.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    shimmerFrameLayout.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.showShimmer(true);
                    swipeRefreshLayout.setRefreshing(false);
                    GetAllCategories();
                }
            }
        });


        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected())
        {
            GetAllCategories();
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
                    GetAllCategories();
                }
            });
        }
        return root;
    }


    //<--  Git All Categories -->
    public void GetAllCategories() {

        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<CategoriesResponse> call = requestInterface.GetCategories("Bearer " + localSession.getToken());
        call.enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                if (response.isSuccessful())
                {
                    if (!response.body().isError())
                    {
                        categories_adapter = new Categories_Adapter(response.body().getCategoriesModelList(), getActivity());
                        if (categories_adapter.getItemCount() == 0)
                        {
                            floatingActionButton.setVisibility(View.VISIBLE);
                            liner_empty_date.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setRefreshing(false);
                            recyclerView.setVisibility(View.INVISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.hideShimmer();
                            shimmerFrameLayout.setVisibility(View.INVISIBLE);
                        }
                        else if (categories_adapter.getItemCount() > 0 )
                        {

                               floatingActionButton.setVisibility(View.VISIBLE);
                               liner_empty_date.setVisibility(View.INVISIBLE);
                                categories_adapter.notifyDataSetChanged();
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.hideShimmer();
                                shimmerFrameLayout.setVisibility(View.INVISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerView.setAdapter(categories_adapter);
                        }
                    }
                    else
                    {
                        if(response.body().getStatus().equals("date"))
                        {
                            categories_adapter = new Categories_Adapter(response.body().getCategoriesModelList(), getActivity());
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.hideShimmer();
                            shimmerFrameLayout.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(categories_adapter);
                            ed_Search.setVisibility(View.GONE);
                            tv_categories.setTextColor(getContext().getColor(R.color.red));
                            tv_categories.setText(getString(R.string.expirt) + " " + LocalSession.getExpirDate() + " " + getString(R.string.expirt1));
                            btn_call.setVisibility(View.VISIBLE);
                            btn_call.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(getActivity(), AboutApp.class));
                                }
                            });
                        }else
                        {
                            Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), getActivity());
                        }

                    }
                } else {
                    Log.i(TAG_server, response.errorBody().toString());
                    Utility.showAlertDialog(getString(R.string.error), getString(R.string.servererror), getActivity());
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
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
                        GetAllCategories();
                    }
                });
            }
        });
    }
}
