package com.example.flexicoin.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.flexicoin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    private EditText firstNameEditText, lastNameEditText, phoneNumberEditText;
    private Button saveButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        firstNameEditText = view.findViewById(R.id.editTextFirstName);
        lastNameEditText = view.findViewById(R.id.editTextLastName);
        phoneNumberEditText = view.findViewById(R.id.editTextPhoneNumber);
        saveButton = view.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });

        // Load user profile data when the fragment is created
        loadUserProfileData();

        return view;
    }

    private void loadUserProfileData() {
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
                            String lastName = document.getString("lastName");
                            String phoneNumber = document.getString("phoneNumber");

                            // Populate the EditText fields with retrieved data
                            firstNameEditText.setText(firstName);
                            lastNameEditText.setText(lastName);
                            phoneNumberEditText.setText(phoneNumber);
                        }
                    }
                }
            });
        }
    }

    private void saveUserProfile() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();

        if (currentUser != null) {
            String userID = currentUser.getUid();
            DocumentReference userDocument = db.collection("UsersProfiles").document(userID);

            Map<String, Object> userProfile = new HashMap<>();
            userProfile.put("firstName", firstName);
            userProfile.put("lastName", lastName);
            userProfile.put("phoneNumber", phoneNumber);

            userDocument.set(userProfile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Profile saved successfully", Toast.LENGTH_SHORT).show();
                                // You can finish the activity or redirect as needed
                            } else {
                                Toast.makeText(getContext(), "Failed to save profile", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
