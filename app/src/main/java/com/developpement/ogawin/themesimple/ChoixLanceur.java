package com.developpement.ogawin.themesimple;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ChoixLanceur extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1);

        if(getSharedPreferences("prefs",0).contains("mode")) {

            SharedPreferences settings = getSharedPreferences("prefs", 0);
            String redirection = settings.getString("mode", "");

            if (redirection.equals("papi")) {
                Intent i = new Intent(this, ActivityPapi.class);
                startActivity(i);
                finish();
            } else if (redirection.equals("essentiel")) {
                Intent i = new Intent(this, ActivityEssentiel.class);
                startActivity(i);
                finish();
            }


        }
        else{
            Intent i = new Intent(this, ActivityPapi.class);
            startActivity(i);
            finish();
        }
    }
}
