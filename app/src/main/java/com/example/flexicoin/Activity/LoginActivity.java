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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
private EditText userEdt, passEdt;
private Button loginBtn;
private TextView registerBtn;
FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        registerBtn = findViewById(R.id.loginId);



        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        initView();
        setVariable();

    }
    private void initView(){
        userEdt = findViewById(R.id.recIEd);
        passEdt = findViewById(R.id.recAccId);
        loginBtn = findViewById(R.id.pushCrypto);


    }

    private void setVariable(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userEdt.getText().toString().isEmpty() || passEdt.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this,"Please fill the login form", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(userEdt.getText().toString(), passEdt.getText().toString())
                        .addOnCompleteListener( (OnCompleteListener<AuthResult>) task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(LoginActivity.this, "Login Successful.",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                        });
            }
        });
    }
}