package com.example.achiever;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class FireBaseLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FireBaseCloudStorage mCloud = new FireBaseCloudStorage();

    private static final String TAG = "EmailPassword";
    private GoogleApi mGoogleApi;
    private FirebaseUser currentUser;
    private GoogleSignInAccount googleAccount;

    private EditText emailString;
    private EditText passwordString;
    private TextView currentUserText;
    private Button signInPress;
    private Button createAccountPress;
    private Button showHidePress;
    private SignInButton googleSignInPress;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_login);

        passwordString =findViewById(R.id.editTextPassword);
        emailString = findViewById(R.id.editTextEmail);
        currentUserText = findViewById(R.id.current_user_textview);
        signInPress = findViewById(R.id.signInButton);
        createAccountPress = findViewById(R.id.createAccountButton);
        googleSignInPress = findViewById(R.id.sign_in_button);
        showHidePress = findViewById(R.id.showHideButton);

        mCloud.setup();
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApi = GoogleSignIn.getClient(this, gso);

        signInPress.setOnClickListener(view -> {
            String email = emailString.getText().toString();
            String password = passwordString.getText().toString();
            signIn(email, password);
        });

        createAccountPress.setOnClickListener(view -> {
            String email = emailString.getText().toString();
            String password = passwordString.getText().toString();
            createAccount(email, password);
        });

        googleSignInPress.setOnClickListener(view -> {
          googleSignIn();
        });

        showHidePress.setOnClickListener(view -> {
            showHidePass();
        });

    }

    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        googleAccount = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(currentUser);
        showHidePress.setText("Show");
    }

    //Function to Show or hide password
    private void showHidePass() {
        if(showHidePress.getText() == "Show"){
                passwordString.setTransformationMethod(new HideReturnsTransformationMethod());
                showHidePress.setText("Hide");
        } else{
                passwordString.setTransformationMethod(new PasswordTransformationMethod());
                showHidePress.setText("Show");
        }
    }

    private boolean validEmail() {
        if(signInPress.getText().equals("Sign In")) {
            if(emailString.getText().toString().isEmpty()) {
                Toast.makeText(FireBaseLoginActivity.this, "Enter Email Address", Toast.LENGTH_SHORT).show();
                return false;
            }else {
                if (emailString.getText().toString().trim().matches(emailPattern)) {
                    return true;
                } else {
                    Toast.makeText(FireBaseLoginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validPassword() {
        if(signInPress.getText().equals("Sign In")) {
            if(passwordString.getText().toString().length() < 6) {
                Toast.makeText(FireBaseLoginActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return false;
            }
            else
            {
                return true;
            }
        }
        return true;
    }

    /*Create a new createAccount method that takes in an email address and password
    validates them, and then creates a new user with the createUserWithEmailAndPassword method.*/
    private void createAccount(String email, String password)
    {
        if(validEmail() && validPassword()) {
            try {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(FireBaseLoginActivity.this, task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(FireBaseLoginActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                mCloud.saveEmail(email); //Save Email to Firebase Cloud
                                sendEmailVerification();
                            } else {
                                Toast.makeText(FireBaseLoginActivity.this, "Could not Create Account", Toast.LENGTH_SHORT).show();
                            }
                        });

            } catch (Exception e) {
                Toast.makeText(FireBaseLoginActivity.this, "Invalid Email and/or Password", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void signIn(String email, String password) {
        //Check if the use has put in a valid Email and Password
        if(validEmail() && validPassword())
        {
            try{
                if (signInPress.getText() == "Sign In") {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(FireBaseLoginActivity.this, task -> {
                                if(task.isSuccessful()) {
                                    Toast.makeText(FireBaseLoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                    updateUI(currentUser);

                                }else {
                                    Toast.makeText(FireBaseLoginActivity.this, "Unable to Login", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    signOut();
                }
            }
            catch (Exception e) {
                Toast.makeText(FireBaseLoginActivity.this, "Invalid Email and/or Password", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void googleSignIn()
    {
        //Intent intent = mGoogleApi.
        //startActivityForResult(intent, RC_SIGN_IN);
        Toast.makeText(FireBaseLoginActivity.this, "Unable to Login", Toast.LENGTH_SHORT).show();
    }


    private void signOut() {
        mAuth.signOut();
        Toast.makeText(FireBaseLoginActivity.this, "Logout Success", Toast.LENGTH_SHORT).show();
        updateUI(null);
    }

    private void sendEmailVerification() {
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification()
                .addOnCompleteListener(this, task -> {
                    Log.d(TAG, "Email sent.");
                });
        // [END send_email_verification]
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) { //If user is signed in
            signInPress.setText("Sign Out");
            createAccountPress.setVisibility(View.GONE);
            googleSignInPress.setVisibility(View.GONE);
            emailString.setVisibility(View.GONE);
            passwordString.setVisibility(View.GONE);
            showHidePress.setVisibility(View.GONE);
            currentUserText.append("Email");            //Fix This
            emailString.getText().clear();
            passwordString.getText().clear();

            currentUserText.setVisibility(View.VISIBLE);

        } else {
            signInPress.setText("Sign In");
            createAccountPress.setVisibility(View.VISIBLE);
            googleSignInPress.setVisibility(View.VISIBLE);
            emailString.setVisibility(View.VISIBLE);
            passwordString.setVisibility(View.VISIBLE);
            showHidePress.setVisibility(View.VISIBLE);
            currentUserText.setVisibility(View.GONE);
            currentUserText.setText("");
        }
    }
}