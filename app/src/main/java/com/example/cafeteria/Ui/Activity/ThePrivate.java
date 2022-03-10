package com.example.cafeteria.Ui.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.cafeteria.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ThePrivate extends AppCompatActivity {
    CardView cardview_image_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_private);
        cardview_image_back = findViewById(R.id.cardview_image_back);

        // <-- Back -- >
        cardview_image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
