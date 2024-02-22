package com.example.multicare_indigo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import android.os.Looper;
import android.provider.Settings;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HomeMenu extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private TextView logout, profile;
    private ImageView logout_icon, profile_icon, sos;
    private Button show_near;
    FusedLocationProviderClient mFusedLocationClient;
    LocationManager locationManager;

    int PERMISSION_ID = 44;

    String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_menu);

        Intent intentinput = getIntent();
        String user_phone_num = intentinput.getStringExtra("phone_no");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        show_near = (Button) findViewById(R.id.Show_nearby);
        show_near.setOnClickListener(this);


        logout = (TextView) findViewById(R.id.logout);
        logout.setOnClickListener(this);

        logout_icon = (ImageView) findViewById(R.id.logout_icon);
        logout_icon.setOnClickListener(this);

        profile = (TextView) findViewById(R.id.profile);
        profile.setOnClickListener(this);

        sos = (ImageView) findViewById(R.id.imageView3);
        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
                Intent intent = new Intent(getApplicationContext(), SendMessage.class);
                intent.putExtra("numberfrom", user_phone_num);

                String em = message.toString();

                intent.putExtra("emergencyMessage", em);



                startActivity(intent);


            }
        });


        profile_icon = (ImageView) findViewById(R.id.profile_icon);
        profile_icon.setOnClickListener(this);

    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {

        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            getAddress(location.getLatitude(), location.getLongitude());
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
//            latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
//            longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");

            getAddress(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
            case R.id.logout_icon:
                clearSavedCredentials();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;

            case R.id.profile_icon:
            case R.id.profile:
                gotoProfile();
                break;

            case R.id.Show_nearby:
//                Intent intent2 = new Intent(getApplicationContext(), ShowNearby.class);
//                startActivity(intent2);

                break;

            default:
                break;
        }
    }

    private void gotoProfile() {
        Intent intentinput = getIntent();

        String user_phone_num = intentinput.getStringExtra("phone_no");
        String user_email = intentinput.getStringExtra("email");
        String user_name = intentinput.getStringExtra("name");
        String user_blood = intentinput.getStringExtra("blood");
        String user_gender = intentinput.getStringExtra("gender");

        Intent intent = new Intent(getApplicationContext(), ProfileView.class);

        intent.putExtra("name", user_name);
        intent.putExtra("phone_no", user_phone_num);
        intent.putExtra("email", user_email);
        intent.putExtra("blood", user_blood);
        intent.putExtra("gender", user_gender);

        startActivity(intent);
    }


    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(HomeMenu.this, Locale.getDefault());
        String address="";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String  add = obj.getAddressLine(0);

            Log.e("Location", "Address" + add);
            address=add;
            address = "http://maps.google.com/?q=" + Double.toString(lat) + "," + Double.toString(lng);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        message = address;
    }

    private void clearSavedCredentials() {
        // Clear saved user information from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    //////

}

