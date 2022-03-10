package com.example.cafeteria.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cafeteria.Local_DB.LocalSession;
import com.example.cafeteria.Model.EditProfileResponse;
import com.example.cafeteria.Network.ApiClient;
import com.example.cafeteria.Network.RequestInterface;
import com.example.cafeteria.R;
import com.example.cafeteria.Utilty.Utility;

public class EditProfile extends AppCompatActivity {

    EditText ed_name,ed_phone,ed_cafeteria,ed_location;
    Button btn_editprofile,btn_editprofile_before;
    String phone,cafeteria,location;
    TextView tv_editpassword;
    CardView cardview_image_back;
    FrameLayout frameLayout;
    LocalSession localSession;

    private static final String Tag_failure = "failure";
    private static final String TAG_server  = "TAG_server";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        cardview_image_back = findViewById(R.id.cardview_image_back);
        ed_name = findViewById(R.id.name);
        ed_cafeteria = findViewById(R.id.cafeteria);
        ed_phone = findViewById(R.id.phone_number);
        ed_location = findViewById(R.id.location);
        tv_editpassword = findViewById(R.id.editpassword);
        btn_editprofile = findViewById(R.id.edit_btn);
        btn_editprofile_before = findViewById(R.id.edit_btn2);
        frameLayout = findViewById(R.id.framelayout);


