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

import com.example.cafeteria.Local_DB.LocalSession;
import com.example.cafeteria.Model.EditProfileResponse;
import com.example.cafeteria.Model.LoginResponse;
import com.example.cafeteria.Network.ApiClient;
import com.example.cafeteria.Network.RequestInterface;
import com.example.cafeteria.R;
import com.example.cafeteria.Utilty.Utility;

public class EditPassword extends AppCompatActivity {

    EditText ed_new_password,ed_newpassword_confirm,ed_password;
    Button btn_editPassword;
    String password,newpassword,newpasswordconfirm;
    ImageView imageView_visible1,imageView_invisible1,imageView_visible2,imageView_invisible2,imageView_visible3,imageView_invisible3;
    CardView cardview_image_back;
    FrameLayout frameLayout1,frameLayout2,frameLayout3;
    LocalSession localSession;

    private static final String Tag_failure = "failure";
    private static final String TAG_server  = "TAG_server";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        cardview_image_back = findViewById(R.id.cardview_image_back);
        ed_password = findViewById(R.id.oldpassword);
        ed_new_password = findViewById(R.id.newpassword);
        ed_newpassword_confirm = findViewById(R.id.newpasswordconfirm);
        btn_editPassword = findViewById(R.id.edit_btn);
        imageView_visible1 = findViewById(R.id.visibiltyoff1);
        imageView_invisible1 = findViewById(R.id.visibilty1);
        imageView_visible2 = findViewById(R.id.visibiltyoff2);
        imageView_invisible2 = findViewById(R.id.visibilty2);
        imageView_visible3 = findViewById(R.id.visibiltyoff3);
        imageView_invisible3 = findViewById(R.id.visibilty3);
        frameLayout1 = findViewById(R.id.framelayout1);
        frameLayout2 = findViewById(R.id.framelayout2);
        frameLayout3 = findViewById(R.id.framelayout3);


