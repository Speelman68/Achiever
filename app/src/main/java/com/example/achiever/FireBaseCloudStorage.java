package com.example.achiever;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;

public class FireBaseCloudStorage {
    private final FirebaseFirestore db;
    private static final String TAG = "DocSnippets";

    Map<String, Object> dataToSave;

    public FireBaseCloudStorage() {
        db = FirebaseFirestore.getInstance();
        dataToSave = new HashMap<>();
    }

    public void setup() {
        // [START set_firestore_settings]
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        // [END set_firestore_settings]
    }

    public void saveEmail(String email) {
        dataToSave.put("email", email);
        db.collection("users").document(email).set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Email has been saved");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Email has not been saved");
            }
        });
    }

    public void getEmail(String email) {
        DocumentReference emailRef = db.collection("users").document(email);
        emailRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

            }
        });

    }
}