        localSession = new LocalSession(EditProfile.this);
        ConnectivityManager connectivityManager = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));


        // <-- Back -- >
        cardview_image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // <-- Intent To Edit Password Page -- >
        tv_editpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProfile.this,EditPassword.class));
            }
        });


        // <-- Get Local Date TO EditText -->
        ed_name.setText(localSession.getName());
        ed_cafeteria.setText(localSession.getCafeteria());
        ed_phone.setText(localSession.getPhone());
        ed_location.setText(localSession.getLocation());


        // <-- When Onclick EditText Name -->
        ed_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = getLayoutInflater();
                View view1 = layoutInflater.inflate(R.layout.custom_error_toast,(ViewGroup)findViewById(R.id.toasst));
                TextView textView = view1.findViewById(R.id.error_toastt);
                textView.setText(getString(R.string.Canot_edit_name_hint_editprofile));
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.TOP,0,30);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(view1);
                toast.show();
            }
        });


        //<---EditText When Text Change-->
        ed_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()>0)
                {
                    ed_phone.setBackgroundResource(R.drawable.custom_edittext);
                }
                if(!charSequence.toString().trim().equals(localSession.getPhone()))
                {
                    btn_editprofile_before.setVisibility(View.INVISIBLE);
                    btn_editprofile.setVisibility(View.VISIBLE);
                }
                else
                {
                    btn_editprofile_before.setVisibility(View.VISIBLE);
                    btn_editprofile.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        //<---EditText When Text Change-->
        ed_cafeteria.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()>0)
                {
                    ed_cafeteria.setBackgroundResource(R.drawable.custom_edittext);
                }
                if(!charSequence.toString().trim().equals(localSession.getCafeteria()))
                {
                    btn_editprofile_before.setVisibility(View.INVISIBLE);
                    btn_editprofile.setVisibility(View.VISIBLE);
                }
                else
                {
                    btn_editprofile_before.setVisibility(View.VISIBLE);
                    btn_editprofile.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        //<---EditText When Text Change-->
        ed_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()>0)
                {
                    ed_location.setBackgroundResource(R.drawable.custom_edittext);
                }
                if(!charSequence.toString().trim().equals(localSession.getLocation()))
                {
                    btn_editprofile_before.setVisibility(View.INVISIBLE);
                    btn_editprofile.setVisibility(View.VISIBLE);
                }
                else
                {
                    btn_editprofile_before.setVisibility(View.VISIBLE);
                    btn_editprofile.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //<---EditText Hidden EditText Cursor When OnClick Done On Keyboard-->
        ed_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == ed_location.getId())
                {
                    ed_location.setCursorVisible(true);
                }
            }
        });
        ed_location.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                ed_location.setCursorVisible(false);
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(ed_location.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });


        //  <-- Onclick Login Button-->
        btn_editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = ed_phone.getText().toString().trim();
                cafeteria = ed_cafeteria.getText().toString().trim();
                location = ed_location.getText().toString().trim();

                if (Checked(phone, cafeteria,location))
                {
                    ed_location.setBackgroundResource(R.drawable.custom_edittext_additems);

                    if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected())
                    {
                        //<--Hidden Keyboard
                        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (inputMethodManager.isAcceptingText())
                        {
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        EditProfile(phone, cafeteria,location);
                    }
                    else
                    {
                        Utility.showAlertDialog(getString(R.string.error), getString(R.string.connect_internet), EditProfile.this);
                    }
                }
            }
        });
    }


    // <-- Check Fields Function -->
    public Boolean Checked(String phone,String cafeteria,String location){
        if(phone.isEmpty()){
            ed_phone.setBackgroundResource(R.drawable.custom_edittext_border_error);
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.custom_error_toast,(ViewGroup)findViewById(R.id.toasst));
            TextView textView = view.findViewById(R.id.error_toastt);
            textView.setText(getString(R.string.phone_number_empty));
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.TOP,0,30);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
            toast.show();
            return false;
        }
        else if(!phone.matches("[0-9]{10}")){
            ed_phone.setBackgroundResource(R.drawable.custom_edittext_border_error);
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.custom_error_toast,(ViewGroup)findViewById(R.id.toasst));
            TextView textView = view.findViewById(R.id.error_toastt);
            textView.setText(getString(R.string.phone_valid));
            Toast toast =new Toast(getApplicationContext());
            toast.setGravity(Gravity.TOP,0,30);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
            toast.show();
            return false;
        }
        if(cafeteria.isEmpty()){
            ed_cafeteria.setBackgroundResource(R.drawable.custom_edittext_border_error);
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.custom_error_toast,(ViewGroup)findViewById(R.id.toasst));
            TextView textView = view.findViewById(R.id.error_toastt);
            textView.setText(getString(R.string.cafeteria_hint_editprofile_empty));
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.TOP,0,30);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
            toast.show();
            return false;
        }
        if(location.isEmpty()){
            ed_location.setBackgroundResource(R.drawable.custom_edittext_border_error);
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.custom_error_toast,(ViewGroup)findViewById(R.id.toasst));
            TextView textView = view.findViewById(R.id.error_toastt);
            textView.setText(getString(R.string.location_hint_editprofile_empty));
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
    public void EditProfile(String phone,String cafeteria,String location){
        ProgressDialog loading = ProgressDialog.show(this,null,getString(R.string.wait), false, false);
        loading.setContentView(R.layout.custom_progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);


        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<EditProfileResponse> call= requestInterface.EditProfile(phone,cafeteria,location,localSession.getPassword(),"Bearer " + localSession.getToken());
        call.enqueue(new Callback<EditProfileResponse>() {
            @Override
            public void onResponse(Call<EditProfileResponse> call, Response<EditProfileResponse> response)
            {
                if(response.isSuccessful())
                {
                    if(!response.body().isError())
                    {
                        loading.dismiss();
                        localSession.createSession(
                                LocalSession.getToken(),
                                response.body().getEditProfileModel().getId(),
                                response.body().getEditProfileModel().getName(),
                                phone,
                                cafeteria,
                                location,
                                response.body().getEditProfileModel().getStatus(),
                                response.body().getEditProfileModel().getMac_address(),
                                response.body().getEditProfileModel().getExpir_date(),
                                LocalSession.getPassword());

                        Toast.makeText(EditProfile.this, getString(R.string.successfulyy_edited)+"", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditProfile.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        loading.dismiss();
                        Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), EditProfile.this);
                    }
                }
                else
                {
                    loading.dismiss();
                    Utility.showAlertDialog(getString(R.string.error),getString(R.string.servererror),EditProfile.this);
                    Log.i(TAG_server, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                loading.dismiss();
                Utility.showAlertDialog(getString(R.string.error),getString(R.string.connect_internet_slow),EditProfile.this);
                Utility.printLog(Tag_failure, t.getMessage());
            }
        });
    }
}


