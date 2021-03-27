package com.example.pillintime.Notification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.pillintime.R;

public class NotificationMessageActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_message);
        textView = findViewById(R.id.activity_notification_text_message);
        Bundle bundle = getIntent().getExtras();
        textView.setText(bundle.getString("message"));
    }
}