        localSession = new LocalSession(EditPassword.this);
        ConnectivityManager connectivityManager = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));


        // <-- Back -- >
        cardview_image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // <-- Show Visibilty or Invisibilty Icon When OnTextChange 1 -->
        ed_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                frameLayout1.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()) {
                    frameLayout1.setVisibility(View.INVISIBLE);
                }
                else {
                    frameLayout1.setVisibility(View.VISIBLE);
                }
                if(charSequence.toString().length()>0){
                    ed_password.setBackgroundResource(R.drawable.custom_edittext);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        // <-- Visibilty and Invisibilty Password 1 -->
        imageView_visible1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_visible1.setVisibility(View.GONE);
                imageView_invisible1.setVisibility(View.VISIBLE);
                ed_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
        });

        imageView_invisible1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_invisible1.setVisibility(View.GONE);
                imageView_visible1.setVisibility(View.VISIBLE);
                ed_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        });


        // <-- Show Visibilty or Invisibilty Icon When OnTextChange 2 -->
        ed_new_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                frameLayout2.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()) {
                    frameLayout2.setVisibility(View.INVISIBLE);
                }
                else {
                    frameLayout2.setVisibility(View.VISIBLE);
                }
                if(charSequence.toString().length()>0){
                    ed_new_password.setBackgroundResource(R.drawable.custom_edittext);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        // <-- Visibilty and Invisibilty Password 2-->
        imageView_visible2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_visible2.setVisibility(View.GONE);
                imageView_invisible2.setVisibility(View.VISIBLE);
                ed_new_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
        });

        imageView_invisible2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_invisible2.setVisibility(View.GONE);
                imageView_visible2.setVisibility(View.VISIBLE);
                ed_new_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        });


        // <-- Show Visibilty or Invisibilty Icon When OnTextChange 3 -->
        ed_newpassword_confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                frameLayout3.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()) {
                    frameLayout3.setVisibility(View.INVISIBLE);
                }
                else {
                    frameLayout3.setVisibility(View.VISIBLE);
                }
                if(charSequence.toString().length()>0){
                    ed_newpassword_confirm.setBackgroundResource(R.drawable.custom_edittext);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        // <-- Visibilty and Invisibilty Password -->
        imageView_visible3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_visible3.setVisibility(View.GONE);
                imageView_invisible3.setVisibility(View.VISIBLE);
                ed_newpassword_confirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
        });

        imageView_invisible3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_invisible3.setVisibility(View.GONE);
                imageView_visible3.setVisibility(View.VISIBLE);
                ed_newpassword_confirm.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        });


        //<---EditText Hidden EditText Cursor When OnClick Done On Keyboard-->
        ed_newpassword_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == ed_newpassword_confirm.getId()) {
                    ed_newpassword_confirm.setCursorVisible(true);
                }
            }
        });
        ed_newpassword_confirm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                ed_newpassword_confirm.setCursorVisible(false);
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(ed_newpassword_confirm.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });


        //  <-- Onclick Login Button-->
        btn_editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password = ed_password.getText().toString().trim();
                newpassword = ed_new_password.getText().toString().trim();
                newpasswordconfirm = ed_newpassword_confirm.getText().toString().trim();

                if (Checked(password, newpassword ,newpasswordconfirm))
                {
                    if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected())
                    {
                        //<--Hidden Keyboard
                        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (inputMethodManager.isAcceptingText())
                        {
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        Login(password);
                    }
                    else
                    {
                        Utility.showAlertDialog(getString(R.string.error), getString(R.string.connect_internet), EditPassword.this);
                    }
                }
            }
        });

    }



    // <-- Check Fields Function -->
    public Boolean Checked(String password,String newPassword,String newpasswordconfirm){
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
        if (newPassword.isEmpty())
        {
            ed_new_password.setBackgroundResource(R.drawable.custom_edittext_border_error);
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.custom_error_toast,(ViewGroup)findViewById(R.id.toasst));
            TextView textView = view.findViewById(R.id.error_toastt);
            textView.setText(getString(R.string.new_password_editpassword_empty));
            Toast toast =new Toast(getApplicationContext());
            toast.setGravity(Gravity.TOP,0,30);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
            toast.show();
            return false;
        }
        else if(newPassword.length() <8)
        {
            ed_new_password.setBackgroundResource(R.drawable.custom_edittext_border_error);
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

        if (newpasswordconfirm.isEmpty())
        {
            ed_newpassword_confirm.setBackgroundResource(R.drawable.custom_edittext_border_error);
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.custom_error_toast,(ViewGroup)findViewById(R.id.toasst));
            TextView textView = view.findViewById(R.id.error_toastt);
            textView.setText(getString(R.string.new_password_confirm_editpassword));
            Toast toast =new Toast(getApplicationContext());
            toast.setGravity(Gravity.TOP,0,30);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
            toast.show();
            return false;
        }
        else if(!newPassword.isEmpty()&&!newpasswordconfirm.equals(newPassword))
        {
            ed_newpassword_confirm.setBackgroundResource(R.drawable.custom_edittext_border_error);
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.custom_error_toast,(ViewGroup)findViewById(R.id.toasst));
            TextView textView = view.findViewById(R.id.error_toastt);
            textView.setText(getString(R.string.password_similarity_edit_profile));
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
    public void Login(String password){
        ProgressDialog loading = ProgressDialog.show(this,null,getString(R.string.wait), false, false);
        loading.setContentView(R.layout.custom_progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);



        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<LoginResponse> call= requestInterface.Login(localSession.getMactaddress(),localSession.getPhone(),password);
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
                                response.body().getLoginModel(). getPhone(),
                                response.body().getLoginModel().getCafeteria(),
                                response.body().getLoginModel().getLocation(),
                                response.body().getLoginModel().getStatus(),
                                response.body().getLoginModel().getMac_address(),
                                response.body().getLoginModel().getExpir_date(),
                                localSession.getPassword());
                                EditPassword(newpassword);
                    }
                    else
                    {

                        if(response.body().getMessage_ar().contains(getString(R.string.expir_date_finsh)))
                        {
                            loading.dismiss();
                            Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), EditPassword.this);
                        }
                        else
                        {
                            loading.dismiss();
                            Utility.showAlertDialog(getString(R.string.error), getString(R.string.wrong_password), EditPassword.this);
                        }
                  }
                }
                else
                {
                    loading.dismiss();
                    Utility.showAlertDialog(getString(R.string.error),getString(R.string.servererror),EditPassword.this);
                    Log.i(TAG_server, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loading.dismiss();
                Utility.showAlertDialog(getString(R.string.error),getString(R.string.connect_internet_slow),EditPassword.this);
                Utility.printLog(Tag_failure, t.getMessage());
            }
        });
    }



    // <-- Send Data TO request And Git Response Status
    public void EditPassword(String password){
        ProgressDialog loading = ProgressDialog.show(this,null,getString(R.string.wait), false, false);
        loading.setContentView(R.layout.custom_progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);



        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<EditProfileResponse> call= requestInterface.EditProfilePassword(password,"Bearer " + localSession.getToken());
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
                                response.body().getEditProfileModel().getPhone(),
                                response.body().getEditProfileModel().getCafeteria(),
                                response.body().getEditProfileModel().getLocation(),
                                response.body().getEditProfileModel().getStatus(),
                                response.body().getEditProfileModel().getMac_address(),
                                response.body().getEditProfileModel().getExpir_date(),
                                password);

                        Toast.makeText(EditPassword.this, getString(R.string.successfulyy_edited)+"", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditPassword.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        loading.dismiss();
                        Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), EditPassword.this);
                    }
                }
                else
                {
                    loading.dismiss();
                    Utility.showAlertDialog(getString(R.string.error),getString(R.string.servererror),EditPassword.this);
                    Log.i(TAG_server, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                loading.dismiss();
                Utility.showAlertDialog(getString(R.string.error),getString(R.string.connect_internet_slow),EditPassword.this);
                Utility.printLog(Tag_failure, t.getMessage());
            }
        });
    }

}


