package com.example.flexicoin.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.flexicoin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BuyActivity extends AppCompatActivity {
    float priceVal;
    private final String apiKey = "7719d53173msh2cfdedd025ae276p1ae495jsn569763a8bcbf";
    TextView wishTStop, amtInUsdTv, convertBtn;
    EditText inAmt;
    Button buyNow;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        wishTStop = findViewById(R.id.stopNow);
        amtInUsdTv = findViewById(R.id.amtInUsdTvId);
        convertBtn = findViewById(R.id.convertBtnId);
        inAmt = findViewById(R.id.amountOfUsdId);
        buyNow = findViewById(R.id.buyNowId);
        buyNow.setEnabled(false);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://coinranking1.p.rapidapi.com/coin/Qwsogvtv82FCd?referenceCurrencyUuid=yhjMzLPhuIDl&timePeriod=24h";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("API Response", response.toString()); // Log the full response
                        try {
                            if (inAmt.getText().toString().isEmpty()) {
                                Toast.makeText(BuyActivity.this, "Your input is empty", Toast.LENGTH_SHORT).show();
                            } else {
                                buyNow.setEnabled(true);
                                JSONObject dataObject = response.getJSONObject("data");
                                JSONObject coinObject = dataObject.getJSONObject("coin");
                                String price = coinObject.getString("price");

                                // Set the price to the TextView
                                priceVal = Float.valueOf(price);
                                priceVal = Float.valueOf(String.valueOf(inAmt.getText())) / priceVal;
                                amtInUsdTv.setText(String.valueOf(priceVal));

                            }
                        } catch (Exception e) {
                            Log.e("Error Here", "Error parsing response", e);
                            Toast.makeText(BuyActivity.this, "Enter valid inputs only!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                if (error.networkResponse != null) {
                    Log.e("NetworkResponse", "Error: " + new String(error.networkResponse.data));
                    amtInUsdTv.setText("Error: " + new String(error.networkResponse.data));
                } else {
                    Log.e("VolleyError", "Error: ", error);
                    amtInUsdTv.setText("Response: error");
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("X-RapidAPI-Key", apiKey);
                headers.put("X-RapidAPI-Host", "coinranking1.p.rapidapi.com");
                return headers;
            }
        };

        // Set the button click listener for the "Convert" button
        convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add the request to the RequestQueue.
                queue.add(jsonObjectRequest);
            }
        });

        // Set the button click listener for the "Buy" button
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBuyButtonClick();
            }
        });

        wishTStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuyActivity.this, MainActivity.class));
            }
        });
    }

    private void handleBuyButtonClick() {
        // Get the entered cash amount from the EditText
        String cashAmountString = inAmt.getText().toString();
        if (cashAmountString.isEmpty()) {
            Toast.makeText(BuyActivity.this, "Please enter a cash amount", Toast.LENGTH_SHORT).show();
        } else {
            final float btcPurchaseAmount = priceVal; // Ensure it's final for the transaction
            final float cashAmountToDeduct = Float.parseFloat(cashAmountString);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                final String userID = user.getUid();
                final DocumentReference totalAssetsRef = db.collection("TotalAssets").document(userID);

                db.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot totalAssetsSnapshot = transaction.get(totalAssetsRef);
                        double cashAmount = totalAssetsSnapshot.getDouble("cashAmount");

                        if (cashAmount >= cashAmountToDeduct) {
                            // Sufficient funds, deduct the specified cash amount
                            double newCashAmount = cashAmount - cashAmountToDeduct;
                            double btcAmount = totalAssetsSnapshot.getDouble("btcAmount") + btcPurchaseAmount;

                            transaction.update(totalAssetsRef, "cashAmount", newCashAmount);
                            transaction.update(totalAssetsRef, "btcAmount", btcAmount);
                        } else {
                            // Not enough cash, handle this situation (e.g., show an error message)
                            throw new FirebaseFirestoreException("Insufficient funds", FirebaseFirestoreException.Code.ABORTED);
                        }
                        return null;
                    }
                }).addOnSuccessListener(aVoid -> {
                    Toast.makeText(BuyActivity.this, "BTC Purchase Request Made!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BuyActivity.this, MainActivity.class));
                }).addOnFailureListener(e -> {
                    Toast.makeText(BuyActivity.this, "Failed to update assets: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }
    }
}
