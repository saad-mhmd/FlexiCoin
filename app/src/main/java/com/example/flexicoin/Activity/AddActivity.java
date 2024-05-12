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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView wishTStop;
    Button pushCash;
    EditText cardNo, cardName, cvv, amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        wishTStop = findViewById(R.id.stopNow);
        pushCash = findViewById(R.id.pushCrypto);
        cardNo = findViewById(R.id.recIEd);
        cardName = findViewById(R.id.cryTypeId);
        cvv = findViewById(R.id.recAccId);
        amount = findViewById(R.id.editTextAmount);

        wishTStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddActivity.this, MainActivity.class));
            }
        });

        pushCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });
    }

    private void addData() {
        String cardNof = cardNo.getText().toString();
        String cardNamef = cardName.getText().toString();
        String cvvf = cvv.getText().toString();
        double cashAmount = Double.parseDouble(amount.getText().toString());

        // Get the current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = sdf.format(new Date());

        // Get the currently logged-in user
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            String userID = user.getUid();

            // Create a Map to represent the data
            Map<String, Object> data = new HashMap<>();
            data.put("cardNo", cardNof);
            data.put("cardName", cardNamef);
            data.put("cvv", cvvf);
            data.put("cashAmount", cashAmount);
            data.put("date", currentDate);  // Add the 'date' attribute

            // Create a reference to the user's collection and add the data
            DocumentReference userCollection = db.collection("UsersInfo").document(userID);
            userCollection.collection("AddedCash")
                    .add(data)
                    .addOnSuccessListener(documentReference -> {
                        // Increment TotalAssets with the added cashAmount
                        db.collection("TotalAssets")
                                .document(userID)
                                .update("cashAmount", FieldValue.increment(cashAmount))
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(AddActivity.this, "Cash added successfully!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(AddActivity.this, MainActivity.class));
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(AddActivity.this, "Failed to update TotalAssets: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddActivity.this, "Failed to add cash: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
