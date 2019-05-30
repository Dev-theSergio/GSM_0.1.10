package com.kbrs.app.GsmRemoteControl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;

public class AboutActivity extends AppCompatActivity {

    ImageView logo;
    TextView titleName;
    TextView version;
    TextView firmware;
    TextView linkWeb;
    //AdView mAdView;

    private int count = 0;
    private long startMillis=0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("О программе");

        logo = findViewById(R.id.AboutLogo);

        titleName =  findViewById(R.id.AboutLabel);
        version = findViewById(R.id.AboutInfo);
        firmware = findViewById(R.id.AboutInfoCPU);
        linkWeb = findViewById(R.id.AboutLink);

        Typeface myCustomFont2 = Typeface.createFromAsset(getAssets(),"font/Circe-Regular.ttf");

        titleName.setTypeface(myCustomFont2);
        version.setTypeface(myCustomFont2);
        firmware.setTypeface(myCustomFont2);
        linkWeb.setTypeface(myCustomFont2);

        linkWeb.setMovementMethod(LinkMovementMethod.getInstance());

        /*mAdView = findViewById(R.id.adView);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713"); //ca-app-pub-2397173881131465~1005819510


        AdRequest adRequest = new AdRequest.Builder()
        .addTestDevice("7C9F8E0C8D3D2CA50FC6058556D9F7AC")
        .build();
        mAdView.loadAd(adRequest);*/


        //////////////////////////////////////////////////////////////      BottomNavigationView        ///////////////////////////////////////////////////////////////

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_about);
        BottomNavigationViewHelper.disableShiftMode(bottomNav);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.Home:
                        startActivity(new Intent(AboutActivity.this, MainActivity.class));
                        onPause();
                        break;

                    case R.id.Settings:
                        startActivity(new Intent(AboutActivity.this, SettingsActivity.class));
                        onPause();
                        break;

                    case R.id.Mode:
                        startActivity(new Intent(AboutActivity.this, ModeActivity.class));
                        onPause();
                        break;

                    case R.id.Information:
                        startActivity(new Intent(AboutActivity.this, InformationActivity.class));
                        onPause();
                        break;

                    case R.id.Event:
                        startActivity(new Intent(AboutActivity.this, SmsEventActivity.class));
                        onPause();
                        break;

                    default:
                        break;
                }

                return false;
            }
        });

        logo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventAction = event.getAction();
                if (eventAction == MotionEvent.ACTION_UP) {

                    //get system current milliseconds
                    long time= System.currentTimeMillis();

                    //if it is the first time, or if it has been more than 3 seconds since the first tap ( so it is like a new try), we reset everything
                    if (startMillis==0 || (time-startMillis> 5000) ) {
                        startMillis=time;
                        count=1;
                    }
                    //it is not the first, and it has been  less than 3 seconds since the first
                    else{ //  time-startMillis< 3000
                        count++;
                    }

                    if (count==3) {
                        Toast.makeText(getApplicationContext(), "Give me more!", Toast.LENGTH_SHORT).show();
                    }
                    else if(count==5){
                        Toast.makeText(getApplicationContext(), "Yeah, baby!", Toast.LENGTH_SHORT).show();
                    }
                    else if (count==7){
                        Toast.makeText(getApplicationContext(), "App developer Sergey Baydenko \nE-mail: thesergio.android@gmail.com", Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
                return false;
            }
        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
        finish();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

}
