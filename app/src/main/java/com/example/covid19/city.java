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

public class city extends AppCompatActivity {


    private static final String USGS_REQUEST_URL =
            "https://api.covid19india.org/state_district_wise.json";
    String district_name,state_name;
    public String format(String s4)
    {
        String s;
        s=s4.trim();
        int i,n,j;
        String s1="",s2="",s3="";
        n=s.length();
        s=s.toLowerCase();

        for(i=0;i<n;i++)
        {
            char ch = s.charAt(i);

            if(ch!= ' ')
                s1 = s1 + ch;

            else
            {
                if(s1.compareToIgnoreCase("and")!=0)
                {
                    char nn, i0 = s1.charAt(0);
                    nn =Character.toUpperCase(i0);
                    int m = s1.length();
                    s3 = nn + s1.substring(1,m);
                }

                else
                    s3 =s1;

                s2= s2 +s3+ " ";
                s1="";

            }
        }
        Log.v("format",s2);
        return s2;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district);



        Button b1 = findViewById(R.id.search);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1,s2;
                EditText e1 = findViewById(R.id.stateedit);
                s1 = e1.getText().toString();
                EditText e2 = findViewById(R.id.distedit);
                s2 = e2.getText().toString();

                state_name = s1.trim();
                district_name = s2.trim();


                districtstats task = new districtstats();
                task.execute();
            }
        });



    }



    private void updateworldUi(cases cur) {
        String confirmed, active , recovered, deaths,delconfirmed,delactive , delrecovered, deldeaths;
        confirmed = Long.toString(cur.confirmed);
        active = Long.toString(cur.active);
        recovered = Long.toString(cur.recovered);
        deaths = Long.toString(cur.deaths);
        delconfirmed = "\u2191"+Long.toString(cur.delconfirmed);
        delactive = "\u2191"+ Long.toString(cur.delactive);
        delrecovered = "\u2191" +Long.toString(cur.delrecovered);
        deldeaths = "\u2191" + Long.toString(cur.deldeaths);

        TextView Tactive = findViewById(R.id.distactive);
        Tactive.setText(active);

        TextView Tconf = findViewById(R.id.distconfirmed);
        Tconf.setText(confirmed);

        TextView Tdelconf = findViewById(R.id.distdelconfirmed);
        Tdelconf.setText(delconfirmed);

        TextView Trecovered = findViewById(R.id.distrecovered);
        Trecovered.setText(recovered);

        TextView Tdeath = findViewById(R.id.distdeath);
        Tdeath.setText(deaths);

        TextView Tdelactive = findViewById(R.id.distdelactive);
        Tdelactive.setText(delactive);

        TextView Tdrecovered = findViewById(R.id.distdelrecovered);
        Tdrecovered.setText(delrecovered);

        TextView Tddeath = findViewById(R.id.distdeldeath);
        Tddeath.setText(deldeaths);

        String s = "updated few secs ago...";

        TextView state = findViewById(R.id.distupdated);
        state.setText(s);
    }


    private class districtstats extends AsyncTask<URL, Void, cases> {

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

                long conf1,active1=0, recovered1=0 , death1=0 ,delconf =0 , deldeath=0 ,delrecov=0;
                String updated1 = "updated few seconds ago...";

                JSONObject root = new JSONObject(caseJSON);
                JSONObject stateobj = root.getJSONObject(state_name);
                JSONObject distdata = stateobj.getJSONObject("districtData");
                JSONObject districtobj = distdata.getJSONObject(district_name);
                conf1 = districtobj.getLong("confirmed");
                active1 = districtobj.getLong("active");
                recovered1 = districtobj.getLong("recovered");
                death1 = districtobj.getLong("deceased");
                JSONObject delta = districtobj.getJSONObject("delta");
                delconf = delta.getLong("confirmed");
                deldeath = delta.getLong("deceased");
                delrecov = delta.getLong("recovered");


                return new cases(conf1,active1, recovered1, death1,delconf,deldeath,delrecov, updated1);

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the cases JSON results", e);
                return new cases(0,0, 0, 0,0,0,0, "invalid parameters");
            }


        }
    }

}
