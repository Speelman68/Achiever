package com.example.achiever.progress;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.achiever.Firebase.FireBaseCloud;
import com.example.achiever.Firebase.FireBaseLoginActivity;
import com.example.achiever.R;
import com.example.achiever.User;

public class ProgressActivity extends AppCompatActivity {

    private User user = new User();
    private FireBaseLoginActivity fireBaseUser = new FireBaseLoginActivity();
    private static String email;
    private FireBaseCloud mCloud = new FireBaseCloud();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        mCloud.getHabitData();
    }
}