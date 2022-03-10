package com.example.cafeteria.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.cafeteria.Local_DB.LocalSession;
import com.example.cafeteria.Model.LogoutResponse;
import com.example.cafeteria.Network.ApiClient;
import com.example.cafeteria.Network.RequestInterface;
import com.example.cafeteria.R;
import com.example.cafeteria.Utilty.Utility;

import static java.security.AccessController.getContext;

public class Logout extends AppCompatActivity {
LocalSession localSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        localSession = new LocalSession(this);
         log();
    }

    public void log(){
        if(!LocalSession.getToken().isEmpty()) {
        ProgressDialog loading = ProgressDialog.show(Logout.this,null,getString(R.string.wait), false, false);
        loading.setContentView(R.layout.custom_progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);

            final RequestInterface requestInterface = ApiClient.getClient(ApiClient.LOGOUT_URL).create(RequestInterface.class);
            Call<LogoutResponse> call= requestInterface.LogoutApi("Bearer " + localSession.getToken());
            call.enqueue(new Callback<LogoutResponse>() {
                @Override
                public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response)
                {
                    if(response.isSuccessful())
                    {
                        if(!response.body().isError())
                        {
                            loading.dismiss();
                            LocalSession.clearSession();
                            Intent intent1 = new Intent(Logout.this, Login.class);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent1);
                            finish();
                        }
                        else
                        {
                            loading.dismiss();
                            Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(),Logout.this);
                        }
                    }
                    else
                    {
                        loading.dismiss();
                        Utility.showAlertDialog(getString(R.string.error),getString(R.string.servererror),Logout.this);

                    }
                }

                @Override
                public void onFailure(Call<LogoutResponse> call, Throwable t) {
                    loading.dismiss();
                    Utility.showAlertDialog(getString(R.string.error),getString(R.string.connect_internet_slow),Logout.this);
                }
            });

        }else{

            //
        }
    }
}
