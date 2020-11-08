package com.example.areapartments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener {

    // private GoogleMap mMap;

    GoogleMap mMap;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mUsers;
    public HashMap<String, Marker> markerHashMap;
    public String smsReciever;
    ProgressDialog dialog;
    List<Marker> bed1markers = new ArrayList<>();
    List<Marker> bed2markers = new ArrayList<>();
    List<Marker> bed3markers = new ArrayList<>();




    private static final LatLng urdaneta = new LatLng(15.975803, 120.570693);


    //Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



        dialog = new ProgressDialog(this);




        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
       SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        markerHashMap=new HashMap<>();
        mapFragment.getMapAsync(this);


        mUsers  = FirebaseDatabase.getInstance().getReference("Users");



        //mUsers.push().setValue(marker);


    }

   /* @Override
    protected void onResume() {
        super.onResume();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }
    */


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    // public void onMapReady(GoogleMap googleMap) {
    public void onMapReady(GoogleMap googleMap) {



     mMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        //googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(urdaneta, 25));
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 4000, null);

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
       //googleMap.getUiSettings().setZoomControlsEnabled(true);


   /*     mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    UserInformation user = s.getValue(UserInformation.class);
                    LatLng location = new LatLng(user.latitude, user.longitude);
                    mMap.addMarker(new MarkerOptions().position(location).title(user.newName).snippet(user.newNumber)).setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    String savenum = user.newNumber;
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

                }
            }*/

findViewById(R.id.bed1btn).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        int height = 150;
        int width = 150;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.mapmarker);
        Bitmap b=bitmapdraw.getBitmap();
        final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


   mUsers.addValueEventListener(new ValueEventListener() {
       @Override
       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           for (DataSnapshot s: dataSnapshot.getChildren()){
                UserInformation user = s.getValue(UserInformation.class);

               if (user.bedrooms.equals("1")){
                   Marker marker;
                   LatLng location = new LatLng(user.latitude, user.longitude);
                   //mMap.addMarker(new MarkerOptions().position(location).title(user.newName).snippet(user.newNumber)).setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                   marker= mMap.addMarker(new MarkerOptions().position(location).title(user.newName).snippet(user.newNumber));
                   marker.setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                   // mUsers.push().setValue(marker);
                   bed1markers.add(marker);
                   hidebed2markers();
                   hidebed3markers();



                }

           }
       }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
});

        findViewById(R.id.bed2btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int height = 150;
                int width = 150;
                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.marker2);
                Bitmap b=bitmapdraw.getBitmap();
                final Bitmap smallmarker2 = Bitmap.createScaledBitmap(b, width, height, false);




                mUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot s: dataSnapshot.getChildren()){
                            UserInformation user = s.getValue(UserInformation.class);

                            if (user.bedrooms.equals("2")){
                                Marker marker;
                                LatLng location = new LatLng(user.latitude, user.longitude);
                                marker = mMap.addMarker(new MarkerOptions().position(location).title(user.newName).snippet(user.newNumber));
                                marker.setIcon(BitmapDescriptorFactory.fromBitmap(smallmarker2));
                               // mUsers.push().setValue(marker);
                                bed2markers.add(marker);
                                hidebed1markers();
                                hidebed3markers();
                            }

                        }
                    }





                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });



        findViewById(R.id.bed3btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int height = 150;
                int width = 150;
                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.bluemarker);
                Bitmap b=bitmapdraw.getBitmap();
                final Bitmap smallmarker3 = Bitmap.createScaledBitmap(b, width, height, false);




                mUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot s: dataSnapshot.getChildren()){
                            UserInformation user = s.getValue(UserInformation.class);

                            if (user.bedrooms.equals("3")){
                                Marker marker;
                                LatLng location = new LatLng(user.latitude, user.longitude);
                                marker = mMap.addMarker(new MarkerOptions().position(location).title(user.newName).snippet(user.newNumber));
                                marker.setIcon(BitmapDescriptorFactory.fromBitmap(smallmarker3));
                                // mUsers.push().setValue(marker);
                                bed3markers.add(marker);
                                hidebed1markers();
                                hidebed2markers();
                            }

                        }
                    }





                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });






        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {


            @Override
            public void onInfoWindowClick(Marker marker) {

                String name = marker.getTitle();
                String number = marker.getSnippet();
                smsReciever=number;
                new AlertDialog.Builder(MapsActivity.this)
                        .setTitle("Send Message to "+name)
                        .setMessage("Do you want to send a message to "+number+ " for renting inquiries?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendSms(smsReciever);
                            }

                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();



            }
        });


    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;





    }

    public void hidebed1markers(){
        for (Marker marker : bed1markers){
            marker.remove();
        }
    }

    public void hidebed2markers(){
        for (Marker marker : bed2markers){
            marker.remove();
        }
    }

    public void hidebed3markers(){
        for (Marker marker : bed3markers){
            marker.remove();
        }
    }

    //SMS Method
    private void sendSms(String smsReciever) {
        dialog.setMessage("Sending Message. Please wait.... ");
        dialog.show();
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        String message = "Hi. I would like to inquire about the apartments/bording house available for rental.";

        PendingIntent sentPI = PendingIntent.getBroadcast(MapsActivity.this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(MapsActivity.this, 0,
                new Intent(DELIVERED), 0);


        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        dialog.dismiss();
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();

                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        dialog.dismiss();
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        dialog.dismiss();
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        dialog.dismiss();
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        dialog.dismiss();
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        dialog.dismiss();
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        dialog.dismiss();
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(smsReciever, null, message, sentPI, deliveredPI);
    }



    //POJO to hold locations:
    public class MapLocation {
        public MapLocation(double lt, double ln, String t, String n){
            lat = lt;
            lon = ln;
            title = t;
            number = n;
        }
        public double lat;
        public double lon;
        public String title;
        public String number;
    }
}
