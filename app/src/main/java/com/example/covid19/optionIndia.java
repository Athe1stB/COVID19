package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class optionIndia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_india);

        String sdesc,s1desc,s = "State wise Stats \u25ba",s1= "SEARCH any city \u25ba";

        TextView t1 = findViewById(R.id.statewise);
        t1.setText(s);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),moreinfoIndia.class);
                startActivity(i);
            }
        });

        TextView t2 = findViewById(R.id.district);
        t2.setText(s1);
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), city.class);
                startActivity(i);
            }
        });

        sdesc = "statistics of each state ";
        s1desc = "Get details of any city by searching its name";
        TextView t3 = findViewById(R.id.statewisedesc);
        t3.setText(sdesc);

        TextView t4 = findViewById(R.id.districtdesc);
        t4.setText(s1desc);

    }
}
