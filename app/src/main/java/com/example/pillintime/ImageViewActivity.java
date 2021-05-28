package com.example.pillintime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ImageViewActivity extends AppCompatActivity {

    ImageView mImageView;
    String imgStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        mImageView = findViewById(R.id.image_view_full_screen);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            imgStr = extras.getString("imgStr");
        }

        if(imgStr != null && imgStr.trim().length() != 0){
            Picasso.get()
                    .load(imgStr)
                    .into(mImageView);
        }

    }
}