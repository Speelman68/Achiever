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

import java.util.HashMap;
import java.util.Map;

public class FireBaseCloud {
    private final FirebaseFirestore db;
    private static final String TAG = "DocSnippets";

    String email;
    Map<String, Object> user;
    Map<String, Object> habit;
    Map<String, Object> goal;


    public FireBaseCloud() {
        db = FirebaseFirestore.getInstance();
        user = new HashMap<>();
        habit = new HashMap<>();
        goal = new HashMap<>();

        email = "";

    }

    public FireBaseCloud(String email) {
        db = FirebaseFirestore.getInstance();
        user = new HashMap<>();
        habit = new HashMap<>();
        goal = new HashMap<>();

        this.email = email;

    }

    public void setup() {
        // [START set_firestore_settings]
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        // [END set_firestore_settings]
    }

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

    public void saveEmail(String email) {
        user.put("email", email);

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

    public void saveHabit(HashMap scheduledDays, HashMap completedDays, String description, String reward, int streak)
    {
        habit.put("scheduledDays", scheduledDays);
        habit.put("completedDays", completedDays);
        habit.put("description", description);
        habit.put("reward", reward);
        habit.put("streak", streak);
        if(email != null)
        {
            db.collection("users").document(email).collection("objectives").document("habit").set(habit).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void saveGoal(String date, String description)
    {
        goal.put("date", date);
        goal.put("description", description);

        if(email != null)
        {
            db.collection("users").document(email).collection("objectives").document("goal").set(goal).addOnSuccessListener(new OnSuccessListener<Void>() {
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
}

