package com.developpement.ogawin.themesimple;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by ogawi on 22/01/2018.
 */

public class BulleIsolation extends AppCompatActivity {
    Button datetime;
    long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulleisolation);
        datetime = findViewById(R.id.date_time_set);


        datetime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //verifier que date est future et désactiver bouton lanceur par défaut, et afficher timer avant la destruction
                final DatePicker datePicker = (DatePicker) findViewById(R.id.date_picker);
                TimePicker timePicker = (TimePicker) findViewById(R.id.time_picker);

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());

                time = calendar.getTimeInMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String currentTime = simpleDateFormat.format(new Date());
                boolean datefuture = true;

                try {

                    Date date2 = simpleDateFormat.parse(currentTime);

                    if (time < date2.getTime()) {
                        datefuture = false;
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (!datefuture) {
                    Toast.makeText(getApplicationContext(), "Tu dois définir une date future ", Toast.LENGTH_LONG).show();
                } else {
                    String days = String.valueOf(datePicker.getDayOfMonth());
                    String months = String.valueOf(datePicker.getMonth() + 1);
                    String years = String.valueOf(datePicker.getYear());
                    if (datePicker.getDayOfMonth() < 10) {
                        days = "0" + days;
                    }
                    if (datePicker.getMonth() < 10) {
                        months = "0" + months;
                    }
                    final String date = days + "/" + months + "/" + years + " à " + String.valueOf(timePicker.getCurrentHour()) + ":" +
                            timePicker.getCurrentMinute();


                    AlertDialog.Builder alert = new AlertDialog.Builder(
                            BulleIsolation.this);
                    alert.setTitle("Confirmer?");
                    alert.setMessage("Date de destruction de la bulle:" + date);
                    alert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences settings = getSharedPreferences("prefs", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putLong("timeOfEndIsolation", time);

                            editor.apply();
                            SharedPreferences settings2 = getSharedPreferences("prefs", 0);
                            SharedPreferences.Editor editor2 = settings2.edit();
                            editor2.putString("mode", "papi");
                            editor2.apply();
                            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    });
                    alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });

                    alert.show();


                }
            }
        });
    }



     /*   final View dialogView = View.inflate(this, R.layout.activity_bulleisolation, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());

               // time = calendar.getTimeInMillis();
             //   alertDialog.dismiss();
            }});*/
                // alertDialog.setView(dialogView);
                // alertDialog.show();


}
