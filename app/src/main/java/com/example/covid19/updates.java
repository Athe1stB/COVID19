package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

public class updates extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String USGS_REQUEST_URL =
            "https://pomber.github.io/covid19/timeseries.json";
    private static final String USGS_REQUEST_URL1 =
            "https://corona.lmao.ninja/v2/all";
    private static final String USGS_REQUEST_URL2 = "https://api.covid19india.org/data.json";
    private static final String USGS_REQUEST_URL3 = "https://corona.lmao.ninja/v2/countries";


    private  int i = 0;
    private  String country1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updates);

        worldStats task = new worldStats();
        task.execute();

        indiaStats task1 = new indiaStats();
        task1.execute();


        LinearLayout moreIndiainfo = findViewById(R.id.updatedll);
        moreIndiainfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),optionIndia.class);
                startActivity(i);
            }
        });

        LinearLayout moreWorldinfo = findViewById(R.id.updatedll2);
        moreWorldinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),optionWorld.class);
                startActivity(i);
            }
        });






    }


    private void updateindiaUi(cases cur) {

        String confirmed, active , recovered, deaths,delconfirmed,delactive , delrecovered, deldeaths;
        confirmed = Long.toString(cur.confirmed);
        active = Long.toString(cur.active);
        recovered = Long.toString(cur.recovered);
        deaths = Long.toString(cur.deaths);
        delconfirmed = "\u2191"+Long.toString(cur.delconfirmed);
        delactive = "\u2191"+ Long.toString(cur.delactive);
        delrecovered = "\u2191" +Long.toString(cur.delrecovered);
        deldeaths = "\u2191" + Long.toString(cur.deldeaths);

        TextView Tactive = findViewById(R.id.active);
        Tactive.setText(active);

        TextView Tconf = findViewById(R.id.confirmed);
        Tconf.setText(confirmed);

        TextView Tdelconf = findViewById(R.id.delconfirmed);
        Tdelconf.setText(delconfirmed);

        TextView Trecovered = findViewById(R.id.recovered);
        Trecovered.setText(recovered);

        TextView Tdeath = findViewById(R.id.death);
        Tdeath.setText(deaths);

        TextView Tdelactive = findViewById(R.id.delactive);
        Tdelactive.setText(delactive);

        TextView Tdrecovered = findViewById(R.id.delrecovered);
        Tdrecovered.setText(delrecovered);

        TextView Tddeath = findViewById(R.id.deldeath);
        Tddeath.setText(deldeaths);

        String s = "updated few secs ago...";

        TextView state = findViewById(R.id.updated);
        state.setText(s);
    }
    private void updateworldUi(cases recent) {
        String active , recovered, deaths,conf,delconf,deldeaths;
        conf = Long.toString(recent.confirmed);
        active = Long.toString(recent.active);
        recovered = Long.toString(recent.recovered);
        deaths = Long.toString(recent.deaths);
        delconf = "\u2191"+Long.toString(recent.delconfirmed);
        deldeaths = "\u2191"+Long.toString(recent.deldeaths);

        TextView Tactive = (TextView) findViewById(R.id.active2);
        Tactive.setText(active);

        TextView Tconf = (TextView) findViewById(R.id.confirmed2);
        Tconf.setText(conf);

        TextView Trecovered = (TextView) findViewById(R.id.recovered2);
        Trecovered.setText(recovered);

        TextView Tdeath = (TextView) findViewById(R.id.deaths2);
        Tdeath.setText(deaths);

        TextView Tdelconf = (TextView) findViewById(R.id.delconfirmed2);
        Tdelconf.setText(delconf);

        TextView Tdeldeath = (TextView) findViewById(R.id.deldeath2);
        Tdeldeath.setText(deldeaths);


        TextView updat = (TextView) findViewById(R.id.updated2);
        updat.setText(recent.date);
    }

    public class indiaStats extends AsyncTask<URL, Void, cases> {

        @Override
        protected cases doInBackground(URL... urls) {

            URL url = createUrl(USGS_REQUEST_URL2);
            String jsonResponse = "";


            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                //TODO Handle the IOException
            }


            cases recent = extractFeatureFromJson(jsonResponse);

            return recent;
        }

         @Override
        protected void onPostExecute(cases case1) {
            if (case1 == null) {
                return;
            }

            updateindiaUi(case1);
        }

         private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            if(url == null){
                return jsonResponse;
            }
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                if(urlConnection.getResponseCode() == 200){
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }

            }

            catch (IOException e) {

                Log.e(LOG_TAG, "Problem making http request", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        private cases extractFeatureFromJson(String caseJSON) {


            if(TextUtils.isEmpty(caseJSON)){
                return null;
            }

            try {

               JSONObject root = new JSONObject(caseJSON);
               JSONArray statewise = root.getJSONArray("statewise");
               JSONObject india = statewise.getJSONObject(0);
                long death1 = india.getLong("deaths");
                long active1 = india.getLong("active");
                long confirmed = india.getLong("confirmed");
                long recovered1 = india.getLong("recovered");
                long deldeath1 = india.getLong("deltadeaths");
                long delactive1 = india.getLong("deltaconfirmed");
                long delrecovered1 = india.getLong("deltarecovered");
                String updated1 = india.getString("lastupdatedtime");


                return new cases(confirmed,active1, recovered1, death1, delactive1,deldeath1,delrecovered1,updated1);

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the cases JSON results", e);
            }


            return null;
        }
    }

    private class worldStats extends AsyncTask<URL, Void, cases> {

        @Override
        protected cases doInBackground(URL... urls) {

            URL url = createUrl(USGS_REQUEST_URL1);
            String jsonResponse = "";


            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                //TODO Handle the IOException
            }


            cases recent = extractFeatureFromJson(jsonResponse);

            return recent;
        }

        @Override
        protected void onPostExecute(cases recent) {
            if (recent == null) {
                return;
            }

            updateworldUi(recent);
        }

        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            if (url == null) {
                return jsonResponse;
            }
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }

            } catch (IOException e) {

                Log.e(LOG_TAG, "Problem making http request", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        private cases extractFeatureFromJson(String caseJSON) {


            if (TextUtils.isEmpty(caseJSON)) {
                return null;
            }

            try {

                JSONObject root = new JSONObject(caseJSON);
                long conf1 = root.getLong("cases");
                long death1 = root.getLong("deaths");
                long active1 = root.getLong("active");
                long recovered1 = root.getLong("recovered");
                long delconf1 = root.getLong("todayCases");
                long deldeath1 = root.getLong("todayDeaths");
                String updated1 = "Updated few secs ago..";


                return new cases(conf1,active1, recovered1, death1,delconf1,deldeath1,0, updated1);

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the cases JSON results", e);
            }


            return null;
        }
    }


    }
