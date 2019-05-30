package com.kbrs.app.GsmRemoteControl;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.view.Gravity.RIGHT;
import static com.kbrs.app.GsmRemoteControl.ChannelRecyclerAdapter.DIFF_CALLBACK;
import static com.kbrs.app.GsmRemoteControl.SettingsActivity.SAVED_TEXT_GSM;


public class MainActivity extends AppCompatActivity {

    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeListener mShakeListener;


    /*private static final int REQUEST_SMS_SEND_PERMISSION = 10001;
    private static final int REQUEST_SMS_READ_PERMISSION = 10005;
    private static final int REQUEST_SMS_RECEIVE_PERMISSION = 10006;*/
    private static final int REQUEST_MULTIPLE_PERMISSION = 10010;
    public static final int ADD_CHANNEL_REQUEST = 10002;
    public static final int EDIT_CHANNEL_REQUEST = 10003;

    // объявляем разрешение, которое нам нужно получить
    private static final String SMS_SEND_PERMISSION = Manifest.permission.SEND_SMS;
    private static final String SMS_READ_PERMISSION = Manifest.permission.READ_SMS;
    private static final String SMS_RECEIVE_PERMISSION = Manifest.permission.RECEIVE_SMS;
    private static final String READ_PHONE_STATE_PERMISSION = Manifest.permission.READ_PHONE_STATE;
    private static final String INTERNET_PERMISSION = Manifest.permission.INTERNET;
    private static final String ACCESS_NETWORK_STATE_PERMISSION = Manifest.permission.ACCESS_NETWORK_STATE;

    private ChannelsViewModel channelsViewModel;
    private ChannelsRepository channelsRepository;

    String phoneNumber;
    SharedPreferences phone;

    private static long back_pressed;

    @SuppressLint("StaticFieldLeak")
    static Button AllOn;
    @SuppressLint("StaticFieldLeak")
    static Button AllOff;

    AlertDialog shakeDialog;

    View view;

    ChannelRecyclerAdapter adapter;

    private ChannelRecyclerAdapter.OnSwitchClickListener listenerSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Lookup the RecyclerView in activity layout
        RecyclerView rvChannels = findViewById(R.id.RecyclerChannelList);
        rvChannels.setLayoutManager(new LinearLayoutManager(this));
        rvChannels.setHasFixedSize(true);

        adapter = new ChannelRecyclerAdapter();
        rvChannels.setAdapter(adapter);

///////////////////////////////////////////////////////    Read SMS    ////////////////////////////////////////////////////////////////////////
        //addAutoStartup();

        /*if (!dateVSdate()){
            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
        }*/


        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = Objects.requireNonNull(mSensorManager)
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeListener = new ShakeListener();
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {

            @Override
            public void onShake(int count) {
                /* The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.*/

                if (count == 3) {
                    AlertDialog.Builder shakeDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    //view = getLayoutInflater().inflate(R.layout.layout_dialog_on_off, null);

                    shakeDialogBuilder.setTitle("Включение и отключение всех каналов")
                            .setMessage("Выберете действие:")
                            .setPositiveButton("Включить все", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setAll(true);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Выключить все", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setAll(false);
                                    dialog.dismiss();
                                }
                            })
                            .setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    final AlertDialog shakeDialog = shakeDialogBuilder.create();
                    shakeDialog.show();

                }

                /*channelsViewModel = ViewModelProviders.of(MainActivity.this).get(ChannelsViewModel.class);
                channelsViewModel.getAllChannels().observe(MainActivity.this, new Observer<List<Channels>>() {
                    @Override
                    public void onChanged(@Nullable List<Channels> channels) {
                        adapter.notifyDataSetChanged();
                    }
                });*/

            }
        });


