package com.developpement.ogawin.themesimple;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Created by ogawi on 22/01/2018.
 */

public class ChoixCouleur extends AppCompatActivity {
    Button bleu;
    Button rouge;
    Button jaune;
    Button noir;
    Button rose;
    Button vert;
    RelativeLayout essentiel;
    RelativeLayout papi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choixcouleur);
        bleu=findViewById(R.id.bleu);
        rouge=findViewById(R.id.rouge);
        jaune=findViewById(R.id.jaune);
        noir=findViewById(R.id.noir);
        rose=findViewById(R.id.rose);
        vert=findViewById(R.id.vert);




        bleu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("prefs", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("color",Color.parseColor("#2b78e4"));
                editor.apply();
                Intent i=new Intent(getApplicationContext(),Page1.class);
                startActivity(i);
            }
        });
        rouge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("prefs", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("color",Color.parseColor("#cf2a27"));

                editor.apply();
                Intent i=new Intent(getApplicationContext(),Page1.class);
                startActivity(i);

            }
        });
        jaune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("prefs", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("color",Color.parseColor("#FFF168"));

                editor.apply();
                Intent i=new Intent(getApplicationContext(),Page1.class);
                startActivity(i);

            }
        });
        noir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("prefs", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("color",Color.parseColor("#000000"));

                editor.apply();
                Intent i=new Intent(getApplicationContext(),Page1.class);
                startActivity(i);

            }
        });
        rose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("prefs", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("color",Color.parseColor("#ff00ff"));

                editor.apply();
                Intent i=new Intent(getApplicationContext(),Page1.class);
                startActivity(i);

            }
        });
        vert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("prefs", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("color",Color.parseColor("#009e0f"));

                editor.apply();
                Intent i=new Intent(getApplicationContext(),Page1.class);
                startActivity(i);

            }
        });




    }
}
