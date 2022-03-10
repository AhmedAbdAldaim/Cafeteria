package com.example.cafeteria.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cafeteria.R;

public class AboutApp extends AppCompatActivity {
    CardView cardview_image_back;
    ImageView img_facebook,img_whatsapp,img_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        img_facebook = findViewById(R.id.img_facebook);
        img_whatsapp = findViewById(R.id.img_whatspp);
        img_email = findViewById(R.id.img_email);
        cardview_image_back = findViewById(R.id.cardview_image_back);

        // <-- Back -- >
        cardview_image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // <-- Intent To FaceBook -- >
        img_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/100429419181547"));
                    startActivity(intent);
                }catch (ActivityNotFoundException e) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/تطبيق-مينيو-السودان-100429419181547/"));
                    startActivity(intent);
                }
            }
        });

        // <-- Intent To WhatSapp -- >
        img_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://Wa.me/249992088105"));
                    startActivity(intent);
                }catch (ActivityNotFoundException e) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.whatsapp.com/send?phone=+249992088105"));
                    startActivity(intent);
                }
            }
        });

        // <-- Intent To Email -- >
        img_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                     Intent intent = new Intent(Intent.ACTION_SEND);
                     intent.setPackage("com.google.android.gm")
                    .setType("message/rfc822")
                    .putExtra(Intent.EXTRA_EMAIL,new String[]{"menuappsd@gmail.com"});
                    startActivity(intent);
                }catch (ActivityNotFoundException e) {
                    Toast.makeText(AboutApp.this, "عفوا ، ليس لديك تطبيق Gmail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
