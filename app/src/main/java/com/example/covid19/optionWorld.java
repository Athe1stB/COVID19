package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class optionWorld extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_world);

        String sdesc,s1desc,s = "Detailed Stats \u25ba",s1= "SEARCH any country \u25ba";

        TextView t1 = findViewById(R.id.detailworld);
        t1.setText(s);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),moreinfoWorld.class);
                startActivity(i);
            }
        });

        TextView t2 = findViewById(R.id.randostats);
        t2.setText(s1);
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),randomstats.class);
                startActivity(i);
            }
        });

        sdesc = "Extensive details of statistics";
        s1desc = "Get details of any country by searching its name";
        TextView t3 = findViewById(R.id.detailworlddesc);
        t3.setText(sdesc);

        TextView t4 = findViewById(R.id.randostatsdesc);
        t4.setText(s1desc);

    }
}
