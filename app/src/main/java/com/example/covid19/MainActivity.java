package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout t1 = findViewById(R.id.info);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),info.class);
                startActivity(i);
            }
        });
        LinearLayout t2 = findViewById(R.id.media);
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),media.class);
                startActivity(i);
            }
        });

        LinearLayout t3 = findViewById(R.id.symptoms);
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),symptoms.class);
                startActivity(i);
            }
        });
        LinearLayout t4 = findViewById(R.id.updates);
        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),updates.class);
                startActivity(i);
            }
        });

        LinearLayout t5 = findViewById(R.id.measures);
        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),measures.class);
                startActivity(i);
            }
        });
        LinearLayout t6 = findViewById(R.id.sources);
        t6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),source.class);
                startActivity(i);
            }
        });



        LinearLayout t7 = findViewById(R.id.feedback);
        t7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mail = new Intent(Intent.ACTION_SEND);
                mail.putExtra(Intent.EXTRA_EMAIL,new String[]{"iit2019019@iiita.ac.in"});
                mail.putExtra(Intent.EXTRA_SUBJECT,"FEEDBACK REGARDING COVID19 APP");
                mail.putExtra(Intent.EXTRA_TEXT,"");

                mail.setType("message/rfc822");
                startActivity(Intent.createChooser(mail,"CHOOSE MAIL RECIEPENT: "));
            }
        });


        LinearLayout t8 = findViewById(R.id.helpline);
        t8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent call = new Intent(Intent.ACTION_DIAL);
                call.setData(Uri.parse("tel:+911123978046"));
                try{
                startActivity(call);}
                catch (SecurityException e){
                    Log.e("seq exception","CALL PERMISSION DENIED",e);
                }
            }
        });


        LinearLayout t9 = findViewById(R.id.credits);
        t9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(getApplicationContext(),credits.class);
               startActivity(i);
            }
        });






    }
}
