package com.example.flexicoin.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.flexicoin.R;

public class ReceiveActivity extends AppCompatActivity {
    TextView wishTStop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        wishTStop = findViewById(R.id.stopNow);
        wishTStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReceiveActivity.this , MainActivity.class));
            }
        });
    }
}