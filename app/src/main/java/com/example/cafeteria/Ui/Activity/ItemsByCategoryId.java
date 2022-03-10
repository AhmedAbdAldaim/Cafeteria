package com.example.cafeteria.Ui.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cafeteria.Local_DB.LocalSession;
import com.example.cafeteria.Model.AddItemResponse;
import com.example.cafeteria.Model.CategoriesModel;
import com.example.cafeteria.Model.EditItemResponse;
import com.example.cafeteria.Model.ItemByCategoryIdResponse;
import com.example.cafeteria.Network.ApiClient;
import com.example.cafeteria.Network.RequestInterface;
import com.example.cafeteria.R;
import com.example.cafeteria.Ui.Adapter.Categories_spinner_Adapter;
import com.example.cafeteria.Ui.Adapter.ItemByCategoryId_Adapter;
import com.example.cafeteria.Ui.Fragment.HomeFragment;
import com.example.cafeteria.Utilty.Utility;
import com.facebook.shimmer.ShimmerFrameLayout;

public class ItemsByCategoryId extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ItemByCategoryId_Adapter itemByCategoryIdAdapter;
    EditText ed_Search , ed_item_name, ed_item_price;;
    TextView tv_count_of_items,tv_item_name, tv_empty, tv_connect;
    CardView cardview_image_back;
    Button btn_connection;
    CardView cardView_add;
    View viewAdd;
    ShimmerFrameLayout shimmerFrameLayout;
    String Categoryid,item_name;
    LocalSession localSession;

    private static final String TAG_server = "Server";
    private static final String Tag_failure = "failure";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_by_category_id);

        cardview_image_back = findViewById(R.id.cardview_image_back);
        tv_connect = findViewById(R.id.connection);
        btn_connection = findViewById(R.id.btnconnection);
        recyclerView = findViewById(R.id.rec);
        tv_empty = findViewById(R.id.empty);
        ed_Search = findViewById(R.id.search);
        swipeRefreshLayout = findViewById(R.id.swipe);
        shimmerFrameLayout = findViewById(R.id.shimmemr);
        tv_count_of_items=  findViewById(R.id.count);
        tv_item_name = findViewById(R.id.item);
        cardView_add = findViewById(R.id.add_cardview);
        viewAdd = findViewById(R.id.viewadd);

        Intent intent = getIntent();
        Categoryid = intent.getStringExtra("Category_id");
        item_name = intent.getStringExtra("item_name");

        localSession = new LocalSession(ItemsByCategoryId.this);
        ConnectivityManager connectivityManager = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE));


        // <-- Back -- >
        cardview_image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //  <-- SEARCH -->
        ed_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    itemByCategoryIdAdapter.getFilter().filter(charSequence);
                }catch (Exception e){ }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    itemByCategoryIdAdapter.getFilter().filter(charSequence);
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
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(ed_Search.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

        // <-- Add Items -->
        cardView_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView img_cancle;
                Button btn_added;

                AlertDialog.Builder builder = new AlertDialog.Builder(ItemsByCategoryId.this);
                View view1 = getLayoutInflater().inflate(R.layout.custom_add_item, null);

                    ed_item_name = view1.findViewById(R.id.item_name);
                    ed_item_price = view1.findViewById(R.id.item_price);
                    btn_added = view1.findViewById(R.id.add_btn);
                    img_cancle = view1.findViewById(R.id.cancle);

                    //<---EditText When Text Change-->
                    ed_item_name.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }
                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (charSequence.toString().length() > 0) {
                                ed_item_name.setBackgroundResource(R.drawable.custom_edittext_additems);
                            }
                        }
                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });

                    //<---EditText When Text Change-->
                    ed_item_price.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }
                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (charSequence.toString().length() > 0) {
                                ed_item_price.setBackgroundResource(R.drawable.custom_edittext_additems);
                            }
                        }
                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });


                    //<---EditText Hidden EditText Cursor When OnClick Done On Keyboard-->
                    ed_item_price.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (view.getId() == ed_item_price.getId()) {
                                ed_item_price.setCursorVisible(true);
                            }
                        }
                    });
                    ed_item_price.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                            ed_item_price.setCursorVisible(false);
                            if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(ed_item_price.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                            return false;
                        }
                    });


                    builder.setView(view1);
                    final AlertDialog dialog = builder.create();
                    InsetDrawable insetDrawable = new InsetDrawable(new ColorDrawable(Color.TRANSPARENT), 20);
                    dialog.getWindow().setBackgroundDrawable(insetDrawable);


                    // <-- cancle dialog -->
                    img_cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    // <-- when click Add btn -->
                    btn_added.setOnClickListener(v ->
                    {
                        String item_name = ed_item_name.getText().toString().trim();
                        String item_price = ed_item_price.getText().toString().trim();

                        if (Checked(item_name, item_price))
                        {
                            if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {
                                dialog.dismiss();
                                //<--Hidden Keyboard
                                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                if (inputMethodManager.isAcceptingText())
                                {
                                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                }

                                AddItem(item_name, item_price, Categoryid);
                            }
                            else
                            {
                                Utility.showAlertDialog(getString(R.string.error), getString(R.string.connect_internet), ItemsByCategoryId.this);
                            }
                        }
                    });
                    dialog.show();
                }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (recyclerView.getVisibility() == View.INVISIBLE) {
                    tv_connect.setVisibility(View.INVISIBLE);
                    btn_connection.setVisibility(View.INVISIBLE);
                    shimmerFrameLayout.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.showShimmer(true);
                    swipeRefreshLayout.setRefreshing(false);
                    GetItemByCategoryId(Categoryid);
                }else if(recyclerView.getVisibility() == View.VISIBLE){
                    recyclerView.setVisibility(View.INVISIBLE);
                    shimmerFrameLayout.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.showShimmer(true);
                    swipeRefreshLayout.setRefreshing(false);
                    GetItemByCategoryId(Categoryid);
                }
            }
        });


        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected())
        {
            GetItemByCategoryId(Categoryid);
        }
        else
        {
            tv_connect.setText(R.string.connect_internet);
            tv_connect.setVisibility(View.VISIBLE);
            btn_connection.setVisibility(View.VISIBLE);
            btn_connection.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            tv_empty.setVisibility(View.INVISIBLE);
            btn_connection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_connect.setVisibility(View.INVISIBLE);
                    btn_connection.setVisibility(View.INVISIBLE);
                    shimmerFrameLayout.setVisibility(View.VISIBLE);
                    GetItemByCategoryId(Categoryid);
                }
            });
        }

    }


    //<--  Git All Item By Category_Id -->
    public void GetItemByCategoryId(String Categoryid_) {

        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<ItemByCategoryIdResponse> call = requestInterface.GetItemByCategoryId(Categoryid_,"Bearer " + localSession.getToken());
        call.enqueue(new Callback<ItemByCategoryIdResponse>() {
            @Override
            public void onResponse(Call<ItemByCategoryIdResponse> call, Response<ItemByCategoryIdResponse> response) {
                if (response.isSuccessful())
                {
                    if (!response.body().isError())
                    {
                        itemByCategoryIdAdapter = new ItemByCategoryId_Adapter(response.body().getItemByCategoryIdModel(), ItemsByCategoryId.this);
                        if (itemByCategoryIdAdapter.getItemCount() == 0)
                        {
                            tv_empty.setVisibility(View.VISIBLE);
                            cardView_add.setVisibility(View.VISIBLE);
                            viewAdd.setVisibility(View.VISIBLE);
                            tv_count_of_items.setVisibility(View.VISIBLE);
                            tv_item_name.setVisibility(View.VISIBLE);
                            tv_count_of_items.setText(itemByCategoryIdAdapter.getItemCount()+"");
                            tv_item_name.setText(item_name);
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setRefreshing(false);
                            recyclerView.setVisibility(View.INVISIBLE);
                            tv_empty.setText(R.string.empty_items);
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.hideShimmer();
                            shimmerFrameLayout.setVisibility(View.INVISIBLE); Intent intent = new Intent(ItemsByCategoryId.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            return;

                        } else if (itemByCategoryIdAdapter.getItemCount() > 0)
                        {
                            tv_empty.setVisibility(View.INVISIBLE);
                            cardView_add.setVisibility(View.VISIBLE);
                            viewAdd.setVisibility(View.VISIBLE);
                            tv_count_of_items.setVisibility(View.VISIBLE);
                            tv_item_name.setVisibility(View.VISIBLE);
                            itemByCategoryIdAdapter.notifyDataSetChanged();
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.hideShimmer();
                            shimmerFrameLayout.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(itemByCategoryIdAdapter);
                            tv_count_of_items.setText(itemByCategoryIdAdapter.getItemCount()+"");
                            tv_item_name.setText(item_name);

                        }
                    }
                    else
                        {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.hideShimmer();
                        shimmerFrameLayout.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), ItemsByCategoryId.this);
                       }
                }
                else {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.hideShimmer();
                    shimmerFrameLayout.setVisibility(View.INVISIBLE);
                    Log.i(TAG_server, response.errorBody().toString());
                    Utility.showAlertDialog(getString(R.string.error), getString(R.string.servererror), ItemsByCategoryId.this);
                }
            }

            @Override
            public void onFailure(Call<ItemByCategoryIdResponse> call, Throwable t) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.hideShimmer();
                shimmerFrameLayout.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                Utility.printLog(Tag_failure, t.getMessage());
                tv_connect.setText(R.string.connect_internet_slow);
                tv_connect.setVisibility(View.VISIBLE);
                btn_connection.setVisibility(View.VISIBLE);
                tv_empty.setVisibility(View.INVISIBLE);
                btn_connection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tv_connect.setVisibility(View.INVISIBLE);
                        btn_connection.setVisibility(View.INVISIBLE);
                        shimmerFrameLayout.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.showShimmer(true);
                        GetItemByCategoryId(Categoryid);
                    }
                });
            }
        });
    }



    // <-- Check Fields Function -->
    public Boolean Checked(String item_name, String price) {
        if (item_name.isEmpty())
        {
            ed_item_name.setBackgroundResource(R.drawable.custom_edittext_border_error);
            return false;
        }
        if (price.isEmpty())
        {
            ed_item_price.setBackgroundResource(R.drawable.custom_edittext_border_error);
            return false;
        }
        return true;
    }


    // <-- Edit Item
    public void AddItem(String item, String price, String categorie_id) {
        ProgressDialog loading = ProgressDialog.show(ItemsByCategoryId.this, null, getString(R.string.wait), false, false);
        loading.setContentView(R.layout.custom_progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);


        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<AddItemResponse> call = requestInterface.AddItem(item, price, categorie_id, "Bearer " + localSession.getToken());
        call.enqueue(new Callback<AddItemResponse>() {
            @Override
            public void onResponse(Call<AddItemResponse> call, Response<AddItemResponse> response) {
                if (response.isSuccessful())
                {
                    if (!response.body().isError())
                    {
                        GetItemByCategoryId(categorie_id);
                        loading.dismiss();
                        Toast.makeText(ItemsByCategoryId.this, getResources().getString(R.string.successfulyy_added), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        loading.dismiss();
                        Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), ItemsByCategoryId.this);
                    }
                } else
                {
                    loading.dismiss();
                    Utility.showAlertDialog(getString(R.string.error), getString(R.string.servererror), ItemsByCategoryId.this);
                }
            }

            @Override
            public void onFailure(Call<AddItemResponse> call, Throwable t) {
                loading.dismiss();
                Utility.showAlertDialog(getString(R.string.error), getString(R.string.connect_internet_slow), ItemsByCategoryId.this);

            }
        });
    }
}
