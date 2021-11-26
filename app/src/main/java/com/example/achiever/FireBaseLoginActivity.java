package com.example.achiever;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class FireBaseLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FireBaseCloudStorage mCloud = new FireBaseCloudStorage();

    private static final String TAG = "EmailPassword";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApi mGoogleApi;
    private FirebaseUser currentUser;
    private GoogleSignInAccount googleAccount;
    private GoogleSignInClient mGoogleSignInClient;


    private EditText emailString;
    private EditText passwordString;
    private EditText confirmPasswordString;
    private TextView currentUserText;
    private Button signInPress;
    private Button createAccountPress;
    private Button showHidePress;
    private SignInButton googleSignInPress;
    private Button cancelPress;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_login);

        passwordString =findViewById(R.id.editTextPassword);
        confirmPasswordString = findViewById(R.id.editTextConfirmPassword);
        emailString = findViewById(R.id.editTextEmail);
        currentUserText = findViewById(R.id.current_user_textview);
        signInPress = findViewById(R.id.signInButton);
        createAccountPress = findViewById(R.id.createAccountButton);
        googleSignInPress = findViewById(R.id.sign_in_button);
        showHidePress = findViewById(R.id.showHideButton);
        cancelPress = findViewById(R.id.cancelCreateAccountButton);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END config_signin]

        mCloud.setup();
        mAuth = FirebaseAuth.getInstance();


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

        cancelPress.setOnClickListener(view -> {
            cancelCreateAccount();
        });

    }

    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        googleAccount = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(currentUser);
        showHidePress.setText("Show");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    //Function to Show or hide password
    private void showHidePass() {
        if(showHidePress.getText() == "Show"){
                passwordString.setTransformationMethod(new HideReturnsTransformationMethod());
                confirmPasswordString.setTransformationMethod(new HideReturnsTransformationMethod());
                showHidePress.setText("Hide");
        } else{
                confirmPasswordString.setTransformationMethod(new PasswordTransformationMethod());
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
        googleSignInPress.setVisibility(View.GONE);
        signInPress.setVisibility(View.GONE);
        confirmPasswordString.setVisibility(View.VISIBLE);
        cancelPress.setVisibility(View.VISIBLE);

        if(validEmail() && validPassword()) {

            try {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(FireBaseLoginActivity.this, task -> {
                            if (task.isSuccessful() && passwordString.getText().toString().equals(confirmPasswordString.getText().toString())) {
                                Toast.makeText(FireBaseLoginActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                mCloud.saveEmail(email); //Save Email to Firebase Cloud
                                sendEmailVerification();
                                updateUI(currentUser);
                            }
                            else if(!passwordString.getText().toString().equals(confirmPasswordString.getText().toString()))
                            {
                                Toast.makeText(FireBaseLoginActivity.this, "Password did not match", Toast.LENGTH_SHORT).show();
                                passwordString.setText("");
                                confirmPasswordString.setText("");
                            }
                            else {
                                Toast.makeText(FireBaseLoginActivity.this, "Could not Create Account", Toast.LENGTH_SHORT).show();
                                emailString.setText("");
                                passwordString.setText("");
                                confirmPasswordString.setText("");
                            }
                        });

            } catch (Exception e) {
                Toast.makeText(FireBaseLoginActivity.this, "Invalid Email and/or Password", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void cancelCreateAccount()
    {
        emailString.setText("");
        passwordString.setText("");
        confirmPasswordString.setText("");
        updateUI(currentUser);
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
                                    startActivity(new Intent(this, MainActivity.class));
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
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void signOut() {
        mAuth.signOut();
        Toast.makeText(FireBaseLoginActivity.this, "Logout Success", Toast.LENGTH_SHORT).show();
        updateUI(null);
    }

    private void updatePassword()
    {
        String newPassword ;
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
            confirmPasswordString.setVisibility(View.GONE);
            currentUserText.append("Email");            //Fix This
            emailString.getText().clear();
            passwordString.getText().clear();
            cancelPress.setVisibility(View.GONE);



            currentUserText.setVisibility(View.VISIBLE);

        } else {
            signInPress.setText("Sign In");
            cancelPress.setVisibility(View.GONE);
            signInPress.setVisibility(View.VISIBLE);
            createAccountPress.setVisibility(View.VISIBLE);
            googleSignInPress.setVisibility(View.VISIBLE);
            emailString.setVisibility(View.VISIBLE);
            passwordString.setVisibility(View.VISIBLE);
            showHidePress.setVisibility(View.VISIBLE);
            currentUserText.setVisibility(View.GONE);
            confirmPasswordString.setVisibility(View.GONE);
        }
    }
}