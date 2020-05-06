package com.example.covid_19symptomtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class HeatMapActivity extends AppCompatActivity {

    private int cases[] = new int[72];
    private String counties[] = new String[72];
    private final double[] latLongDoubles = {43.9747, -89.815620, 46.3983, -90.681316, 45.4544, -91.879245,46.6185, -91.199515,44.4901, -88.0039248,44.3433, -91.768913,45.8440, -92.365815,44.1098, -88.166348,45.0554, -91.262462,44.7923, -90.551234,43.4428, -89.306156,43.2392, -90.962716,43.0729, -89.4056488,43.4213, -88.670088,45.0472, -87.244927,46.5233, -91.878744,44.9744, -91.945743,44.7044, -91.328198,45.7950, -88.38004,43.7401, -88.5209101,45.6358, -88.69709,42.8577, -90.717151,42.6670, -89.603836,43.8015, -89.077619,42.9933, -90.143923,46.3568, -90.29575,44.3458, -90.952420,43.0424, -88.740883,43.8679, -90.142326,42.5556, -87.9882166,44.5431, -87.610220,43.9056, -91.1206114,42.6349, -90.106716,45.2903, -89.010619,45.3281, -89.698228,44.1049, -87.783681,44.8883, -89.7118134,45.3586, -88.004241,43.7909, -89.443415,44.8777, -88.59694,43.0281, -87.9440947,43.9196, -90.594144,44.9880, -88.249137,45.7200, -89.576135,44.3641, -88.4578176,43.3627, -87.951986,44.5651, -92.06467,44.7156, -92.426241,45.4047, -92.491144,44.4873, -89.480470,45.6274, -90.378914,42.7390, -87.9285195,43.2604, -90.387318,42.6760, -89.0604160,45.4416, -91.042714,45.0159, -92.450484,43.3847, -89.944161,45.8209, -91.268116,44.7731, -88.697841,43.7071, -87.8995115,45.2098, -90.457920,44.2524, -91.370128,43.5899, -90.837529,46.0510, -89.536221,42.6442, -88.5687102,45.8970, -91.773815,43.3492, -88.2199131, 43.0266, -88.2745389, 44.4518, -88.960652, 44.1170, -89.249424, 44.0881, -88.6515166, 44.4744, -89.986774};
    private ArrayList<LatLng> locations = new ArrayList<LatLng>();
    private LatLng wiLatLong = new LatLng(44.500000, -89.500000);

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap mMap;
    JSONObject attributes;

    public class FetchData {

        private void retrieveFileFromUrl() {
            new GetJsonFile().execute();
        }

        private class GetJsonFile extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {

                try {
                    /* URL for Covid-19 cases in all 72 WI counties
                     *
                     * Data query not in optimal format: first few items list data properties, then the data
                     * for all 72 counties is listed, and finally there is a bunch of data with null as the
                     * name. The number of null entries is data from previous days that I've attempted to
                     * remove using the AcrGIS REST API syntax, but have only been able to null out. We only
                     * need to recover data from the 72 counties listed at the top of the JSON structure, and
                     * the rest of the data is just bulk we'll need to store and forget.
                     *
                     * County name under "features" -> "attributes" -> "NAME"
                     * Number of positive Covid-19 cases under "features" -> "attributes" -> "POSITIVE"
                     */

                    // Open a stream from the URL
                    InputStream stream = new URL("https://services1.arcgis.com/ISZ89Z51ft1G16OK/ArcGIS/rest/services/COVID19_WI/FeatureServer/10/query?where=GEO+%3D+%27County%27&objectIds=&time=&resultType=none&outFields=NAME%2C+POSITIVE&returnIdsOnly=false&returnUniqueIdsOnly=false&returnCountOnly=false&returnDistinctValues=false&cacheHint=false&orderByFields=LoadDttm+desc&groupByFieldsForStatistics=&outStatistics=&having=&resultOffset=&resultRecordCount=&sqlFormat=none&f=pjson&token=").openStream();

                    String line;
                    StringBuilder result = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                    while ((line = reader.readLine()) != null) {
                        // Read and save each line of the stream
                        result.append(line);
                    }

                    // Close the stream
                    reader.close();
                    stream.close();

                    JSONObject object = new JSONObject(result.toString());
                    JSONArray features = object.getJSONArray("features");

                    for(int i = 0; i < counties.length; i++){
                        attributes = features.getJSONObject(i); // Generic JSONObject in JSONArray
                        attributes = attributes.getJSONObject("attributes"); // Actual attributes object
                        counties[i] = attributes.getString("NAME");
                        cases[i] = attributes.getInt("POSITIVE");
                    }

                } catch (IOException e) {
                    Log.e("get data", "GeoJSON file could not be read");
                } catch (JSONException e) {
                    Log.e("get data", "GeoJSON file could not be converted to a JSONObject");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                populateMap();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heat_map);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_map);

        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;

            //create arrayList of 72 latLong objects for each WI county
            locations = createLatLongArray(latLongDoubles);

            FetchData fetchData = new FetchData();
            fetchData.retrieveFileFromUrl();

            //display user location
            displayMyLocation();
            //set center and zoom level of map
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(wiLatLong,7));

        });
        //obtain a FusedLocationProviderClient
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    //create arrayList of LatLong objects for each county from array of values
    public ArrayList<LatLng> createLatLongArray(double[] latLongDoubles) {
        ArrayList<LatLng> tempArrayList = new ArrayList<LatLng>();
        for (int i = 0; i<144; i = i+2) {
            tempArrayList.add(new LatLng(latLongDoubles[i], latLongDoubles[i+1]));
        }
        return tempArrayList;
    }

    //populate the map with markers from the JSON file
    public void populateMap() {
        //draw a cases-corresponding sized circle for each county
        for (int i=0; i<72; i++) {
            drawCircle(locations.get(i), cases[i], counties[i]);
        }
    }

    //draw circle for each county and populate their properties based off of respective # of cases
    public void drawCircle(LatLng center, int cases, String name){

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(center);

        //draw based off of cases, range from 0 to 3,000
        if (cases < 1) {
            //do nothing if zero cases
            mMap.addMarker(new MarkerOptions().position(center).title(name + ": " + cases).alpha((float)0.20)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.outputonlinepngtools)));
        } else if (cases<10) {
            //less than 10 cases
            circleOptions.radius(cases * 15);
            circleOptions.strokeColor(Color.RED);
            circleOptions.fillColor(0x30ff0000);
            circleOptions.strokeWidth(2);
            mMap.addMarker(new MarkerOptions().position(center).title(name + ": " + cases).alpha((float)0.40)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.outputonlinepngtools)));
        } else if (cases < 100) {
            //between 10 and 100
            circleOptions.radius(cases * 15);
            circleOptions.strokeColor(Color.RED);
            circleOptions.fillColor(0x30ff0000);
            circleOptions.strokeWidth(2);
            mMap.addMarker(new MarkerOptions().position(center).title(name + ": " + cases).alpha((float)0.60)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.outputonlinepngtools)));
        } else if (cases < 1000) {
            //between 1,000 and 2,000 cases
            circleOptions.radius(cases * 15);
            circleOptions.strokeColor(Color.RED);
            circleOptions.fillColor(0x30ff0000);
            circleOptions.strokeWidth(2);
            mMap.addMarker(new MarkerOptions().position(center).title(name + ": " + cases).alpha((float)0.90)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.outputonlinepngtools)));
        } else {
            //more than 2,000 cases
            circleOptions.radius(cases * 15);
            circleOptions.strokeColor(Color.RED);
            circleOptions.fillColor(0x30ff0000);
            circleOptions.strokeWidth(2);
            mMap.addMarker(new MarkerOptions().position(center).title(name + ": " + cases).alpha((float)1)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.outputonlinepngtools)));
        }
        mMap.addCircle(circleOptions);
    }


    private void displayMyLocation() {
        //check if permission granted
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        //if not, ask for it during runtime
        if (permission==PackageManager.PERMISSION_DENIED) {
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
                    //display marker at current location
                    LatLng myLocLatLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(myLocLatLng).title("My Location"));
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

