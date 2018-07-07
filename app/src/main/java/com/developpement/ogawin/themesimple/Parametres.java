package com.developpement.ogawin.themesimple;

/**
 * Created by ogawi on 22/01/2018.
 */

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ogawi on 27/09/2017.
 */

public class Parametres extends AppCompatActivity {
    ListView lv;
    TextView tempsfin;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.parametres);
        tempsfin=findViewById(R.id.tempsdeFin);

        lv=(ListView) findViewById(R.id.listDetailsApp);
        if (getSharedPreferences("prefs", 0).contains("timeOfEndIsolation")) {
            SharedPreferences settings = getSharedPreferences("prefs", 0);
            long timeOfEndIsolation = settings.getLong("timeOfEndIsolation", 0);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            date=new Date(timeOfEndIsolation).toString();

        }
        String[] enonces=new String[]{getString(R.string.parametres4),getString(R.string.parametres5),getResources().getString(R.string.parametres1),getResources().getString(R.string.parametres2),getResources().getString(R.string.parametres3)};
        if(defineIsolationButtonAndDefaultButtonState())

        {
            //hide status bar
            getWindow().addFlags(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            tempsfin.setText(getString(R.string.parametres6) + date);

        }
        else {
            tempsfin.setText("");
        }
       // Toast.makeText(getApplicationContext(), Boolean.toString(isMyAppLauncherDefault()), Toast.LENGTH_LONG).show();
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,enonces){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view =super.getView(position, convertView, parent);

                        TextView textView=(TextView) view.findViewById(android.R.id.text1);

            /*YOUR CHOICE OF COLOR*/
                        textView.setTextColor(Color.BLACK);

                        return view;
                    }


        @Override
                    public boolean isEnabled(int position) {

                        if(position==0 && defineIsolationButtonAndDefaultButtonState()){
                            return false;
                        }
                        if(position==1 && defineIsolationButtonAndDefaultButtonState()){
                            return false;
                        }
                        if(position==3 && defineIsolationButtonAndDefaultButtonState()){
                            return false;
                        }



                      return true;
                    }
                };



        lv.setAdapter(adapter);
        if(defineIsolationButtonAndDefaultButtonState()){
            lv.getAdapter().getView(0, null, lv).setClickable(false);
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3)
            {
                if(position==0){
                    if(!isMyAppLauncherDefault()){
                        Toast.makeText(getApplicationContext(), getString(R.string.parametres7), Toast.LENGTH_LONG).show();
                    }
                    else{
                        Intent intent=new Intent(getApplicationContext(),BulleIsolation.class);
                        startActivity(intent);
                    }
                }


                    else if(position==1){
                    Intent intent = new Intent(getApplicationContext(),DefineLauncher.class);
                    startActivity(intent);

                }

                else if(position==2){
                    Intent i;

                    i =new Intent(Parametres.this,ChoixCouleur.class);

                    startActivity(i);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                }

                else if(position==3) {
                    Intent i = new Intent(Parametres.this, ChoixMode.class);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                else if(position==4){
                    Intent intent=new Intent(getApplicationContext(),APropos.class);
                    startActivity(intent);
                }





            }
        });
       // defineIsolationButtonAndDefaultButtonState();
       /* if (getSharedPreferences("prefs", 0).contains("timeOfEndIsolation")) {
            SharedPreferences settings = getSharedPreferences("prefs", 0);
            long timeOfEndIsolation=settings.getLong("timeOfEndIsolation",0);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String currentTim = simpleDateFormat.format(new Date());

            try {
                long currentTime=simpleDateFormat.parse(currentTim).getTime();
                Toast.makeText(getApplicationContext(), Boolean.toString(lv.getAdapter().getView(0,null,lv).isEnabled() ), Toast.LENGTH_LONG).show();
                if(currentTime<timeOfEndIsolation ){
                    if( lv.getAdapter().getView(0,lv,null).isEnabled())
                    {
                        lv.getAdapter().getView(0,lv,null).setEnabled(false);
                    }
                    if(lv.getAdapter().getView(1,lv,null).isEnabled()){
                        lv.getAdapter().getView(1,lv,null).setEnabled(false);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        else {

        }*/

    }


    /**
     * method checks to see if app is currently set as default launcher
     * @return boolean true means currently set as default, otherwise false
     */
    private boolean isMyAppLauncherDefault() {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_MAIN);
        filter.addCategory(Intent.CATEGORY_HOME);

        List<IntentFilter> filters = new ArrayList<IntentFilter>();
        filters.add(filter);

        final String myPackageName = getPackageName();
        List<ComponentName> activities = new ArrayList<ComponentName>();
        final PackageManager packageManager = (PackageManager) getPackageManager();

        packageManager.getPreferredActivities(filters, activities, null);

        for (ComponentName activity : activities) {
            if (myPackageName.equals(activity.getPackageName())) {
                return true;
            }
        }
        return false;
    }
    public static void resetPreferredLauncherAndOpenChooser(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, FakeLauncherActivity.class);
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Intent selector = new Intent(Intent.ACTION_MAIN);
        selector.addCategory(Intent.CATEGORY_HOME);
        selector.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(selector);

        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
    }

    public boolean defineIsolationButtonAndDefaultButtonState(){
        boolean bulleEnCours=false;
        if (getSharedPreferences("prefs", 0).contains("timeOfEndIsolation")) {
            SharedPreferences settings = getSharedPreferences("prefs", 0);
            long timeOfEndIsolation=settings.getLong("timeOfEndIsolation",0);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String currentTim = simpleDateFormat.format(new Date());
            try {
                long currentTime=simpleDateFormat.parse(currentTim).getTime();

                if(currentTime<timeOfEndIsolation){

                       bulleEnCours=true;

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        else {

        }
        return bulleEnCours;
    }



}