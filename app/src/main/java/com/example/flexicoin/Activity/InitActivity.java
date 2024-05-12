package com.example.flexicoin.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.flexicoin.R;

public class InitActivity extends AppCompatActivity {
    private ImageView goBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        ImageView ltgobtn = findViewById(R.id.goBtnId);
        ltgobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InitActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
//        initView();
//        setVariable();
    }

//    private void initView() {
//        goBtn = findViewById(R.id.goBtnId);
//    }
//
//    private void setVariable() {
//        goBtn.setOnClickListener(v -> startActivity(new Intent(InitActivity.this, LoginActivity.class)));
//    }
}
