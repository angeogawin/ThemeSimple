package com.developpement.ogawin.themesimple;

/**
 * Created by ogawi on 22/01/2018.
 */


import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityEssentiel extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;

    private PrefManager prefManager;
    private ImageView SettingsButton;

    private  ImageView phoneButton;
    private ImageView msgButton;
    private TextClock clock;
    private TextView mTodayDate;
    private ImageView galleryButton;
    private  ImageView apnButton;
    private  ImageView emailButton;
    private  ImageView mapButton;
    private  ImageView earthButton;
    private ImageView locationButton;
    private  ImageView wifiButton;
    private  ImageView dataButton;
    private  ImageView planeButton;
    private  ImageView torcheButton;
    private ImageView bluetoothButton;
    private  ImageView bellButton;
    private ImageView musiqueButton;

    private RelativeLayout activity;
    private  Boolean torcheState;

    CameraDevice cameraDevice;

    private CameraManager cameraManager;

    private CameraCharacteristics cameraCharacteristics;

    String cameraId;

    boolean statusOfGPS;
    boolean statusOfWifi;
    boolean statusOfData;
    boolean statusOfBluetooth;
    boolean statusOfPlane;



    public static Camera cam = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        torcheState=false;

        // Checking for first time launch - before calling setContentView()
        prefManager = new com.developpement.ogawin.themesimple.PrefManager(this);
        if (prefManager.isFirstTimeLaunch()) {
            launchWelcomeScreen();
            finish();
        }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

      

        setContentView(R.layout.activity_essentiel);

        activity=findViewById(R.id.activityessentiel);
       /* SharedPreferences settings = getSharedPreferences("prefs", 0);


        activity.getRootView().setBackgroundColor(settings.getInt("color",0));*/


        viewPager = (ViewPager) findViewById(R.id.view_pager);

        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.essentiel_slide2,
                R.layout.essentiel_slide1};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);




    }

    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);


        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchWelcomeScreen() {

        startActivity(new Intent(ActivityEssentiel.this, WelcomeActivity.class));
        finish();
    }

    //	viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if(position==1) {
               //initializebutton
                SettingsButton = findViewById(R.id.settingsButton);
                bluetoothButton=findViewById(R.id.bluetoothButton);
                torcheButton=findViewById(R.id.torcheButton);
                planeButton=findViewById(R.id.planeButton);
                bellButton=findViewById(R.id.bellButton);
                locationButton=findViewById(R.id.locationButton);
                wifiButton=findViewById(R.id.wifiButton);
                dataButton=findViewById(R.id.dataButton);
                musiqueButton=findViewById(R.id.musiqueButton);
               // actualize buttons states

                actualizeStateLocalization();
                actualizeStateWifi();
                actualizeStateData();
                actualizeStatePlane();
                actualizeStateBluetooth();
                actualizeStateBell();


                SettingsButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(ActivityEssentiel.this, Parametres.class);
                        startActivity(intent);

                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    }

                });

                planeButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                        startActivityForResult(intent,5);

                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    }

                });

                bluetoothButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                        startActivityForResult(intent,3);

                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    }



                });

                bellButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_SOUND_SETTINGS);
                        startActivityForResult(intent,6);

                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    }



                });

                torcheButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                       if(torcheState==false){
                           flashLightOn();
                       }
                       else if(torcheState==true){
                           flashLightOff();
                       }

                    }

                });

                locationButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(viewIntent,1);
                    }

                });
                wifiButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent viewIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivityForResult(viewIntent,2);

                    }

                });
                dataButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);

                        startActivityForResult(intent,4);

                    }

                });
                musiqueButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent("android.intent.action.MUSIC_PLAYER");
                        startActivity(intent);
                    }

                });
            }


        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //  window.setStatusBarColor(Color.BLUE);
            if (getSharedPreferences("prefs", 0).contains("color")) {
                SharedPreferences settings = getSharedPreferences("prefs", 0);
                window.setStatusBarColor(settings.getInt("color", 0));
            }
            else {
                window.setStatusBarColor(Color.parseColor("#2b78e4"));
            }
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);

            if(position==1){


                phoneButton=findViewById(R.id.phoneButton);
                msgButton=findViewById(R.id.msgButton);
                clock=findViewById(R.id.textClock);
                emailButton=findViewById(R.id.emailButton);
                mapButton=findViewById(R.id.mapButton);
                earthButton=findViewById(R.id.earthButton);
                mTodayDate = (TextView)findViewById(R.id.today_date);

               /* galleryButton=findViewById(R.id.galleryButton);
                apnButton=findViewById(R.id.apnButton);*/

                phoneButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        startActivity(intent);

                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                });
                msgButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setType("vnd.android-dir/mms-sms");
                        startActivity(intent);

                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                });
                clock.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent openClockIntent = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
                        openClockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(openClockIntent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                });
                //Get or Generate Date
                Date todayDate = new Date();

                //Get an instance of the formatter
                DateFormat dateFormat = DateFormat.getDateTimeInstance();

                //If you want to show only the date then you will use
                //DateFormat dateFormat = DateFormat.getDateInstance();

                //Format date

                String todayDateTimeString = dateFormat.format(todayDate).substring(0,12);

                //display Date
                mTodayDate.setText(todayDateTimeString);
                emailButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        try{
                            Intent intent = new Intent(Intent.ACTION_SENDTO);
                            // intent.setType("message/rfc822");
                                 intent.setData(Uri.parse("mailto:"));
                            //    intent.setType("plain/text");
                            startActivity(intent);


                        } catch (ActivityNotFoundException e) {
// show message to user
                        }

                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                });
                mapButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=48.866667,2.333333"));

                        startActivity(intent);

                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                });
                earthButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        String url = "http://www.google.com";

                        intent.setData(Uri.parse(url));
                        startActivity(intent);

                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                });


              /*  galleryButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/external/images/media"));
                        startActivity(intent);

                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                });
                apnButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        startActivity(intent);

                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                });*/



            }

            if (getSharedPreferences("prefs", 0).contains("color")) {
                SharedPreferences settings = getSharedPreferences("prefs", 0);
                view.setBackgroundColor(settings.getInt("color", 0));



            }
            else {
                view.setBackgroundColor(Color.parseColor("#2b78e4"));
            }
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    public void flashLightOn() {
        cameraManager = (CameraManager)
                getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];

        } catch (CameraAccessException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.activityessentiel1), Toast.LENGTH_SHORT).show();
        }
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId,true);
                torcheState=true;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }


    }

    public void flashLightOff() {
        cameraManager = (CameraManager)
                getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];

        } catch (CameraAccessException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.activityessentiel1), Toast.LENGTH_SHORT).show();
        }
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId,false);
                torcheState=false;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



            switch (requestCode) {
                case 1:
                    actualizeStateLocalization();
                    break;

                case 2:
                    actualizeStateWifi();
                    break;

                case 3:
                    actualizeStateBluetooth();
                    break;

                case 4:
                    actualizeStateData();
                    break;
                case 5:
                    actualizeStatePlane();
                    break;
                case 6:
                    actualizeStateBell();
                    break;
            }

    }

    public void actualizeStateLocalization(){
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
        statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(statusOfGPS){
            locationButton.setImageDrawable(getDrawable(R.drawable.localisationon));
        }
        else if(!statusOfGPS){
            locationButton.setImageDrawable(getDrawable(R.drawable.localisationoff));
        }
    }

    public void actualizeStateWifi(){
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


        statusOfWifi = mWifi.isConnected();
        if(statusOfWifi){
            wifiButton.setImageDrawable(getDrawable(R.drawable.wifi));
        }
        else if(!statusOfWifi){
            wifiButton.setImageDrawable(getDrawable(R.drawable.wifioff));
        }
    }






    public void actualizeStateData(){
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mData = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


        statusOfData = mData.isConnected();
        if(statusOfData){
            dataButton.setImageDrawable(getDrawable(R.drawable.dataon));
        }
        else if(!statusOfData){
            dataButton.setImageDrawable(getDrawable(R.drawable.dataoff));
        }

    }

    public void actualizeStatePlane(){


        try {
            statusOfPlane = Settings.Global.getInt(getApplicationContext().getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON)!=0;
            if(statusOfPlane){
                planeButton.setImageDrawable(getDrawable(R.drawable.planeon));
            }
            else if(!statusOfPlane){
                planeButton.setImageDrawable(getDrawable(R.drawable.planeoff));
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }


    }
    public void actualizeStateBluetooth(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.activityessentiel2), Toast.LENGTH_SHORT).show();
        } else {
            statusOfBluetooth=mBluetoothAdapter.isEnabled();
            if(statusOfBluetooth){
                bluetoothButton.setImageDrawable(getDrawable(R.drawable.bluetoothon));
            }
            else if(!statusOfBluetooth){
                bluetoothButton.setImageDrawable(getDrawable(R.drawable.bluetoothoff));
            }

        }

    }
    public void actualizeStateBell(){
        AudioManager audio = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        switch( audio.getRingerMode() ){
            case AudioManager.RINGER_MODE_NORMAL:
                bellButton.setImageDrawable(getDrawable(R.drawable.bell));
                break;
            case AudioManager.RINGER_MODE_SILENT:
                bellButton.setImageDrawable(getDrawable(R.drawable.mute));
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                bellButton.setImageDrawable(getDrawable(R.drawable.bellvibreur));
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean res = super.onKeyDown(keyCode, event);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                // Your Home button logic;
                return false;
            }
        }
            return res;
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




}
