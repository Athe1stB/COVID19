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

import static com.example.covid19.moreinfoIndia.LOG_TAG;

public class moreinfoWorld extends AppCompatActivity {


    private static final String USGS_REQUEST_URL = "https://corona.lmao.ninja/v2/all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic);

        WorldStats task = new WorldStats();
        task.execute();
    }

    private void updateUi1(ArrayList<cases> recent) {

        ListView lv = (ListView) findViewById(R.id.list);

        final worldinfoAdapter cAdapter = new worldinfoAdapter(this,recent);

        lv.setAdapter(cAdapter);
    }




    private class WorldStats extends AsyncTask<URL, Void, ArrayList<cases>> {

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
                long caases = root.getLong("cases");
                long todayCases = root.getLong("todayCases");
                long  deaths= root.getLong("deaths");
                long todayDeaths = root.getLong("todayDeaths");
                long recovered = root.getLong("recovered");
                long active = root.getLong("active");
                long critical = root.getLong("critical");
                long casesPerOneMillion = root.getLong("casesPerOneMillion");
                long deathsPerOneMillion = root.getLong("deathsPerOneMillion");
                long tests = root.getLong("tests");
                long affectedCountries = root.getLong("affectedCountries");

                temp.add(new cases(caases,"cases"));
                temp.add(new cases(todayCases,"today Cases"));
                temp.add(new cases(deaths,"deaths"));
                temp.add(new cases(todayDeaths,"today Deaths"));
                temp.add(new cases(recovered,"recovered"));
                temp.add(new cases(active,"active"));
                temp.add(new cases(critical,"critical"));
                temp.add(new cases(casesPerOneMillion,"cases Per Million"));
                temp.add(new cases(deathsPerOneMillion,"deaths Per Million"));
                temp.add(new cases(tests,"tests"));
                temp.add(new cases(affectedCountries,"affected Countries"));


                return temp;

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }


            return null;
        }
    }

}
