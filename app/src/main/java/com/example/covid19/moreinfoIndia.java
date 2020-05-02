package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
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
import java.util.ArrayList;

public class moreinfoIndia extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String USGS_REQUEST_URL = "https://api.covid19india.org/data.json";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic);


        stateStats task = new stateStats();
        task.execute();


    }


    private void updateUi1(ArrayList<cases> recent) {

        ListView listview = (ListView) findViewById(R.id.list);

        final stateAdapter cAdapter = new stateAdapter(this,recent);

        listview.setAdapter(cAdapter);
    }




    private class stateStats extends AsyncTask<URL, Void, ArrayList<cases>> {

        @Override
        protected ArrayList<cases> doInBackground(URL... urls) {

            URL url = createUrl(USGS_REQUEST_URL);
            String jsonResponse = "";


            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                //TODO Handle the IOException
            }


            ArrayList<cases> recent = extractFeatureFromJson(jsonResponse);

            return recent;
        }

        @Override
        protected void onPostExecute(ArrayList<cases> list) {
            if (list == null) {
                return;
            }

            updateUi1(list);
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

        private ArrayList<cases> extractFeatureFromJson(String caseJSON) {


            ArrayList<cases> temp = new ArrayList<>();
            if (TextUtils.isEmpty(caseJSON)) {
                return null;
            }

            try {

                JSONObject root = new JSONObject(caseJSON);
                JSONArray statewise = root.getJSONArray("statewise");
                int i,n=0;
                n = statewise.length();

                for(i=0;i<n;i++){
                JSONObject state = statewise.getJSONObject(i);

                    long death1 = state.getLong("deaths");
                    long confirmed1 = state.getLong("confirmed");
                    long active1 = state.getLong("active");
                    long recovered1 = state.getLong("recovered");
                    long deldeath1 = state.getLong("deltadeaths");
                    long delconf1 = state.getLong("deltaconfirmed");
                    long delrecovered1 = state.getLong("deltarecovered");
                String updated1 = state.getString("state");

                temp.add(new cases(confirmed1,active1, recovered1, death1, delconf1,deldeath1,delrecovered1,updated1));

                }




                return temp;

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the cases JSON results", e);
            }


            return null;
        }
    }


}
