package com.developpement.ogawin.themesimple;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by ogawi on 27/09/2017.
 */

public class DefineLauncher extends AppCompatActivity {
    Button define;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.define_launcher);
        define=findViewById(R.id.define);
        define.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_SETTINGS);

                startActivity(intent);

            }
        });


    }

}
