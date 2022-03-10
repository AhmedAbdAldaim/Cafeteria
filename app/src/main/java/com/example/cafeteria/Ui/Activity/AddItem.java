package com.example.cafeteria.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cafeteria.Ui.Adapter.Categories_spinner_Adapter;
import com.example.cafeteria.Local_DB.LocalSession;
import com.example.cafeteria.Model.AddItemResponse;
import com.example.cafeteria.Model.CategoriesModel;
import com.example.cafeteria.Model.CategoriesResponse;
import com.example.cafeteria.Network.ApiClient;
import com.example.cafeteria.Network.RequestInterface;
import com.example.cafeteria.R;
import com.example.cafeteria.Utilty.Utility;

public class AddItem extends AppCompatActivity {

    Categories_spinner_Adapter categories_spinner_adapter;
    EditText ed_item_name,ed_item_price;
    Button btn_added,btn_added_before;
    String item_name,item_price,categorie_id;
    ProgressBar progressBar;
    LocalSession localSession;
    Spinner spinner;

    private static final String TAG_server = "Server";
    private static final String Tag_failure = "failure";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        spinner = findViewById(R.id.categories_type);
        ed_item_name = findViewById(R.id.item_name);
        ed_item_price = findViewById(R.id.item_price);
        btn_added = findViewById(R.id.add_btn);
        btn_added_before = findViewById(R.id.add_btn2);
        progressBar = findViewById(R.id.prog);

        localSession = new LocalSession(AddItem.this);
        ConnectivityManager connectivityManager = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE));


        //<---EditText When Text Change-->
        ed_item_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length()>0)
                {
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
                if(charSequence.toString().length()>0)
                {
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


        // <--Spinner On Item Seleted -->
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CategoriesModel categoriesModel=(CategoriesModel)adapterView.getItemAtPosition(i);
                categorie_id = categoriesModel.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });



        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected())
        {
            GetAllCategories();
        }
        else
        {
            Utility.showAlertDialog(getString(R.string.error), getString(R.string.connect_internet), AddItem.this);
        }


        //  <-- Onclick Login Button-->
        btn_added.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item_name = ed_item_name.getText().toString().trim();
                item_price = ed_item_price.getText().toString().trim();

                if (Checked(item_name,item_price))
                {
                    if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected())
                    {
                        //<--Hidden Keyboard
                        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (inputMethodManager.isAcceptingText())
                        {
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        AddItem(item_name,item_price,categorie_id);
                    }
                    else
                    {
                        Utility.showAlertDialog(getString(R.string.error), getString(R.string.connect_internet), AddItem.this);
                    }
                }
            }
        });
    }


    // <-- Check Fields Function -->
    public Boolean Checked(String item_name,String price){
        if(item_name.isEmpty()){
            ed_item_name.setBackgroundResource(R.drawable.custom_edittext_border_error);
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.custom_error_toast,(ViewGroup)findViewById(R.id.toasst));
            TextView textView = view.findViewById(R.id.error_toastt);
            textView.setText(getString(R.string.item_name_empty));
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.TOP,0,30);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
            toast.show();
            return false;
        }

        if (price.isEmpty())
        {
            ed_item_price.setBackgroundResource(R.drawable.custom_edittext_border_error);
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.custom_error_toast,(ViewGroup)findViewById(R.id.toasst));
            TextView textView = view.findViewById(R.id.error_toastt);
            textView.setText(getString(R.string.item_price_empty));
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.TOP,0,30);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
            toast.show();
            return false;
        }

        return true;
    }



    // <-- Send Data TO request And Git Response Status
    public void AddItem(String item,String price,String categorie_id){
        ProgressDialog loading = ProgressDialog.show(this,null,getString(R.string.wait), false, false);
        loading.setContentView(R.layout.custom_progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);


        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<AddItemResponse> call= requestInterface.AddItem(item,price,categorie_id,"Bearer " + localSession.getToken());
        call.enqueue(new Callback<AddItemResponse>() {
            @Override
            public void onResponse(Call<AddItemResponse> call, Response<AddItemResponse> response)
            {
                if(response.isSuccessful())
                {
                    if(!response.body().isError())
                    {
                        ed_item_name.setText("");
                        ed_item_price.setText("");
                        Toast.makeText(AddItem.this, getString(R.string.successfulyy_added), Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                    else
                    {
                        loading.dismiss();
                        Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), AddItem.this);
                    }
                }
                else
                {
                    loading.dismiss();
                    Utility.showAlertDialog(getString(R.string.error),getString(R.string.servererror),AddItem.this);
                    Log.i(TAG_server, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<AddItemResponse> call, Throwable t) {
                loading.dismiss();
                Utility.showAlertDialog(getString(R.string.error),getString(R.string.connect_internet_slow),AddItem.this);
                Utility.printLog(Tag_failure, t.getMessage());
            }
        });
    }



    //<--  Git All Goldbars -->
    public void GetAllCategories() {

        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<CategoriesResponse> call = requestInterface.GetCategories_Spinner("Bearer " + LocalSession.getToken());
        call.enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                if (response.isSuccessful())
                {
                    if (!response.body().isError())
                    {
                        categories_spinner_adapter = new Categories_spinner_Adapter(response.body().getCategoriesModelList(), AddItem.this);
                        categories_spinner_adapter.notifyDataSetChanged();
                        spinner.setAdapter(categories_spinner_adapter);
                        progressBar.setVisibility(View.INVISIBLE);
                        btn_added_before.setVisibility(View.INVISIBLE);
                        btn_added.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), AddItem.this);
                    }
                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.i(TAG_server, response.errorBody().toString());
                    Utility.showAlertDialog(getString(R.string.error), getString(R.string.servererror), AddItem.this);
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t)
            {
                progressBar.setVisibility(View.INVISIBLE);
                Utility.showAlertDialog(getString(R.string.error),getString(R.string.connect_internet_slow), AddItem.this);
                Utility.printLog(Tag_failure, t.getMessage());
            }
        });
    }
}
