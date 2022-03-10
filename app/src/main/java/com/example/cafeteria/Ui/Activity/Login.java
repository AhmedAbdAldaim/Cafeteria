package com.example.cafeteria.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
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
import android.provider.Settings.Secure;

import com.example.cafeteria.Local_DB.LocalSession;
import com.example.cafeteria.Model.LoginResponse;
import com.example.cafeteria.Network.ApiClient;
import com.example.cafeteria.Network.RequestInterface;
import com.example.cafeteria.R;
import com.example.cafeteria.Ui.Fragment.ProfileFragment;
import com.example.cafeteria.Utilty.Utility;

public class Login extends AppCompatActivity {
    EditText ed_phone,ed_password;
    Button login_btn;
    String phone,password;
    ImageView imageView_visibilty,imageView_invisibilty;
    FrameLayout frameLayout;
    LocalSession localSession;
    private String androidid = "";


    private static final String Tag_failure = "failure";
    private static final String TAG_server  = "TAG_server";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ed_phone = findViewById(R.id.phone_number);
        ed_password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_btn);
        imageView_visibilty = findViewById(R.id.visibiltyoff);
        imageView_invisibilty = findViewById(R.id.visibilty);
        frameLayout = findViewById(R.id.framelayout);

        localSession = new LocalSession(Login.this);
        ConnectivityManager connectivityManager = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));


        //<---EditText When Text Change-->
        ed_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length()>0)
                {
                    ed_phone.setBackgroundResource(R.drawable.custom_edittext);
                }

            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        // <-- Show Visibilty or Invisibilty Icon When OnTextChange -->
        ed_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                frameLayout.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty())
                {
                    frameLayout.setVisibility(View.INVISIBLE);
                }
                else
                {
                    frameLayout.setVisibility(View.VISIBLE);
                }
                if(charSequence.toString().length()>0)
                {
                    ed_password.setBackgroundResource(R.drawable.custom_edittext);
                }
            }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    });

        // <-- Visibilty and Invisibilty Password -->
        imageView_visibilty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_visibilty.setVisibility(View.GONE);
                imageView_invisibilty.setVisibility(View.VISIBLE);
                ed_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
        });

        imageView_invisibilty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_invisibilty.setVisibility(View.GONE);
                imageView_visibilty.setVisibility(View.VISIBLE);
                ed_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        });

        //<---EditText Hidden EditText Cursor When OnClick Done On Keyboard-->
        ed_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == ed_password.getId()) {
                    ed_password.setCursorVisible(true);
                }
            }
        });
        ed_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                ed_password.setCursorVisible(false);
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(ed_password.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });


        //  <-- Onclick Login Button-->
        login_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"MissingPermission", "HardwareIds"})
            @Override
            public void onClick(View view) {
             //  androidid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
              androidid = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);

//                TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//                telphony = telephonyManager.getDeviceId();

                phone = ed_phone.getText().toString().trim();
                password = ed_password.getText().toString().trim();

                if (Checked(phone, password))
                {
                    if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected())
                    {
                        //<--Hidden Keyboard
                        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (inputMethodManager.isAcceptingText())
                        {
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }

//                        if(!androidid.equals("")){
                            Login(androidid,"0"+phone, password);
//                            Toast.makeText(Login.this, androidid+"", Toast.LENGTH_SHORT).show();
//                        }else {
//                            Login(telphony,phone,password);
//                            Toast.makeText(Login.this, telephonyManager.getDeviceId()+"", Toast.LENGTH_SHORT).show();
//                        }


                    }
                    else
                    {
                        Utility.showAlertDialog(getString(R.string.error), getString(R.string.connect_internet), Login.this);
                    }
                }
            }
        });

    }

    // <-- Check Fields Function -->
    public Boolean Checked(String phone,String password){
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
      else if(!phone.matches("[0-9]{9}")){
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
        if (password.isEmpty())
        {
            ed_password.setBackgroundResource(R.drawable.custom_edittext_border_error);
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.custom_error_toast,(ViewGroup)findViewById(R.id.toasst));
            TextView textView = view.findViewById(R.id.error_toastt);
            textView.setText(getString(R.string.password_empty));
            Toast toast =new Toast(getApplicationContext());
            toast.setGravity(Gravity.TOP,0,30);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
            toast.show();
            return false;
        }
        else if(password.length() <8)
        {
            ed_password.setBackgroundResource(R.drawable.custom_edittext_border_error);
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.custom_error_toast,(ViewGroup)findViewById(R.id.toasst));
            TextView textView = view.findViewById(R.id.error_toastt);
            textView.setText(getString(R.string.password_check));
            Toast toast =new Toast(getApplicationContext());
            toast.setGravity(Gravity.TOP,0,30);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
            toast.show();
            return false;
        }

        return true;
    }



    // <-- Send Data TO request And Git Response Status
    public void Login(String android_id,String phone,String password){
        ProgressDialog loading = ProgressDialog.show(this,null,getString(R.string.wait), false, false);
        loading.setContentView(R.layout.custom_progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);

        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<LoginResponse> call= requestInterface.Login(android_id,phone,password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response)
            {
                if(response.isSuccessful())
                {
                    if(!response.body().isError())
                    {
                        loading.dismiss();
                        localSession.createSession(
                                response.body().getToken(),
                                response.body().getLoginModel().getId(),
                                response.body().getLoginModel().getName(),
                                response.body().getLoginModel().getPhone(),
                                response.body().getLoginModel().getCafeteria(),
                                response.body().getLoginModel().getLocation(),
                                response.body().getLoginModel().getStatus(),
                                response.body().getLoginModel().getMac_address(),
                                response.body().getLoginModel().getExpir_date(),
                                ed_password.getText().toString().trim());

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        loading.dismiss();
                        Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), Login.this);
                    }
                }
                else
                {
                    loading.dismiss();
                    Utility.showAlertDialog(getString(R.string.error),getString(R.string.servererror),Login.this);
                    Log.i(TAG_server, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loading.dismiss();
                Utility.showAlertDialog(getString(R.string.error),getString(R.string.connect_internet_slow),Login.this);
                Utility.printLog(Tag_failure, t.getMessage());
            }
        });
    }
}


