package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import static com.example.covid19.updates.LOG_TAG;

public class randomstats extends AppCompatActivity {


    private static final String USGS_REQUEST_URL =
            "https://corona.lmao.ninja/v2/countries";
    String country_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randomstats);



        Button b1 = findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText e1 = findViewById(R.id.edit);
                country_name = e1.getText().toString();

                randomCountryStats task = new randomCountryStats();
                task.execute();
            }
        });



    }



    private void updateworldUi(cases recent) {
        String active , recovered, deaths,conf,delconf,deldeaths;
        conf = Long.toString(recent.confirmed);
        active = Long.toString(recent.active);
        recovered = Long.toString(recent.recovered);
        deaths = Long.toString(recent.deaths);

        TextView Tactive = (TextView) findViewById(R.id.active3);
        Tactive.setText(active);

        TextView Tconf = (TextView) findViewById(R.id.confirmed3);
        Tconf.setText(conf);

        TextView Trecovered = (TextView) findViewById(R.id.recovered3);
        Trecovered.setText(recovered);

        TextView Tdeath = (TextView) findViewById(R.id.death3);
        Tdeath.setText(deaths);


        TextView updat = (TextView) findViewById(R.id.updated3);
        updat.setText(recent.date);
    }


    private class randomCountryStats extends AsyncTask<URL, Void, cases> {

        @Override
        protected cases doInBackground(URL... urls) {

            URL url = createUrl(USGS_REQUEST_URL);
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

                JSONArray root = new JSONArray(caseJSON);
                int n = root.length();
                long conf1 =0,active1=0, recovered1=0, death1=0;
                String updated1 ="updated few secs ago...";

                for(int i=0;i<n;i++){
                    JSONObject country = root.getJSONObject(i);
                    String curcountry = country.getString("country");
                    if(curcountry.compareToIgnoreCase(country_name)==0){
                         conf1 =country.getLong("cases");
                         active1 =country.getLong("active");
                         recovered1 =country.getLong("recovered");
                         death1 =country.getLong("deaths");
                        break;
                    }
                }


                return new cases(conf1,active1, recovered1, death1,0,0,0, updated1);

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the cases JSON results", e);
            }


            return null;
        }
    }

}
