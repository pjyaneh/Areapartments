package com.example.areapartments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import android.text.TextWatcher;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{





    private FusedLocationProviderClient client;
    ProgressBar progressBar;
    EditText txtEmail, txtPassword, txtName, txtNumber, txtLatitude, txtLongitude, txtConfirmPassword;
    private FirebaseAuth firebaseAuth;
    Spinner dropdown;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        client = LocationServices.getFusedLocationProviderClient(this);
        progressBar = findViewById(R.id.progressbar);
        txtEmail = findViewById(R.id.editTextEmail);
        txtPassword = findViewById(R.id.editTextPassword);
        txtConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        txtName = findViewById(R.id.regName);
        txtNumber = findViewById(R.id.regNumber);
        firebaseAuth = FirebaseAuth.getInstance();
        txtLatitude = findViewById(R.id.regLatitude);
        txtLongitude = findViewById(R.id.regLongitude);
        findViewById(R.id.getLatLng).setOnClickListener(this);

        findViewById(R.id.btnRegister).setOnClickListener(this);
        //get the spinner from the xml.
        dropdown = findViewById(R.id.spinner1);
//create a list of items for the spinner.
        String[] items = new String[]{"","1", "2", "3"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);


    }
    @Override
    protected void onStart() {

        super.onStart();
        if (firebaseAuth.getCurrentUser()!=null){



        }
    }


    private void registerUser() {


        final String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString();
        String confirmpassword = txtConfirmPassword.getText().toString();
        final String name = txtName.getText().toString();
        final String myBedrooms = dropdown.getSelectedItem().toString();
        String nolatitude = txtLatitude.getText().toString();
        String nolongitude = txtLongitude.getText().toString();
        final String number = txtNumber.getText().toString().trim();

        if (email.isEmpty()) {
            txtEmail.setError("Email is required");
            txtEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmail.setError("Please enter a valid email");
            txtEmail.requestFocus();
            return;

        }


        if ((password.isEmpty())) {
            txtPassword.setError("Password is required");
            txtPassword.requestFocus();
            return;
        }

        if ((confirmpassword.isEmpty())) {
            txtPassword.setError("Password is required");
            txtPassword.requestFocus();
            return;
            }
        else {
            if (!password.equals(confirmpassword)) {
                txtPassword.setError("Password did not match");
                txtPassword.requestFocus();
                return;
            }
        }

        if (password.length()<6){
            txtPassword.setError("Minimum length of password should be 6");
            txtPassword.requestFocus();
            return;
        }

       if (name.isEmpty()){
            txtName.setError("name is required");
            txtName.requestFocus();
            return;
        }

        if (number.isEmpty()){
        txtNumber.setError("number is required");
        txtNumber.requestFocus();
        return;
        }


        if (nolatitude.isEmpty()){
            txtLatitude.setError("longitude is required");
            txtLatitude.requestFocus();
            return;

        }

        if (nolongitude.isEmpty()){
            txtLongitude.setError("latitude is required");
            txtLongitude.requestFocus();
            return;
        }
        if (myBedrooms.isEmpty()){
            ((TextView) dropdown.getSelectedView()).setError("Please select how many bedrooms");
            dropdown.requestFocus();
            return;
        }





        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                 Double latitude = Double.parseDouble(txtLatitude.getText().toString().trim());
                 Double longitude = Double.parseDouble(txtLongitude.getText().toString().trim());
                if (task.isSuccessful()){
                    UserInformation userInformation = new UserInformation(
                            name, latitude,longitude,number, myBedrooms

                            );
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(userInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                txtEmail.getText().clear();
                                txtPassword.getText().clear();
                                txtConfirmPassword.getText().clear();
                                txtName.getText().clear();
                                txtLatitude.getText().clear();
                                txtLongitude.getText().clear();
                                txtNumber.getText().clear();
                                Toast.makeText(SignupActivity.this, "User registered successfully", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SignupActivity.this, ProfileActivity.class));
                            }else {
                                Toast.makeText(SignupActivity.this, "User registration failed", Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                }
                else {
                    Toast.makeText(SignupActivity.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
                if (task.getException() instanceof FirebaseAuthUserCollisionException){
                    Toast.makeText(getApplicationContext(),"You are already registered",Toast.LENGTH_SHORT).show();
                }

            }
                });
    }
        /*
                if (!task.isSuccessful()){
                    Toast.makeText(SignupActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                        else {
                   startActivity(new Intent(SignupActivity.this, ProfileActivity.class));
                   finish();
                }


              else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_SHORT).show();

             } */


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
            registerUser();


           break;

            case R.id.getLatLng:
                if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

                    return;
                }
                client.getLastLocation().addOnSuccessListener(SignupActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location!=null){
                            txtLatitude.setText(String.valueOf(location.getLatitude()));
                            txtLongitude.setText(String.valueOf(location.getLongitude()));
                        }

                    }
                });

        }
    }
}
