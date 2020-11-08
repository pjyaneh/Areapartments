package com.example.areapartments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class UpdateProfile extends AppCompatActivity implements View.OnClickListener {

   private FusedLocationProviderClient client;


    private EditText editTextName, editTextlatitude, editTextlongitude, editTextPhone;
    Button getLoc;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    Spinner myBedroomUpdate;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(this);



        mAuth = FirebaseAuth.getInstance();
        editTextName = findViewById(R.id.name);
        editTextlatitude = findViewById(R.id.getLatitude);
        editTextlongitude = findViewById(R.id.getLongitude);
        editTextPhone = findViewById(R.id.phone);
        findViewById(R.id.updateUser).setOnClickListener(this);
        findViewById(R.id.getLocation).setOnClickListener(this);
        myBedroomUpdate = findViewById(R.id.spinner_update);


        //get the spinner from the xml.

//create a list of items for the spinner.
        String[] items = new String[]{"","1","2","3"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        myBedroomUpdate.setAdapter(adapter);

    }




    private void  updateUser() {

        String bedrooms = myBedroomUpdate.getSelectedItem().toString().trim();
        String newName = editTextName.getText().toString().trim();
        //double latitude = Double.parseDouble(editTextlatitude.getText().toString().trim());
        //double longitude = Double.parseDouble(editTextlongitude.getText().toString().trim());
        String nolatitude = editTextlatitude.getText().toString();
        String nolongitude = editTextlongitude.getText().toString();
       String newNumber = editTextPhone.getText().toString().trim();

        if (newName.isEmpty()){
            editTextName.setError("name is required");
            editTextName.requestFocus();
            return;
        }

        if (bedrooms.isEmpty()){
            ((TextView) myBedroomUpdate.getSelectedView()).setError("Please select a blood type");
            myBedroomUpdate.requestFocus();
            return;
        }

        if (nolatitude.isEmpty()){
            editTextlatitude.setError("latitude is required");
            editTextlatitude.requestFocus();
            return;
        }

        if (nolongitude.isEmpty()){
            editTextlongitude.setError("longitude is required");
            editTextlongitude.requestFocus();
            return;
        }
        if (newNumber.isEmpty()){
            editTextPhone.setError("number is required");
            editTextPhone.requestFocus();
            return;
        }


        double latitude = Double.parseDouble(editTextlatitude.getText().toString().trim());
        double longitude = Double.parseDouble(editTextlongitude.getText().toString().trim());
        UserInformation userInformation = new UserInformation(newName, latitude, longitude, newNumber, bedrooms);
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(userInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(UpdateProfile.this, "Update Successful", Toast.LENGTH_LONG).show();
                    finish();

                }else {
                    Toast.makeText(UpdateProfile.this, "Update failed", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.updateUser:
               updateUser();
                break;
            case R.id.getLocation:
                if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

                    return;
                }
                client.getLastLocation().addOnSuccessListener(UpdateProfile.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location!=null){
                            editTextlatitude.setText(String.valueOf(location.getLatitude()));
                            editTextlongitude.setText(String.valueOf(location.getLongitude()));
                        }

                    }
                });
                break;



        }
    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},1 );
    }
}
