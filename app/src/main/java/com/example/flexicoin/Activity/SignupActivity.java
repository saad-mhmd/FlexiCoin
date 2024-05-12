package com.example.flexicoin.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.flexicoin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private Button signupButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailEditText = findViewById(R.id.recIEd);
        passwordEditText = findViewById(R.id.recAccId);
        signupButton = findViewById(R.id.pushCrypto);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpUser();
            }
        });
    }

    private void signUpUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User registration was successful
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Obtain the user's ID
                                String userID = user.getUid();

                                // Create a reference to the "TotalAssets" collection
                                DocumentReference totalAssetsDocument = db.collection("TotalAssets").document(userID);

                                // Check if the document already exists
                                totalAssetsDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document != null && document.exists()) {
                                                // The "TotalAssets" document already exists; proceed with other tasks
                                                Toast.makeText(SignupActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                                // Redirect to the main activity or wherever needed
                                            } else {
                                                // The "TotalAssets" document doesn't exist; create it with initial values
                                                Map<String, Object> initialData = new HashMap<>();
                                                initialData.put("cashAmount", 0.0); // Set an initial value for cashAmount
                                                initialData.put("btcAmount", 0.0); // Set an initial value for btcAmount
                                                totalAssetsDocument.set(initialData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            // The "TotalAssets" document has been created; proceed with other tasks
                                                            Toast.makeText(SignupActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                                            // Redirect to the main activity or wherever needed
                                                        } else {
                                                            // Handle the case where document creation fails
                                                            Toast.makeText(SignupActivity.this, "Failed to create TotalAssets document", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        } else {
                                            // Handle the case where document retrieval fails
                                            Toast.makeText(SignupActivity.this, "Failed to retrieve TotalAssets document", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        } else {
                            // Handle user registration failure
                            Toast.makeText(SignupActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
