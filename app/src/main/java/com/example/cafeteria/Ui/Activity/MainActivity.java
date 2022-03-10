package com.example.cafeteria.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.cafeteria.Local_DB.NotificationLocalSession;
import com.example.cafeteria.Ui.Fragment.NotificationFragment;
import com.example.cafeteria.Ui.Fragment.ProfileFragment;
import com.example.cafeteria.R;
import com.example.cafeteria.Ui.Fragment.HomeFragment;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import static com.example.cafeteria.Ui.Notification.Service.newid;


public class MainActivity extends AppCompatActivity {

    public static ChipNavigationBar chipNavigationBar;
    NotificationLocalSession notificationLocalSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chipNavigationBar = findViewById(R.id.navigtion);
        notificationLocalSession = new NotificationLocalSession(this);


        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new HomeFragment()).commit();
        chipNavigationBar.setItemSelected(R.id.home,true);

        naviagation_bar();

        Intent intent = getIntent();
        boolean notification_st = intent.getBooleanExtra("notification_status",false);

        if(notification_st==true)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new NotificationFragment()).commit();
            chipNavigationBar.setItemSelected(R.id.notification,true);
            notificationLocalSession.createSession(newid);
            chipNavigationBar.dismissBadge(R.id.notification);
        }
    }


    public void naviagation_bar(){
        chipNavigationBar.showBadge(R.id.home);

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.home:
                        fragment = new HomeFragment();
                        chipNavigationBar.dismissBadge(R.id.notification);
                        break;
                    case R.id.profile:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.notification:
                        fragment = new NotificationFragment();
                        notificationLocalSession.createSession(newid);
                        chipNavigationBar.dismissBadge(R.id.notification);
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).commit();

            }
        });
    }
}
