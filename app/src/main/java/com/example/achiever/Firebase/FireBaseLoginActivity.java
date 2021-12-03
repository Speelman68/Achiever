package com.example.achiever.Firebase;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.achiever.MainActivity;
import com.example.achiever.R;
import com.example.achiever.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FireBaseLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    private static final String TAG = "EmailPassword";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseUser currentUser;
    private GoogleSignInAccount googleAccount;
    private GoogleSignInClient mGoogleSignInClient;

//    private SharedPreferences mPrefs = getSharedPreferences("cloud", 0);
//    private SharedPreferences.Editor mEditor = mPrefs.edit();

    private FireBaseCloud mCloud;

    private String currentUserEmail = "";

    private EditText emailString;
    private EditText passwordString;
    private EditText confirmPasswordString;
    private TextView currentUserText;
    private Button signInPress;
    private Button createAccountPress;
    private Button showHidePress;
    private SignInButton googleSignInPress;
    private Button cancelPress;
    private Button changeEmailPress;
    private Button resetPassPress;
    private Button deleteAcntPress;


    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_login);

        Intent i = new Intent(FireBaseLoginActivity.this, FireBaseCloud.class);
        i.putExtra("email", currentUserEmail);

        passwordString =findViewById(R.id.editTextPassword);
        confirmPasswordString = findViewById(R.id.editTextConfirmPassword);
        emailString = findViewById(R.id.editTextEmail);
        currentUserText = findViewById(R.id.current_user_textview);
        signInPress = findViewById(R.id.signInButton);
        createAccountPress = findViewById(R.id.createAccountButton);
        googleSignInPress = findViewById(R.id.sign_in_button);
        showHidePress = findViewById(R.id.showHideButton);
        cancelPress = findViewById(R.id.cancelCreateAccountButton);
        changeEmailPress = findViewById(R.id.changeEmailBtn);
        resetPassPress = findViewById(R.id.resetPassBtn);
        deleteAcntPress = findViewById(R.id.deleteAcntBtn);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END config_signin]

        //mCloud.setup();
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
          //googleSignIn();
        });

        showHidePress.setOnClickListener(view -> {
            showHidePass();
        });

        cancelPress.setOnClickListener(view -> {
            cancelCreateAccount();
        });

        resetPassPress.setOnClickListener(view -> {
            updatePassword();
        });

        changeEmailPress.setOnClickListener(view -> {
            updateEmail();
        });

        deleteAcntPress.setOnClickListener(view -> {
            deleteAccount();
        });

    }

    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        googleAccount = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(currentUser);
        showHidePress.setText("Show");

        mCloud = new FireBaseCloud();

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
        if(cancelPress.getVisibility() == View.GONE)
        {
            emailString.setText("");
            passwordString.setText("");
        }
        googleSignInPress.setVisibility(View.GONE);
        signInPress.setVisibility(View.GONE);
        resetPassPress.setVisibility(View.GONE);
        changeEmailPress.setVisibility(View.GONE);
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
                                emailString.setText("");
                                passwordString.setText("");
                                confirmPasswordString.setText("");
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
                                    ((User) this.getApplication()).setEmail(email);
                                    //mEditor.putString("cloud", email);
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

    /*
    //Google Sign In Functions
    private void googleSignIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
    //End of Google Sign in Functions
    */

    private void signOut() {
        mAuth.signOut();
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(FireBaseLoginActivity.this, "Logout Success", Toast.LENGTH_SHORT).show();
        currentUser = null;
        updateUI(null);
    }

    private void updatePassword()
    {
        if(passwordString.getVisibility() == View.GONE)
        {
            if(validEmail())
            {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = emailString.getText().toString();

                auth.sendPasswordResetEmail(emailString.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");
                                }
                            }
                        });
            }

        }

        cancelPress.setVisibility(View.VISIBLE);
        signInPress.setVisibility(View.GONE);
        createAccountPress.setVisibility(View.GONE);
        //googleSignInPress.setVisibility(View.VISIBLE);
        emailString.setVisibility(View.VISIBLE);
        passwordString.setVisibility(View.GONE);
        showHidePress.setVisibility(View.GONE);
        resetPassPress.setVisibility(View.VISIBLE);
        changeEmailPress.setVisibility(View.GONE);
        currentUserText.setVisibility(View.GONE);
        confirmPasswordString.setVisibility(View.GONE);




    }

    private void updateEmail()
    {
        if(signInPress.getVisibility() == View.GONE)
        {
            if(validEmail())
            {
                if(emailString.getText().toString().equals(confirmPasswordString.getText().toString()))
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    user.updateEmail(emailString.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User email address updated.");
                                        Toast.makeText(FireBaseLoginActivity.this, "Email Updated", Toast.LENGTH_SHORT).show();
                                        emailString.setText("");
                                        confirmPasswordString.setText("");
                                        startActivity(new Intent(FireBaseLoginActivity.this, MainActivity.class));
                                    }
                                }
                            });
                } else {
                    Toast.makeText(FireBaseLoginActivity.this, "Email does not match", Toast.LENGTH_SHORT).show();
                    emailString.setText("");
                    confirmPasswordString.setText("");
                }

            }
        }


        cancelPress.setVisibility(View.VISIBLE);
        signInPress.setVisibility(View.GONE);
        createAccountPress.setVisibility(View.GONE);
        //googleSignInPress.setVisibility(View.VISIBLE);
        emailString.setVisibility(View.VISIBLE);
        emailString.setHint("New Email");
        passwordString.setVisibility(View.GONE);
        showHidePress.setVisibility(View.GONE);
        resetPassPress.setVisibility(View.GONE);
        changeEmailPress.setVisibility(View.VISIBLE);
        currentUserText.setVisibility(View.GONE);
        confirmPasswordString.setVisibility(View.VISIBLE);
        confirmPasswordString.setTransformationMethod(new HideReturnsTransformationMethod());
        confirmPasswordString.setHint("Confirm Email");

    }

    private void deleteAccount()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mCloud.deleteData(user.getEmail());
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                        }
                    }
                });

        //Delete Cloud Data
        mCloud.deleteData(user.getEmail());

        emailString.setText("");
        passwordString.setText("");
        confirmPasswordString.setText("");

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
            //googleSignInPress.setVisibility(View.GONE);
            emailString.setVisibility(View.GONE);
            passwordString.setVisibility(View.GONE);
            showHidePress.setVisibility(View.GONE);
            confirmPasswordString.setVisibility(View.GONE);
            currentUserText.setText("Current User: ");
            currentUserText.append(user.getEmail());
            emailString.getText().clear();
            passwordString.getText().clear();
            cancelPress.setVisibility(View.GONE);
            resetPassPress.setVisibility(View.GONE);
            changeEmailPress.setVisibility(View.VISIBLE);
            currentUserText.setVisibility(View.VISIBLE);
            deleteAcntPress.setVisibility(View.VISIBLE);

            currentUserEmail = currentUser.getEmail();

        } else {
            signInPress.setText("Sign In");
            cancelPress.setVisibility(View.GONE);
            signInPress.setVisibility(View.VISIBLE);
            createAccountPress.setVisibility(View.VISIBLE);
            //googleSignInPress.setVisibility(View.VISIBLE);
            emailString.setVisibility(View.VISIBLE);
            passwordString.setVisibility(View.VISIBLE);
            showHidePress.setVisibility(View.VISIBLE);
            resetPassPress.setVisibility(View.VISIBLE);
            changeEmailPress.setVisibility(View.GONE);
            currentUserText.setVisibility(View.GONE);
            confirmPasswordString.setVisibility(View.GONE);
            deleteAcntPress.setVisibility(View.GONE);
        }
    }

    public String getCurrentUserEmail() {
        return currentUserEmail;
    }
}