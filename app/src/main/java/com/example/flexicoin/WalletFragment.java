package com.example.flexicoin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WalletFragment extends Fragment {

    private TextView btcAmountTextView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);

        btcAmountTextView = view.findViewById(R.id.btcAmountTextView);

        // Get the currently logged-in user
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            String userID = user.getUid();

            // Create a reference to the "TotalAssets" collection for the current user
            db.collection("TotalAssets")
                    .document(userID)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Retrieve the btcAmount from the document
                            double btcAmount = documentSnapshot.getDouble("btcAmount");
                            // Update the TextView with the retrieved btcAmount
                            btcAmountTextView.setText(String.valueOf(btcAmount));
                        } else {
                            // Handle the case where the document doesn't exist
                            btcAmountTextView.setText("N/A");
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle any errors that may occur during the retrieval
                        btcAmountTextView.setText("Error: " + e.getMessage());
                    });
        }

        return view;
    }
}
