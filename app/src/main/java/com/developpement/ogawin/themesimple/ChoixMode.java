package com.developpement.ogawin.themesimple;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by ogawi on 22/01/2018.
 */



public class ChoixMode extends AppCompatActivity {
    RadioGroup rg;
    Boolean buttonchecked;
    String id;
    Button valider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choixmode);
        buttonchecked=false;
        valider=findViewById(R.id.validerMode);
        rg=findViewById(R.id.rgChoixMode);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);

                id = (String) radioButton.getTag();


                buttonchecked = true;
            }
        });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonchecked && id.equals("papi")) {
                    SharedPreferences settings = getSharedPreferences("prefs", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("mode", "papi");
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), ActivityPapi.class);

                    startActivity(intent);
                    finish();
                }
                else if (buttonchecked && id.equals("essentiel")) {
                    SharedPreferences settings = getSharedPreferences("prefs", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("mode", "essentiel");
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), ActivityEssentiel.class);

                    startActivity(intent);
                    finish();
                }
                else if(!buttonchecked){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.choixmode), Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