///////////////////////////////////////////////////////    Toolbar и NavigationView    ////////////////////////////////////////////////////////////////////////

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        // BottomNavigationViewHelper.disableShiftMode(bottomNav);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                SharedPreferences sharedPreferencesGsm;
                String gsmNumber;

                sharedPreferencesGsm = Objects.requireNonNull(getApplicationContext()).getSharedPreferences(SAVED_TEXT_GSM, MODE_PRIVATE);
                gsmNumber = sharedPreferencesGsm.getString(SAVED_TEXT_GSM, "Номер не задан");

                switch (item.getItemId()) {
                    case R.id.Settings:
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        onPause();
                        break;

                    case R.id.Mode:
                        if (Objects.equals(gsmNumber, "Номер не задан")) {
                            Toast.makeText(getApplicationContext(), "Введите номер GSM устройства", Toast.LENGTH_LONG).show();
                        } else {
                            startActivity(new Intent(MainActivity.this, ModeActivity.class));
                            onPause();
                        }
                        break;

                    case R.id.Information:
                        if (Objects.equals(gsmNumber, "Номер не задан")) {
                            Toast.makeText(getApplicationContext(), "Введите номер GSM устройства", Toast.LENGTH_LONG).show();
                        } else {
                            startActivity(new Intent(MainActivity.this, InformationActivity.class));
                            onPause();
                        }
                        break;

                    case R.id.Event:
                        if (Objects.equals(gsmNumber, "Номер не задан")) {
                            Toast.makeText(getApplicationContext(), "Введите номер GSM устройства", Toast.LENGTH_LONG).show();
                        } else {
                            startActivity(new Intent(MainActivity.this, SmsEventActivity.class));
                            onPause();
                        }
                        break;

                    default:
                        break;
                }

                return false;
            }
        });

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /*// Lookup the RecyclerView in activity layout
        RecyclerView rvChannels = findViewById(R.id.RecyclerChannelList);
        rvChannels.setLayoutManager(new LinearLayoutManager(this));
        rvChannels.setHasFixedSize(true);*/

        /*final ChannelRecyclerAdapter adapter = new ChannelRecyclerAdapter();
        rvChannels.setAdapter(adapter);*/

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        channelsViewModel = ViewModelProviders.of(this).get(ChannelsViewModel.class);
        channelsViewModel.getAllChannels().observe(this, new Observer<List<Channels>>() {
            @Override
            public void onChanged(@Nullable List<Channels> channels) {
                adapter.submitList(channels);
            }
        });

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*
        AllOn = findViewById(R.id.buttonAllOn);
        AllOff = findViewById(R.id.buttonAllOff);*/


///////////////////////////////////////////////////   Запрос разрешений у системы     /////////////////////////////////////////////////////////////////

        // проверяем разрешения: если они уже есть,
        // то приложение продолжает работу в нормальном режиме
        // иначе запрашиваем разрешение у пользователя
        if (!isPermissionGranted()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Разрешение на отправку SMS не получено.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 250);
            toast.show();
            requestPermission();
        }


///////////////////////////////////////////////////    Обработка swipe и move Slave-каналов в RecyclerView    ////////////////////////////////////////

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                int dragFlags = 0;
                int swipeFlags = (position <= 2) ? 0 : ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                return position <= 2 ? 0 : super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(viewHolder.itemView.getContext());
                alertDialog.setTitle("Удаление канала");
                alertDialog.setCancelable(true);
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        dialog.cancel();
                    }
                })
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                channelsViewModel.delete(adapter.getChannelAt(viewHolder.getAdapterPosition()));
                                customToast(MainActivity.this, "Канал удален");
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //channelsViewModel.update(adapter.getChannelAt(viewHolder.getAdapterPosition()));
                                adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        }).attachToRecyclerView(rvChannels);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        adapter.setOnItemLongClickListener(new ChannelRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Channels channels) {
                Intent intent = new Intent(MainActivity.this, AddEditChannelActivity.class);
                intent.putExtra(AddEditChannelActivity.EXTRA_ID, channels.getId());
                intent.putExtra(AddEditChannelActivity.EXTRA_TITLE, channels.getChDesign());
                intent.putExtra(AddEditChannelActivity.EXTRA_NUMBER, String.valueOf(channels.getChName()));
                intent.putExtra(AddEditChannelActivity.EXTRA_CHECKED, String.valueOf(channels.isChSwitch()));
                startActivityForResult(intent, EDIT_CHANNEL_REQUEST);
            }

        });

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        adapter.setOnSwitchClickListener(new ChannelRecyclerAdapter.OnSwitchClickListener() {
            @Override
            public void OnSwitchClick(Channels channels) {
                channelsViewModel.update(channels);
            }
        });

        adapter.setOnSwitchCheckedListener(new ChannelRecyclerAdapter.OnSwitchCheckedListener() {
            @Override
            public void OnSwitchChecked(Channels channels) {
                channelsViewModel.update(channels);
            }
        });

    }

