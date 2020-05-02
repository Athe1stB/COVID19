package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class credits extends AppCompatActivity {


    private static final String instaURL = "https://www.instagram.com/athe1stb/?hl=en";
    private static final String FBUrl = "https://www.facebook.com/Athe1stB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        ImageView insta = findViewById(R.id.insta);
        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri eqUri = Uri.parse(instaURL);

                Intent webIntent = new Intent(Intent.ACTION_VIEW,eqUri);
                startActivity(webIntent);
            }
        });

        ImageView fb = findViewById(R.id.fb);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri eqUri = Uri.parse(FBUrl);

                Intent webIntent = new Intent(Intent.ACTION_VIEW,eqUri);
                startActivity(webIntent);
            }
        });
    }
}
