package com.example.areapartments;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity{

     private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private Button btnLogin;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);





        auth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_main);
        inputEmail = findViewById(R.id.loginEmail);
        inputPassword = findViewById(R.id.loginPassword);

        progressBar = findViewById(R.id.progressbar);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();



    }

    private void userLogin(){

        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (email.isEmpty()){
            inputEmail.setError("Email is required");
            inputEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inputEmail.setError("Please enter a valid email");
            inputEmail.requestFocus();
            return;

        }

        if ((password.isEmpty())) {
            inputPassword.setError("Password is required");
            inputPassword.requestFocus();
            return;
        }
        if (password.length()<6){
            inputPassword.setError("Minimum length of password should be 6");
            inputPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    finish();
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }else {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser()!= null){
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }
    }

    public void Clicktosignup(View view) {

        Intent intent = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(intent)  ;
    }
    public void ClicktoLogin(View view) {
       userLogin();
    }

    public void gotoMap (View view) {

        Intent i = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(i);
    }

    public void onBackPressed(){
        super.onBackPressed();
        this.finishAffinity();}


    }
