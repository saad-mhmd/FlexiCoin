package com.example.flexicoin.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flexicoin.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SendActivity extends AppCompatActivity {
    TextView wishTStop;
    Button pushCrpto;

    EditText recEmail,recAcc,crpType;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        wishTStop = findViewById(R.id.stopNow);
        pushCrpto = findViewById(R.id.pushCrypto);
        recEmail = findViewById(R.id.recIEd);
        recAcc = findViewById(R.id.recAccId);
        crpType = findViewById(R.id.cryTypeId);

        dbRef = FirebaseDatabase.getInstance().getReference().child("Send Crypto");

        wishTStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SendActivity.this , MainActivity.class));
            }
        });

        pushCrpto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushData();
            }
        });


    }
    private void pushData(){
        String recEmailf = recEmail.getText().toString();
        String recAccf = recAcc.getText().toString();
        String crypTypef = crpType.getText().toString();
        dbRef.push().setValue(new SendCrypto(recEmailf,recAccf,crypTypef));
        Toast.makeText(SendActivity.this,"Transaction successful",Toast.LENGTH_SHORT).show();
        startActivity( new Intent(SendActivity.this,MainActivity.class));
    }

}