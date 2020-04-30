package com.example.covid_19symptomtracker;

import android.os.AsyncTask;
import android.renderscript.ScriptGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// TODO: May need to change AsyncTask input types
public class GetCountyPositives extends AsyncTask<Void, Void, Void> {
    String jsonData = ""; // Holds data from JSON of WI Covid-19 cases
    String[] counties = new String[72]; // Holds Names of each county
    int[] cases = new int[72]; // Holds number of Covid-19 cases in each county

    /**
     * Retrieves JSON of Covid-19 cases in WI from DHS website
     */
    @Override
    protected Void doInBackground(Void... voids) {
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
            URL url = new URL("https://services1.arcgis.com/ISZ89Z51ft1G16OK/ArcGIS/rest/services/COVID19_WI/FeatureServer/10/query?where=1%3D1&outFields=NAME,POSITIVE,LoadDttm&returnGeometry=false&orderByFields=%20LoadDttm%20DESC&outSR=&f=json");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = ""; // Holds line from JSON
            // Read lines from JSON
            while(line != null) {
                line = bufferedReader.readLine();
                jsonData = jsonData + line;
            }
            // Convert data to JSON object
            JSONObject unformatted = new JSONObject(jsonData);
            // Extract specific JSON Array we want
            JSONArray countyPositives = unformatted.getJSONArray("features");

            // TODO: Translate Javascript into Java
            // TODO: Parse JSON Array
//            // Populate counties and cases arrays
//            for(int i = 1; i <= 72; i++) {
//                counties[i-1] = countyPositives[i].NAME;
//                cases[i-1] = countyPositives[i].POSITIVE;
//            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        // TODO: Export county[] and cases[] to HeatMapActivity

        super.onPostExecute(aVoid);
    }
}