///////////////////////////////////////////      Получение разрешениий от системы в процессе работы      //////////////////////////////////////////

    private boolean isPermissionGranted() {
        // проверяем разрешение - есть ли оно у нашего приложения

        if (needRequestRuntimePermissions()) {
            int permissionCheckSmsSend = ActivityCompat.checkSelfPermission(this, MainActivity.SMS_SEND_PERMISSION);
            int permissionCheckSmsRead = ActivityCompat.checkSelfPermission(this, MainActivity.SMS_READ_PERMISSION);
            int permissionCheckSmsReceive = ActivityCompat.checkSelfPermission(this, MainActivity.SMS_RECEIVE_PERMISSION);
            int permissionCheckInternet = ActivityCompat.checkSelfPermission(this, MainActivity.INTERNET_PERMISSION);
            int permissionCheckAccessNetwork = ActivityCompat.checkSelfPermission(this, MainActivity.ACCESS_NETWORK_STATE_PERMISSION);
            int permissionCheckReadPhoneState = ActivityCompat.checkSelfPermission(this, MainActivity.READ_PHONE_STATE_PERMISSION);

            return permissionCheckSmsRead == PackageManager.PERMISSION_GRANTED
                    & permissionCheckSmsSend == PackageManager.PERMISSION_GRANTED
                    & permissionCheckSmsReceive == PackageManager.PERMISSION_GRANTED
                    & permissionCheckInternet == PackageManager.PERMISSION_GRANTED
                    & permissionCheckAccessNetwork == PackageManager.PERMISSION_GRANTED
                    & permissionCheckReadPhoneState == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }

    }

    private boolean needRequestRuntimePermissions() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_MULTIPLE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { //& grantResults[1] == PackageManager.PERMISSION_GRANTED & grantResults[2] == PackageManager.PERMISSION_GRANTED ){
                customToast(this, "Разрешения получены");
            } else {
                customToast(this, "Разрешения не получены");
                showPermissionDialog(MainActivity.this);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void requestPermission() {
        // запрашиваем разрешение
        //ActivityCompat.requestPermissions(this, new String[]{MainActivity.SMS_SEND_STATE_PERMISSION}, MainActivity.REQUEST_SMS_SEND_STATE);
        ActivityCompat.requestPermissions(this, new String[]{
                MainActivity.SMS_SEND_PERMISSION,
                MainActivity.SMS_READ_PERMISSION,
                MainActivity.SMS_RECEIVE_PERMISSION,
                MainActivity.INTERNET_PERMISSION,
                MainActivity.ACCESS_NETWORK_STATE_PERMISSION,
                MainActivity.READ_PHONE_STATE_PERMISSION}, MainActivity.REQUEST_MULTIPLE_PERMISSION);
        /*boolean hasPermissionSendSMS = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionSendSMS) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.SEND_SMS},
                    REQUEST_SMS_SEND_PERMISSION);
        }

        boolean hasPermissionReceiveSMS = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionReceiveSMS) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.RECEIVE_SMS},
                    REQUEST_SMS_RECEIVE_PERMISSION);
        }

        boolean hasPermissionReadSMS = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionReadSMS) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_SMS},
                    REQUEST_SMS_READ_PERMISSION);
        }*/
    }

    private void showPermissionDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String title = getResources().getString(R.string.app_name);
        builder.setTitle(title);
        builder.setMessage(title + " требует разрешение на отправку и чтение SMS ");

        String positiveText = "Настройки";
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openAppSettings();
            }
        });

        String negativeText = "Выход";
        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();

        ////// display dialog //////////
        dialog.show();
    }

    private void openAppSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQUEST_MULTIPLE_PERMISSION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        phone = this.getSharedPreferences(SettingsActivity.SAVED_TEXT_GSM, MODE_PRIVATE);
        phoneNumber = phone.getString(SettingsActivity.SAVED_TEXT_GSM, "Номер не задан");

        if (requestCode == REQUEST_MULTIPLE_PERMISSION) {
            requestApplicationConfig();
        } else if (requestCode == ADD_CHANNEL_REQUEST && resultCode == RESULT_OK) {
            String titleExtra = data.getStringExtra(AddEditChannelActivity.EXTRA_TITLE);
            String numberExtra = data.getStringExtra(AddEditChannelActivity.EXTRA_NUMBER);

            Channels channels = new Channels(Integer.valueOf(numberExtra), titleExtra, false);

            if (!titleExtra.equals("...") & !titleExtra.isEmpty()) {
                if (phoneNumber.equals("Номер не задан")) {
                    customToast(this, "Номер GSM устройства не задан,\nперейдите в \"Настройки\"");
                } else {
                    SmsManager.getDefault().sendTextMessage(phoneNumber, null, "#005#" +
                            numberExtra + "#" + titleExtra + "#", null, null);
                    customToast(this, "Канал " + titleExtra + " успешно добавлен");
                    channelsViewModel.insert(channels);
                }
            } else {
                customToast(this, "Канал " + numberExtra + " успешно добавлен");
                channelsViewModel.insert(channels);
            }


        } else if (requestCode == EDIT_CHANNEL_REQUEST && resultCode == RESULT_OK) {

            int id = data.getIntExtra(AddEditChannelActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                return;
            }
            String titleExtra = data.getStringExtra(AddEditChannelActivity.EXTRA_TITLE);
            String numberExtra = data.getStringExtra(AddEditChannelActivity.EXTRA_NUMBER);
            String switchExtra = data.getStringExtra(AddEditChannelActivity.EXTRA_CHECKED);

            Channels channels = new Channels(Integer.valueOf(numberExtra), titleExtra, Boolean.valueOf(switchExtra));
            channels.setId(id);

            if (!titleExtra.equals("...") & !titleExtra.isEmpty()) {
                if (phoneNumber.equals("Номер не задан")) {
                    customToast(this, "Номер GSM устройства не задан,\nперейдите в \"Настройки\"");
                } else {
                    SmsManager.getDefault().sendTextMessage(phoneNumber, null, "#005#" + numberExtra + "#" + titleExtra + "#", null, null);
                    customToast(this, "Канал " + titleExtra + " успешно изменен");
                    channelsViewModel.update(channels);
                }
            } else {
                customToast(this, "Канал " + numberExtra + " успешно изменен");
                channelsViewModel.update(channels);
            }

        } else if (requestCode == EDIT_CHANNEL_REQUEST && resultCode == RESULT_FIRST_USER) {
            int id = data.getIntExtra(AddEditChannelActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                return;
            }
            String titleExtra = data.getStringExtra(AddEditChannelActivity.EXTRA_TITLE);
            String numberExtra = data.getStringExtra(AddEditChannelActivity.EXTRA_NUMBER);
            String switchExtra = data.getStringExtra(AddEditChannelActivity.EXTRA_CHECKED);

            Channels channels = new Channels(Integer.valueOf(numberExtra), titleExtra, Boolean.valueOf(switchExtra));
            channels.setId(id);
            if (phoneNumber.equals("Номер не задан")) {
                customToast(this, "Номер GSM устройства не задан,\nперейдите в \"Настройки\"");
            } else {
                SmsManager.getDefault().sendTextMessage(phoneNumber, null, "#005#00#" + numberExtra + "#", null, null);
                customToast(this, "Канал " + numberExtra + " успешно изменен.");
                channelsViewModel.update(channels);
            }

        } else if (requestCode == ADD_CHANNEL_REQUEST && resultCode == 2) {
            int id = data.getIntExtra(AddEditChannelActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                return;
            }
            String titleExtra = data.getStringExtra(AddEditChannelActivity.EXTRA_TITLE);
            String numberExtra = data.getStringExtra(AddEditChannelActivity.EXTRA_NUMBER);
            String switchExtra = data.getStringExtra(AddEditChannelActivity.EXTRA_CHECKED);

            Channels channels = new Channels(Integer.valueOf(numberExtra), titleExtra, Boolean.valueOf(switchExtra));
            channels.setId(id);
            if (phoneNumber.equals("Номер не задан")) {
                customToast(this, "Номер GSM устройства не задан,\nперейдите в \"Настройки\"");
            } else {
                customToast(this, "Канал " + numberExtra + " успешно изменен.");
                channelsViewModel.update(channels);
            }

        } else {
            Log.e("AddEditChannel", "Ошибка передачи данных Intent от AddEditChannelActivity");
            customToast(this, "Канал не был изменен");
        }

    }

    private void requestApplicationConfig() {
        if (isPermissionGranted()) {
            customToast(MainActivity.this, "Разрешения получены");
        } else {
            customToast(MainActivity.this, "Пользователь снова не дал разрешение");
            requestPermission();
        }
    }

    //////////////////////////////////////////////////////////////       Меню       /////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top, menu);
        return true;
    }

    //////////////////////////////////////////////////       Обработка нажатий пунктов меню         ////////////////////////////////////////////////

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.AddChannel:
                Intent intent = new Intent(MainActivity.this, AddEditChannelActivity.class);
                startActivityForResult(intent, ADD_CHANNEL_REQUEST);
                break;

            case R.id.About:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                return true;

            case R.id.AllOn:
                setAll(true);
                break;

            case R.id.AllOff:
                setAll(false);
                break;

            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void setAll(boolean OnOff) {
        int cntAll = 0;

        if (OnOff) {
            for (int i = 0; i <= adapter.getItemCount() - 1; i++) {
                if (!adapter.getChannelAt(i).isChSwitch()) {
                    cntAll++;
                }
            }
        } else {
            for (int i = 0; i <= adapter.getItemCount() - 1; i++) {
                if (adapter.getChannelAt(i).isChSwitch()) {
                    cntAll++;
                }
            }
        }

        if (cntAll > 0) {
            if (OnOff) {
                channelsViewModel.getAllChannelsOn();
                Toast.makeText(MainActivity.this, "Все каналы будут включены", Toast.LENGTH_SHORT).show();
            } else {
                channelsViewModel.getAllChannelsOff();
                Toast.makeText(MainActivity.this, "Все каналы будут выключены", Toast.LENGTH_SHORT).show();
            }
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(MainActivity.this, "Действие не требуется", Toast.LENGTH_SHORT).show();
        }

    }

    //////////////////////////////////////////////////////////       Custom Toast         //////////////////////////////////////////////////////////

    public static void customToast(Context context, String message) {
        Toast toastMarket = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        //toastMarket.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 150);
        toastMarket.show();
    }


    ///////////////////////////////////////////////////////////////       Exit        //////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {

        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            customToast(getBaseContext(), "Нажмите «Назад» еще раз, чтобы выйти.");
            back_pressed = System.currentTimeMillis();
        }

    }

    ///////////////////////////////////////////////////////////////       Restart        //////////////////////////////////////////////////////////////

    @Override
    public void onResume() {
        mSensorManager.registerListener(mShakeListener, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        super.onResume();
    }

    @Override
    public void onStop() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeListener);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeListener);
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeListener);
        overridePendingTransition(0, 0);
    }

}







    /*@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final SharedPreferences GuestMode = getSharedPreferences("SAVED_TEXT_GROUP_MODE", MODE_PRIVATE);
        final boolean GroupModeStatus = GuestMode.getBoolean("GROUP_MODE", false);
        int id = item.getItemId();

        if (GroupModeStatus) {

            switch (id) {
                case R.id.Settings:
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    break;

                case R.id.InformationActivity:
                    startActivity(new Intent(MainActivity.this, InformationActivity.class));
                    break;

                case R.id.AboutActivity:
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                    break;


            }
        } else {

            switch (id) {
                case R.id.Settings:
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    break;

                case R.id.Mode:
                    startActivity(new Intent(MainActivity.this, ModeActivity.class));
                    break;

                case R.id.InformationActivity:
                    startActivity(new Intent(MainActivity.this, InformationActivity.class));
                    break;

                case R.id.AboutActivity:
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                    break;


            }
        }
        drawerLayout.closeDrawer(Gravity.START, false);
        return false;                                                                               // 'false' For make menu item not selected after click
    }*/