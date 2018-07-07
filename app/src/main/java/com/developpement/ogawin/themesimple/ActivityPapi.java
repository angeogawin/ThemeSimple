package com.developpement.ogawin.themesimple;

/**
 * Created by ogawi on 22/01/2018.
 */


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityPapi extends AppCompatActivity {

    PackageManager pm;

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private PrefManager prefManager;

    private ImageView SettingsButton;
    private RelativeLayout activity;
    private RelativeLayout layoutpapislide1;

    private  ImageView phoneButton;
    private ImageView msgButton;
    private TextClock clock;
    private TextView mTodayDate;
    private ImageView galleryButton;
    private  ImageView apnButton;
    private  ImageView planeButton;
    private  ImageView torcheButton;
    private ImageView bluetoothButton;
    private  ImageView bellButton;

    private  Boolean torcheState;
    private  ImageView musiqueButton;

    CameraDevice cameraDevice;

    private CameraManager cameraManager;

    private CameraCharacteristics cameraCharacteristics;

    String cameraId;

    boolean statusOfBluetooth;
    boolean statusOfPlane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        torcheState=false;

      //  getWindow().addFlags(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);
        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (prefManager.isFirstTimeLaunch()) {
            launchWelcomeScreen();
            finish();
        }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
         //   getWindow().addFlags(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }

        //desactiver status bar
        if(defineIsolationButtonAndDefaultButtonState()){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        //fin

        setContentView(R.layout.activity_papi);

        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
/*disable data*/
        ConnectivityManager dataManager;
        dataManager  = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        Method dataMtd = null;
        try {
            dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
            try {
                dataMtd.setAccessible(true);
                dataMtd.invoke(dataManager, false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


        /*end disable data*/

      /*  SharedPreferences settings = getSharedPreferences("prefs", 0);

        activity.getRootView().setBackgroundColor(settings.getInt("color",0));*/


        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.papi_slide2,
                R.layout.papi_slide1
                };

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);











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

        startActivity(new Intent(ActivityPapi.this, WelcomeActivity.class));
        finish();
    }

    //	viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
             if(position==1) {
              //   getWindow().addFlags(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                SettingsButton = findViewById(R.id.settingsButton);
                bluetoothButton=findViewById(R.id.bluetoothButton);
                torcheButton=findViewById(R.id.torcheButton);
                planeButton=findViewById(R.id.planeButton);
                bellButton=findViewById(R.id.bellButton);
                musiqueButton=findViewById(R.id.musiqueButton);
                 actualizeStatePlane();
                 actualizeStateBluetooth();
                 actualizeStateBell();


                 SettingsButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(ActivityPapi.this, Parametres.class);
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
              /*  pm = getApplicationContext().getPackageManager();

                 // if device support camera?


                 camera = Camera.open();
                 final Camera.Parameters p = camera.getParameters();
                torcheButton.setOnClickListener(new View.OnClickListener() {


                    public void onClick(View v) {
                        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                            if (isLighOn) {

                                Log.i("info", "torch is turn off!");

                                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                                camera.setParameters(p);
                                camera.stopPreview();
                                isLighOn = false;

                            } else {

                                Log.i("info", "torch is turn on!");

                                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

                                camera.setParameters(p);
                                camera.startPreview();
                                isLighOn = true;

                            }

                        }
                    }

                });*/
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
                 musiqueButton.setOnClickListener(new View.OnClickListener() {
                     public void onClick(View v) {
                         Intent intent = new Intent("android.intent.action.MUSIC_PLAYER");
                         startActivity(intent);
                     }

                 });
            }

            // changing the next button text 'NEXT' / 'GOT IT'

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
        //    window.setStatusBarColor(Color.BLUE);
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
                mTodayDate = (TextView)findViewById(R.id.today_date);
                /*    galleryButton=findViewById(R.id.galleryButton);
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
                   /* galleryButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("content://media/external/images/media"));
                            startActivity(intent);

                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    });
                    apnButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            Uri uriSavedImage=Uri.fromFile(new File("/sdcard/flashCropped.png"));
                           intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                            startActivityForResult(intent, 1);
                           // startActivity(intent);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        switch (requestCode) {


            case 3:
                actualizeStateBluetooth();
                break;


            case 5:
                actualizeStatePlane();
                break;
            case 6:
                actualizeStateBell();
                break;
        }

    }
    public void flashLightOn() {
        cameraManager = (CameraManager)
                getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];

        } catch (CameraAccessException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.activityessentiel1), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.activityessentiel1), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.activityessentiel2), Toast.LENGTH_SHORT).show();
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





    public class customViewGroup extends ViewGroup {

        public customViewGroup(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            Log.v("customViewGroup", "**********Intercepted");
            return true;
        }
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
