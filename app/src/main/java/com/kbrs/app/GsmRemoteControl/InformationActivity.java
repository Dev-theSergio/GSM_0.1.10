package com.kbrs.app.GsmRemoteControl;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.telephony.SmsManager;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.kbrs.app.GsmRemoteControl.MainActivity.customToast;


public class InformationActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<String> spinnerListNumberPicker;

    AlertDialog alertDialog;

    SharedPreferences gsm;
    String gsmNumber;

    NumberPicker numberPickerCustom;

    RelativeLayout rlInfoConnectDevices;
    RelativeLayout rlInfoSlave;
    RelativeLayout rlInfoNet;
    RelativeLayout rlInfoTemp;
    RelativeLayout rlInfoVoltage;
    RelativeLayout rlInfoPower;
    RelativeLayout rlInfoVoltageDefParam;
    RelativeLayout rlInfoPowerDefParam;
    RelativeLayout rlInfoTempDefParam;

    View view;

    SmsManager smsManager = SmsManager.getDefault();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        setTitle("Информация о системе");

        rlInfoConnectDevices = findViewById(R.id.RelativeInfoConnectDevices);
        rlInfoConnectDevices.setOnClickListener(this);
        rlInfoSlave = findViewById(R.id.RelativeInfoSlave);
        rlInfoSlave.setOnClickListener(this);
        rlInfoNet = findViewById(R.id.RelativeInfoNet);
        rlInfoNet.setOnClickListener(this);
        rlInfoTemp = findViewById(R.id.RelativeInfoTemp);
        rlInfoTemp.setOnClickListener(this);
        rlInfoVoltage = findViewById(R.id.RelativeInfoVoltage);
        rlInfoVoltage.setOnClickListener(this);
        rlInfoPower = findViewById(R.id.RelativeInfoPower);
        rlInfoPower.setOnClickListener(this);
        rlInfoVoltageDefParam = findViewById(R.id.RelativeInfoVoltageDefParam);
        rlInfoVoltageDefParam.setOnClickListener(this);
        rlInfoPowerDefParam = findViewById(R.id.RelativeInfoPowerDefParam);
        rlInfoPowerDefParam.setOnClickListener(this);
        rlInfoTempDefParam = findViewById(R.id.RelativeInfoTempDefParam);
        rlInfoTempDefParam.setOnClickListener(this);

        //////////////////////////////////////////////////////////////      getChannelNumberFromDatabaseToNumberPicker        ///////////////////////////////////////////////////////////////

        PickerViewModel pickerViewModelNumb;
        spinnerListNumberPicker = new ArrayList<>();

        pickerViewModelNumb = ViewModelProviders.of(this).get(PickerViewModel.class);
        pickerViewModelNumb.loadPickerChannels().observe(this, new Observer<List<Picker>>() {
            @Override
            public void onChanged(@Nullable final List<Picker> pickers) {
                Log.d("Size_pickers=", String.valueOf(Objects.requireNonNull(pickers).size()));
                for (int i = 0; i < Objects.requireNonNull(pickers).size(); i++) {
                    if (Objects.requireNonNull(pickers).get(i).chDesign.equals("...")) {
                        spinnerListNumberPicker.add(String.valueOf(pickers.get(i).chName));
                        Log.d("Добавлен канал ", String.valueOf(pickers.get(i).chName));
                    } else {
                        spinnerListNumberPicker.add(String.valueOf(pickers.get(i).chName) + " (" + Objects.requireNonNull(pickers).get(i).chDesign + ")");
                        Log.d("Добавлен канал ", String.valueOf(pickers.get(i).chDesign));
                    }
                    Log.d("Iteration=", String.valueOf(i));
                }
                /*final ArrayAdapter adapter = new ArrayAdapter<>(getApplication(), R.layout.spinner_item, R.id.spinnerCustom1, (Objects.requireNonNull(spinnerListNumberPicker.toArray())));
                spinner.setAdapter(adapter);*/
            }
        });

        //////////////////////////////////////////////////////////////      BottomNavigationView        ///////////////////////////////////////////////////////////////

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_info);
        BottomNavigationViewHelper.disableShiftMode(bottomNav);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.Home:
                        startActivity(new Intent(InformationActivity.this, MainActivity.class));
                        onPause();
                        break;

                    case R.id.Settings:
                        startActivity(new Intent(InformationActivity.this, SettingsActivity.class));
                        onPause();
                        break;

                    case R.id.Mode:
                        startActivity(new Intent(InformationActivity.this, ModeActivity.class));
                        onPause();
                        break;

                    case R.id.Event:
                        startActivity(new Intent(InformationActivity.this, SmsEventActivity.class));
                        onPause();
                        break;

                    default:
                        break;
                }

                return false;
            }
        });

        gsm = this.getSharedPreferences(SettingsActivity.SAVED_TEXT_GSM, MODE_PRIVATE);
        gsmNumber = gsm.getString(SettingsActivity.SAVED_TEXT_GSM, "Номер не задан");

    }


    public void onClick(View v) {

        if (Objects.requireNonNull(gsmNumber).equals("Номер не задан")) {
            customToast(this, "Номер GSM устройства не задан,\nперейдите в \"Настройки\"");
        } else {
            switch (v.getId()) {
                case R.id.RelativeInfoConnectDevices:
                    InfoSendDialog("Запрос состояния каналов", "#777#0#");
                    break;

                case R.id.RelativeInfoSlave:
                    InfoSendDialog("Запрос количества подлюченных устройств", "#777#1#");
                    break;

                case R.id.RelativeInfoNet:
                    InfoSendDialog("Запрос уровня сигнала GSM сети", "#777#5#");
                    break;

                case R.id.RelativeInfoVoltage:
                    ChannelNumberToPicker("Запрос напряжения канала", "#777#", "#3#", false);
                    break;

                case R.id.RelativeInfoPower:
                    ChannelNumberToPicker("Запрос мощности канала", "#777#", "#4#", false);
                    break;

                case R.id.RelativeInfoTemp:
                    InfoSendDialog("Запрос температуры", "#777#6#");
                    break;

                case R.id.RelativeInfoVoltageDefParam:
                    ChannelNumberToPicker("Запрос параметров защиты канала по напряжению", "#222#", "", true);
                    break;

                case R.id.RelativeInfoPowerDefParam:
                    ChannelNumberToPicker("Запрос параметров защиты канала по мощности", "#222#", "#3#", false);
                    break;

                case R.id.RelativeInfoTempDefParam:
                    ChannelNumberToPicker("Запрос параметров защиты канала по температуре", "#333#", "", false);
                    break;

                /*case R.id.RelativeInfoTest:

                 *//*AlertDialog.Builder alertDialogTest = new AlertDialog.Builder(InformationActivity.this);
                    final LayoutInflater inflaterMaster = this.getLayoutInflater();
                    view = inflaterMaster.inflate(R.layout.layout_info_pick_dialog, null);
                    String[] s = new String[spinnerListNumberPicker.size()];
                    s = spinnerListNumberPicker.toArray(s);

                    numberPickerCustom = view.findViewById(R.id.numberPickerCustom);
                    numberPickerCustom.setMaxValue(spinnerListNumberPicker.size());
                    numberPickerCustom.setMinValue(1);
                    numberPickerCustom.setDisplayedValues(s);
                    numberPickerCustom.setWrapSelectorWheel(true);

                    alertDialogTest.setTitle("Тест")
                            .setView(view)
                            .setMessage("Выберете канал")
                            .setPositiveButton("Отправить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String[] channel = (spinnerListNumberPicker.get(numberPickerCustom.getValue() - 1)).split(" ");
                                    Toast.makeText(getApplicationContext(), channel[0], Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .create()
                            .show();*//*
                    ChannelNumberToPicker("Тестовый режим", "#777#", "#2#");
                    break;*/

                default:
                    break;

            }
        }
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
    }

    @SuppressLint("InflateParams")
    private void ChannelNumberToPicker(String title, final String messagePart_1, final String messagePart_2, final boolean withRadioGroup) {

        /////////////////////////////////////////////////////////       Convert ArrayList to String Array       /////////////////////////////////////////////////
        String[] s = new String[spinnerListNumberPicker.size()];
        s = spinnerListNumberPicker.toArray(s);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        final AlertDialog.Builder alertDialogTest;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            alertDialogTest = new AlertDialog.Builder(InformationActivity.this);
        } else {
            alertDialogTest = new AlertDialog.Builder(InformationActivity.this);
        }

        final LayoutInflater inflater = this.getLayoutInflater();


        if (withRadioGroup) {
            view = inflater.inflate(R.layout.layout_info_pick_dialog_radiogroup, null);

            numberPickerCustom = view.findViewById(R.id.numberPickerCustom);
            numberPickerCustom.setMaxValue(spinnerListNumberPicker.size());
            numberPickerCustom.setMinValue(1);
            numberPickerCustom.setDisplayedValues(s);
            numberPickerCustom.setWrapSelectorWheel(true);

            final RadioButton radioGroupInfoMode_1 = view.findViewById(R.id.radioInfoDialogMode_1);
            RadioButton radioGroupInfoMode_2 = view.findViewById(R.id.radioInfoDialogMode_2);

            alertDialogTest
                    .setView(view)
                    .setTitle(title)
                    .setMessage("Для отправки запроса выберете канал и порог:");

            alertDialogTest.setPositiveButton("Отправить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String[] channel = (spinnerListNumberPicker.get(numberPickerCustom.getValue() - 1)).split(" ");
                    try {
                        if(radioGroupInfoMode_1.isChecked()){
                            smsManager.sendTextMessage(gsmNumber, null, messagePart_1 + channel[0] + "#1#", null, null);
                            Toast.makeText(getApplicationContext(), "Запрос отправлен", Toast.LENGTH_SHORT).show();
                        }else{
                            smsManager.sendTextMessage(gsmNumber, null, messagePart_1 + channel[0] + "#2#", null, null);
                            Toast.makeText(getApplicationContext(), "Запрос отправлен", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Запрос не отправлен", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            })
                    .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog = alertDialogTest.create();
            alertDialog.show();


        } else {
            view = inflater.inflate(R.layout.layout_info_pick_dialog, null);

            numberPickerCustom = view.findViewById(R.id.numberPickerCustom);
            numberPickerCustom.setMaxValue(spinnerListNumberPicker.size());
            numberPickerCustom.setMinValue(1);
            numberPickerCustom.setDisplayedValues(s);
            numberPickerCustom.setWrapSelectorWheel(true);

            alertDialogTest
                    .setView(view)
                    .setTitle(title)
                    .setMessage("Для отправки запроса выберете канал:");

            alertDialogTest.setPositiveButton("Отправить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String[] channel = (spinnerListNumberPicker.get(numberPickerCustom.getValue() - 1)).split(" ");
                    try {
                        smsManager.sendTextMessage(gsmNumber, null, messagePart_1 + channel[0] + messagePart_2, null, null);
                        Toast.makeText(getApplicationContext(), "Запрос отправлен", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Запрос не отправлен", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            })
                    .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog = alertDialogTest.create();
            alertDialog.show();
        }


    }

    private void InfoSendDialog(String title, final String message) {
        AlertDialog.Builder alertDialogTest = new AlertDialog.Builder(InformationActivity.this);
        final LayoutInflater inflaterMaster = this.getLayoutInflater();
        view = inflaterMaster.inflate(R.layout.layout_info_simple_dialog, null);
        alertDialogTest.setTitle(title)
                .setView(view)
                .setMessage("Отправить запрос?")
                .setPositiveButton("Отправить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            smsManager.sendTextMessage(gsmNumber, null, message, null, null);
                            Toast.makeText(getApplicationContext(), "Запрос отправлен", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Запрос не отправлен", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog = alertDialogTest.create();
        alertDialog.show();

    }


    /*public void ChannelNumberToSpinner(final Spinner spinner) {
        PickerViewModel pickerViewModel;
        final ArrayList<String> spinnerList;
        spinnerList = new ArrayList<>();

        pickerViewModel = ViewModelProviders.of(this).get(PickerViewModel.class); /// activity!!!!
        pickerViewModel.loadPickerChannels().observe(this, new Observer<List<Picker>>() {
            @Override
            public void onChanged(@Nullable final List<Picker> pickers) {
                Log.d("Size_pickers=", String.valueOf(Objects.requireNonNull(pickers).size()));
                for (int i = 0; i < Objects.requireNonNull(pickers).size(); i++) {
                    if (Objects.requireNonNull(pickers).get(i).chDesign.equals("...")) {
                        spinnerList.add(String.valueOf(pickers.get(i).chName));
                        Log.d("Добавлен канал ", String.valueOf(pickers.get(i).chName));
                    } else {
                        spinnerList.add(String.valueOf(pickers.get(i).chName) + " (" + Objects.requireNonNull(pickers).get(i).chDesign + ")");
                        Log.d("Добавлен канал ", String.valueOf(pickers.get(i).chDesign));
                    }
                    Log.d("Iteration=", String.valueOf(i));
                }
                final ArrayAdapter adapter = new ArrayAdapter<>(getApplication(), R.layout.spinner_item, R.id.spinnerCustom1, (Objects.requireNonNull(spinnerList.toArray())));
                spinner.setAdapter(adapter);
            }
        });
    }*/


    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    protected void onDestroy() {
        if(alertDialog != null && alertDialog.isShowing()){alertDialog.cancel();}
        super.onDestroy();
    }

    @Override
    public void onPause() {
        if(alertDialog != null && alertDialog.isShowing()){alertDialog.cancel();}
        super.onPause();
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public void onStop() {
        if(alertDialog != null && alertDialog.isShowing()){alertDialog.cancel();}
        super.onStop();

    }


    @Override
    protected void onUserLeaveHint() {
        //код для перехода к вашей главной активности
        super.onUserLeaveHint();
    }

}