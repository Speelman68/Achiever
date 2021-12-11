package com.example.achiever.Firebase;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.achiever.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class FireBaseCloud {
    private FirebaseFirestore db;
    private static final String TAG = "DocSnippets";

    private User currentUser = new User();

    String email;
    Map<String, Object> user;


    public FireBaseCloud() {
        db = FirebaseFirestore.getInstance();
        user = new HashMap<>();

        email = currentUser.getEmail();
    }

    //Constructor that includes an email
    public FireBaseCloud(String email) {
        db = FirebaseFirestore.getInstance();
        user = new HashMap<>();

        this.email = email;

    }

    public void setup() {
        // [START set_firestore_settings]
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true) //Sets up offline storage
                .build();
        db.setFirestoreSettings(settings);
        // [END set_firestore_settings]
    }

    //Delete all data under the email in Firebase Cloud
    public void deleteData(String email)
    {
        db.collection("users").document(email)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    //Save Email to Firestore cloud as a new user
    public void saveEmail(String email) {
        //Put information in the user hashmap
        user.put("email", email);

        //Save the email into a new user in the database in firestore cloud
        db.collection("users").document(email).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    //Save a habit in Firestore Cloud including all variables in the habit this is stored as a HashMap
    public void saveHabit(HashMap scheduledDays, HashMap completedDays, String description, String reward, int streak)
    {
        Map<String, Object> data = new HashMap<>();
        //Put information in the HashMap "habit"
        data.put("scheduledDays", scheduledDays);
        data.put("completedDays", completedDays);
        data.put("reward", reward);
        data.put("streak", streak);

        if(email != null) //Check if user is logged in
        {
            db.collection("users").document(email).collection("Habit").document(description).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(TAG, "Habit has been saved");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Habit has not been saved");
                }
            });
        }
        else
        {
            Log.w(TAG, "Habit has not been saved");
        }

    }

    //Save a Goal in Firestore Cloud including all variables in the habit this is stored as a HashMap
    public void saveGoal(String date, String description)
    {
        Map<String, Object> data = new HashMap<>();

        data.put("date", date);

        if(email != null) //Check if there is a user logged in
        {
            db.collection("users").document(email).collection("Goals").document(description).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(TAG, "Goal has been saved");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Goal has not been saved");
                }
            });
        }
        else
        {
            Log.w(TAG, "Goal has not been saved");
        }
    }

    //This function is not working.
    public void getEmail(String email) {
        DocumentReference emailRef = db.collection("users").document(email);
        emailRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                String tempEmail = user.getEmail();
            }
        });
    }

    //Retrieve all user data stored in Firestore cloud and save it to variables inside of the user class
    public void getUserData() {
        DocumentReference docRef = db.collection("cities").document("BJ");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
            }
        });
    }
}

