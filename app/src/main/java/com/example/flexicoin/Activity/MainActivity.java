package com.example.flexicoin.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flexicoin.Activity.BuyActivity;
import com.example.flexicoin.R;
import com.example.flexicoin.WalletFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LinearLayout add, send, recv, buy, profile, home,logout;

    private TextView textviewName;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<AddCash> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Firestore and Firebase Auth
        db = FirebaseFirestore.getInstance();  // Use the instance variable
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();  // Use the instance variable

        add = findViewById(R.id.addLOut);
        send = findViewById(R.id.sendLOut);
        recv = findViewById(R.id.recLOut);
        buy = findViewById(R.id.buyLOut);
        profile = findViewById(R.id.profileId);
        home = findViewById(R.id.homeLOut);
        logout = findViewById(R.id.logoutLoutId);
        textviewName = findViewById(R.id.textviewName);

        // Get a reference to the TextView for displaying cashAmount
        TextView cashAmountTextView = findViewById(R.id.displayCash);

        findViewById(R.id.walletId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current content with the WalletFragment
                WalletFragment walletFragment = new WalletFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.lay, walletFragment);
                transaction.addToBackStack(null); // Optional, allows back navigation
                transaction.commit();
            }
        });

        // Check if the user is authenticated
        if (currentUser != null) {
            // Obtain the user's ID
            String userID = currentUser.getUid();

            // Create a reference to the "TotalAssets" document for the user
            db.collection("TotalAssets").document(userID).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                // Get the cashAmount from the document
                                Double cashAmount = document.getDouble("cashAmount");

                                if (cashAmount != null) {
                                    // Update the TextView with the retrieved cashAmount
                                    cashAmountTextView.setText("$" + String.format("%.2f", cashAmount));
                                } else {
                                    // Handle the case where cashAmount is not available
                                    cashAmountTextView.setText("N/A");
                                }
                            }
                        }
                    });
        } else {
            // Handle the case where the user is not authenticated
            cashAmountTextView.setText("N/A");
        }

        // Load user first name
        loadUserFirstName();

        recyclerView = findViewById(R.id.recyclerLog);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataList = new ArrayList<>();
        adapter = new MyAdapter(dataList);
        recyclerView.setAdapter(adapter);

        // Load data from Firestore and populate the RecyclerView
        loadFirestoreData();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SendActivity.class));
            }
        });
        recv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ReceiveActivity.class));
            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BuyActivity.class));
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // You are already on the home screen, so no need to do anything here.
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ProfileFragment());
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FirebaseAuth.getInstance().signOut();

                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }catch (Exception e){
                    e.getMessage();
                    Toast.makeText(MainActivity.this,"Sign Out failed",Toast.LENGTH_SHORT);
                }

            }
        });
    }


    private void loadUserFirstName() {
        if (currentUser != null) {
            String userID = currentUser.getUid();
            DocumentReference userDocument = db.collection("UsersProfiles").document(userID);

            userDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String firstName = document.getString("firstName");

                            // Populate the textviewName with retrieved data
                            textviewName.setText(firstName);
                        }
                    }
                }
            });
        }
    }

    private void loadFirestoreData() {
        if (currentUser != null) {
            String userID = currentUser.getUid();

            // Create a reference to the user's subcollection "AddedCash" within their document
            db.collection("UsersInfo")
                    .document(userID)
                    .collection("AddedCash")
                    .orderBy("date") // Order by the 'date' field
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            dataList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String cardName = document.getString("cardName");
                                String cardNo = document.getString("cardNo");
                                String cvv = document.getString("cvv");
                                double cashAmount = document.getDouble("cashAmount");
                                String date = document.getString("date");

                                if (cardName != null && cardNo != null && cvv != null && date != null) {
                                    dataList.add(new AddCash(cardName, cardNo, cvv, cashAmount, date));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.lay, fragment);
        fragmentTransaction.commit();
    }
}
