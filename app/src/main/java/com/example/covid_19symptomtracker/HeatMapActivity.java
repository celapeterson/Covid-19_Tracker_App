package com.example.covid_19symptomtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class HeatMapActivity extends AppCompatActivity {

    //PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is just a constant defined in the activity to
    //identify a request for this particular permission
    //could have been any number
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;

    private FusedLocationProviderClient mFusedLocationProviderClient; //save the instance

    //Somewhere in Australia
    private final LatLng mDestinationLatLng = new LatLng(43.0752778,-89.4063554);
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this is the path to get to the xml file for the main activity
        setContentView(R.layout.activity_heat_map);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_map);

        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            //code to add a new marker and add it to the map
            //googleMap.addMarker(new MarkerOptions());
            //then chain commands to customize the marker (.position, .title)
            mMap.addMarker(new MarkerOptions().position(mDestinationLatLng).title("Destination"));
            //cal it inside here bc map needs to be ready before it is called
            displayMyLocation();
        });
        //obtain a FusedLocationProviderClient
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void displayMyLocation() {
        //check if permission granted
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        //if not, ask for it during runtime
        if (permission== PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            //from here code goes to method that handles this result
        }
        //if permission granted, display marker at current location
        else {
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(this, task -> {
                Location mLastKnownLocation = task.getResult();
                //check to make sure computation was successful
                if (task.isSuccessful() && mLastKnownLocation != null) {
                    //add a polyline between our obtained location and the marker we made in last milestone
                    LatLng myLocLatLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(myLocLatLng).title("My Location"));
                    mMap.addPolyline(new PolylineOptions().add(myLocLatLng,mDestinationLatLng));
                }
            });
        }
    }

    /**
     * Handles the result of the request for location permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode==PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            //if request is cancelled, the result arrays are empty
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayMyLocation();
            }
        }
    }

}
