package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

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

public class media extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String USGS_REQUEST_URL = "https://saurav.tech/NewsAPI/top-headlines/category/health/in.json";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic);


        newsStats task = new newsStats();
        task.execute();


    }


    private void updateUi1(ArrayList<cases> recent) {

        ListView listview = (ListView) findViewById(R.id.list);

        final mediaAdapter cAdapter = new mediaAdapter(this,recent);

        listview.setAdapter(cAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cases cur = cAdapter.getItem(position);
                Uri eqUri = Uri.parse(cur.getMurl());

                Intent webIntent = new Intent(Intent.ACTION_VIEW,eqUri);
                startActivity(webIntent);


            }
        });
    }




    private class newsStats extends AsyncTask<URL, Integer, ArrayList<cases>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

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

                String title="",url="",updated="",imgurl;
                JSONObject root = new JSONObject(caseJSON);
                JSONArray articles = root.getJSONArray("articles");
                int i;
                for(i=0;i<30;i++)
                {
                    JSONObject news = articles.getJSONObject(i);
                    title = news.getString("title");
                    url = news.getString("url");
                    imgurl = news.getString("urlToImage");
                    updated = news.getString("publishedAt");

                    temp.add(new cases(title,url,imgurl,updated));

                }






                return temp;

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the cases JSON results", e);
            }


            return null;
        }
    }



}
