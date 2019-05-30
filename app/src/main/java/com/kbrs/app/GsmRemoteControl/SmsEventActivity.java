package com.kbrs.app.GsmRemoteControl;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;


public class SmsEventActivity extends AppCompatActivity {

    String message;
    String date;
    String time;
    TextView smsText;

    public SmsViewModel smsViewModel;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        smsText = findViewById(R.id.smsBodyItem);

        // Lookup the RecyclerView in activity layout
        RecyclerView rvSmsEvent = findViewById(R.id.RecyclerSmsEventList);
        rvSmsEvent.setLayoutManager(new LinearLayoutManager(this));
        rvSmsEvent.setHasFixedSize(true);

        final SmsEventRecycler adapter = new SmsEventRecycler();
        rvSmsEvent.setAdapter(adapter);

        //Toast.makeText(this, String.valueOf(Objects.requireNonNull(rvSmsEvent.getAdapter()).getItemCount()), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, String.valueOf(smsViewModel.isEmpty()), Toast.LENGTH_SHORT).show();

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        smsViewModel = ViewModelProviders.of(this).get(SmsViewModel.class);
        smsViewModel.getAllSms().observe(this, new Observer<List<SmsListTable>>() {
            @Override
            public void onChanged(@Nullable List<SmsListTable> smsListTables) {
                adapter.submitList(smsListTables);


            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_event);
        BottomNavigationViewHelper.disableShiftMode(bottomNav);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.Home:
                        startActivity(new Intent(SmsEventActivity.this, MainActivity.class));
                        onPause();
                        break;

                    case R.id.Settings:
                        startActivity(new Intent(SmsEventActivity.this, SettingsActivity.class));
                        onPause();
                        break;

                    case R.id.Mode:
                        startActivity(new Intent(SmsEventActivity.this, ModeActivity.class));
                        onPause();
                        break;

                    case R.id.Information:
                        startActivity(new Intent(SmsEventActivity.this, InformationActivity.class));
                        onPause();
                        break;

                    default:
                        break;
                }

                return false;
            }
        });
    }


    private BroadcastReceiver broadcastReceiverSmsAccept = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            message = Objects.requireNonNull(b).getString("smsBody");
            date = Objects.requireNonNull(b).getString("smsDate");
            time = Objects.requireNonNull(b).getString("smsTime");
            //SmsListTable smsListTable = new SmsListTable(message, date, time);
            smsViewModel.insert(new SmsListTable(message, date, time));

        }
    };




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_sms_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.DeleteAllSms:
                smsViewModel.deleteAllSms();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
        try {
            unregisterReceiver(broadcastReceiverSmsAccept);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Receiver not registered")) {
                Log.i("Receiver", "unregistered when not registered");
            } else {
                throw e;
            }

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SMS_RECEIVED"); //android.intent.action.SMS_RECEIVED
        registerReceiver(broadcastReceiverSmsAccept, filter);
    }
}
