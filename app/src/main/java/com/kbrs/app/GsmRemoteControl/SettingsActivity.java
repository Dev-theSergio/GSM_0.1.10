package com.kbrs.app.GsmRemoteControl;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;


public class SettingsActivity extends AppCompatActivity implements OnClickListener {

    RelativeLayout gsmNumberLayout;
    RelativeLayout masterNumberLayout;
    RelativeLayout groupNumberLayout_1;
    RelativeLayout groupNumberLayout_2;
    RelativeLayout groupNumberLayout_3;
    RelativeLayout passLayout;
    RelativeLayout timeLayout;
    RelativeLayout dateLayout;
    RelativeLayout factoryResetLayout;
    RelativeLayout backgroundMode;


    Switch silentMode;
    SharedPreferences sharedPreferencesMaster;
    static SharedPreferences sharedPreferencesGSM;
    SharedPreferences sharedPreferencesGroup_1;
    SharedPreferences sharedPreferencesGroup_2;
    SharedPreferences sharedPreferencesGroup_3;
    SharedPreferences sharedPreferencesPass;
    SharedPreferences sharedPreferencesSilentMode;

    Editor edGSM;
    Editor edMaster;
    Editor edPass;
    Editor edSilent;
    Editor edGroupDelete_1;
    Editor edGroupDelete_2;
    Editor edGroupDelete_3;

    TextView labelGroupMode;
    TextView sNumber;
    @SuppressLint("StaticFieldLeak")
    public static TextView gNumber;
    TextView hintSilentMode;
    TextView hintBackgroundMode;
    TextView textGroupNumber_1;
    TextView textGroupNumber_2;
    TextView textGroupNumber_3;
    TextView textInstallPass;
    TextView textTimeSet;
    TextView textDateSet;
    TextInputLayout editNumber;
    TextInputLayout editPass;
    String phoneNumber;
    String phoneNumberGroup;
    View view;
    View viewGroup;


    final String SAVED_SILENT_MODE = "saved_silent_mode";

    final String SAVED_TEXT_Master = "saved_text_master";
    final static public String SAVED_TEXT_GSM = "saved_text_gsm";
    final String SAVED_GROUP_NUMBER_1 = "saved_text_group1";
    final String SAVED_GROUP_NUMBER_2 = "saved_text_group2";
    final String SAVED_GROUP_NUMBER_3 = "saved_text_group3";
    final String SAVED_TEXT_PASS = "saved_text_pass";

    String CountryCode = "+7";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Настройки");

        hintSilentMode = findViewById(R.id.hintSilentMode);
        gsmNumberLayout = findViewById(R.id.LinearGsmNumber);
        masterNumberLayout = findViewById(R.id.LinearMasterNumber);
        groupNumberLayout_1 = findViewById(R.id.LinearGroupNumber_1);
        groupNumberLayout_2 = findViewById(R.id.LinearGroupNumber_2);
        groupNumberLayout_3 = findViewById(R.id.LinearGroupNumber_3);
        passLayout = findViewById(R.id.LinearPass);
        timeLayout = findViewById(R.id.LinearTimeSet);
        dateLayout = findViewById(R.id.LinearDateSet);
        factoryResetLayout = findViewById(R.id.LinearFactoryReset);
        silentMode = findViewById(R.id.switchSilentMode);
        hintSilentMode = findViewById(R.id.hintSilentMode);
        backgroundMode = findViewById(R.id.RelativeBackgroundMode);
        hintBackgroundMode = findViewById(R.id.hintBackgroundMode);

        labelGroupMode = findViewById(R.id.labelSilentMode);

        sNumber = findViewById(R.id.masterNumber);
        gNumber = findViewById(R.id.gsmNumber);

        textGroupNumber_1 = findViewById(R.id.textInstallGroupNumber_1);
        textGroupNumber_2 = findViewById(R.id.textInstallGroupNumber_2);
        textGroupNumber_3 = findViewById(R.id.textInstallGroupNumber_3);

        textInstallPass = findViewById(R.id.textInstallPass);

        //Typeface myCustomFont1 = Typeface.createFromAsset(getAssets(), "font/xxx.ttf");


        //sNumber.setTypeface(myCustomFont2);

        silentMode.setOnClickListener(this);
        backgroundMode.setOnClickListener(this);

        masterNumberLayout.setOnClickListener(this);
        gsmNumberLayout.setOnClickListener(this);
        groupNumberLayout_1.setOnClickListener(this);
        groupNumberLayout_2.setOnClickListener(this);
        groupNumberLayout_3.setOnClickListener(this);
        passLayout.setOnClickListener(this);
        timeLayout.setOnClickListener(this);
        dateLayout.setOnClickListener(this);
        factoryResetLayout.setOnClickListener(this);

        //justify(hintSilentMode);
        //justify(hintBackgroundMode);

        loadPreference();

        //////////////////////////////////////////////////////////////      BottomNavigationView        ///////////////////////////////////////////////////////////////

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_settings);
        BottomNavigationViewHelper.disableShiftMode(bottomNav);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                SharedPreferences sharedPreferencesGsm;
                String gsmNumber;
                sharedPreferencesGsm = Objects.requireNonNull(getApplicationContext()).getSharedPreferences(SAVED_TEXT_GSM, MODE_PRIVATE);
                gsmNumber = sharedPreferencesGsm.getString(SAVED_TEXT_GSM, "Номер не задан");
                switch (item.getItemId()) {
                    case R.id.Home:
                        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                        onPause();
                        break;

                    case R.id.Mode:
                        if (Objects.equals(gsmNumber, "Номер не задан")) {
                            Toast.makeText(getApplicationContext(), "Введите номер GSM устройства", Toast.LENGTH_LONG).show();
                        } else {
                            startActivity(new Intent(SettingsActivity.this, ModeActivity.class));
                            onPause();
                        }
                        break;

                    case R.id.Information:
                        if (Objects.equals(gsmNumber, "Номер не задан")) {
                            Toast.makeText(getApplicationContext(), "Введите номер GSM устройства", Toast.LENGTH_LONG).show();
                        } else {
                            startActivity(new Intent(SettingsActivity.this, InformationActivity.class));
                            onPause();
                        }
                        break;

                    case R.id.Event:
                        if (Objects.equals(gsmNumber, "Номер не задан")) {
                            Toast.makeText(getApplicationContext(), "Введите номер GSM устройства", Toast.LENGTH_LONG).show();
                        } else {
                            startActivity(new Intent(SettingsActivity.this, SmsEventActivity.class));
                            onPause();
                        }
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
    }


    @SuppressLint("InflateParams")
    public void onClick(final View v) {
        final String gsmNumber = sharedPreferencesGSM.getString(SAVED_TEXT_GSM, "Номер не задан");
        switch (v.getId()) {
            case R.id.switchSilentMode:
                if (Objects.equals(gsmNumber, "Номер не задан")) {
                    Toast.makeText(this, "Введите номер GSM устройства", Toast.LENGTH_LONG).show();
                } else {
                    edSilent = sharedPreferencesSilentMode.edit();
                    if (silentMode.isChecked()) {
                        silentMode.setChecked(true);
                        edSilent.putBoolean(SAVED_SILENT_MODE, true);
                        edSilent.apply();
                        SmsManager.getDefault().sendTextMessage(gsmNumber, null, "#008#1#", null, null);
                        Toast.makeText(SettingsActivity.this, "Тихий режим включен", Toast.LENGTH_SHORT).show();

                    } else {
                        silentMode.setChecked(false);
                        edSilent.putBoolean(SAVED_SILENT_MODE, false);
                        edSilent.apply();
                        SmsManager.getDefault().sendTextMessage(gsmNumber, null, "#008#0#", null, null);
                        Toast.makeText(SettingsActivity.this, "Тихий режим выключен", Toast.LENGTH_SHORT).show();
                    }
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                }
                break;

            case R.id.RelativeBackgroundMode:

                try {
                    Intent intent = new Intent();
                    String manufacturer = android.os.Build.MANUFACTURER;
                    if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                        intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                    } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                        intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
                    } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                        intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                    } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                        intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
                    } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                        intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                    }

                    List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    if (list.size() > 0) {
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Log.e("exc", String.valueOf(e));
                    Toast.makeText(this, "Произодитель Вашего телефона предоставляет доступ к фоновому режиму работы приложения.", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.LinearMasterNumber:
                final AlertDialog.Builder alertDialogMaster = new AlertDialog.Builder(SettingsActivity.this);
                final LayoutInflater inflaterMaster = this.getLayoutInflater();
                // inflate the View
                view = inflaterMaster.inflate(R.layout.layout_dialog_number, null);
                editNumber = view.findViewById(R.id.textNumber);
                if (!sNumber.getText().toString().equals("Номер не задан")) {
                    Objects.requireNonNull(editNumber.getEditText()).setText(sNumber.getText().subSequence(2, sNumber.length()));
                }

                if (Objects.equals(gsmNumber, "Номер не задан")) {
                    Toast.makeText(this, "Введите номер GSM устройства", Toast.LENGTH_LONG).show();
                } else {
                    alertDialogMaster.setTitle("Введите мастер-номер");
                    alertDialogMaster.setView(view);
                    final AlertDialog dialog = alertDialogMaster.create();
                    dialog.show();

                    Button save = view.findViewById(R.id.buttonDialogSaveNumber);
                    Button send = view.findViewById(R.id.buttonDialogSendNumber);
                    Button delete = view.findViewById(R.id.buttonDialogAllCancel);
                    Button cancel = view.findViewById(R.id.buttonDialogCancel);

                    save.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            phoneNumber = Objects.requireNonNull(editNumber.getEditText()).getText().toString().trim();
                            edMaster = sharedPreferencesMaster.edit();
                            if (phoneNumber.length() != 10) {
                                editNumber.setError("Введите 10-значный номер");
                            } else {
                                editNumber.setError(null);
                                if ((CountryCode + phoneNumber).equals(sNumber.getText().toString())) {
                                    Toast.makeText(SettingsActivity.this, "Введенный номер уже установлен", Toast.LENGTH_SHORT).show();
                                } else {
                                    edMaster.putString(SAVED_TEXT_Master, CountryCode + editNumber.getEditText().getText().toString().trim());
                                    edMaster.apply();
                                    //SmsManager.getDefault().sendTextMessage(gsmNumber, null, "#00#", null, null);
                                    Toast.makeText(SettingsActivity.this, "Мастер-номер сохранен", Toast.LENGTH_SHORT).show();
                                    loadPreference();
                                    dialog.dismiss();
                                }
                            }
                        }
                    });

                    send.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //editMasterNumber = view.findViewById(R.id.textMasterNumber);
                            phoneNumber = Objects.requireNonNull(editNumber.getEditText()).getText().toString().trim();
                            edMaster = sharedPreferencesMaster.edit();
                            if (phoneNumber.length() != 10) {
                                editNumber.setError("Введите 10-значный номер");
                            } else {
                                editNumber.setError(null);
                                if ((CountryCode + phoneNumber).equals(sNumber.getText().toString())) {
                                    Toast.makeText(SettingsActivity.this, "Введенный номер уже установлен", Toast.LENGTH_SHORT).show();
                                } else {
                                    edMaster.putString(SAVED_TEXT_Master, CountryCode + editNumber.getEditText().getText().toString().trim());
                                    edMaster.apply();
                                    SmsManager.getDefault().sendTextMessage(gsmNumber, null, "#00#", null, null);
                                    Toast.makeText(SettingsActivity.this, "Мастер-номер сохранен и отправлен", Toast.LENGTH_SHORT).show();
                                    loadPreference();
                                    dialog.dismiss();
                                }
                            }
                        }
                    });

                    delete.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            Editor edMaster = sharedPreferencesMaster.edit();
                            if (sNumber.getText().toString().equals("Номер не задан")) {
                                Toast.makeText(SettingsActivity.this, "Действие не требуется", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            } else {
                                edMaster.putString(SAVED_TEXT_Master, "Номер не задан");
                                edMaster.apply();
                                SmsManager.getDefault().sendTextMessage(gsmNumber, null,
                                        "#00#" + sharedPreferencesPass.getString(SAVED_TEXT_PASS, "КБРС") + "#", null, null);
                                Toast.makeText(SettingsActivity.this, "Мастер-номер удален", Toast.LENGTH_SHORT).show();
                                loadPreference();
                                dialog.dismiss();
                            }
                        }
                    });

                    cancel.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                }
                break;

            case R.id.LinearGsmNumber:
                AlertDialog.Builder alertDialogGsm = new AlertDialog.Builder(SettingsActivity.this);
                LayoutInflater inflaterGsm = this.getLayoutInflater();
                // inflate the View
                view = inflaterGsm.inflate(R.layout.layout_dialog_number, null);
                editNumber = view.findViewById(R.id.textNumber);
                if (!gNumber.getText().toString().equals("Номер не задан")) {
                    Objects.requireNonNull(editNumber.getEditText()).setText(gNumber.getText().subSequence(2, gNumber.length()));
                }
                alertDialogGsm.setTitle("Введите GSM номер");
                alertDialogGsm.setView(view);
                final AlertDialog dialog = alertDialogGsm.create();
                dialog.show();

                Button save = view.findViewById(R.id.buttonDialogSaveNumber);
                Button send = view.findViewById(R.id.buttonDialogSendNumber);
                Button delete = view.findViewById(R.id.buttonDialogAllCancel);
                Button cancel = view.findViewById(R.id.buttonDialogCancel);

                save.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        phoneNumber = Objects.requireNonNull(editNumber.getEditText()).getText().toString().trim();
                        edGSM = sharedPreferencesGSM.edit();
                        if (phoneNumber.length() != 10) {
                            editNumber.setError("Введите 10-значный номер");
                        } else {
                            editNumber.setError(null);
                            if ((CountryCode + phoneNumber).equals(gNumber.getText().toString())) {
                                Toast.makeText(SettingsActivity.this, "Введенный номер уже установлен", Toast.LENGTH_SHORT).show();
                            } else {
                                edGSM.putString(SAVED_TEXT_GSM, CountryCode + editNumber.getEditText().getText().toString().trim());
                                edGSM.apply();
                                Toast.makeText(SettingsActivity.this, "GSM номер сохранен", Toast.LENGTH_SHORT).show();
                                loadPreference();
                                dialog.dismiss();

                            }
                        }
                    }
                });

                send.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        phoneNumber = Objects.requireNonNull(editNumber.getEditText()).getText().toString().trim();
                        edGSM = sharedPreferencesGSM.edit();
                        if (phoneNumber.length() != 10) {
                            editNumber.setError("Введите 10-значный номер");
                        } else {
                            editNumber.setError(null);
                            if ((CountryCode + phoneNumber).equals(gNumber.getText().toString())) {
                                Toast.makeText(SettingsActivity.this, "Введенный номер уже установлен", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!gNumber.getText().toString().equals("Номер не задан")) {
                                    edGSM.putString(SAVED_TEXT_GSM, CountryCode + editNumber.getEditText().getText().toString().trim());
                                    edGSM.apply();
                                    SmsManager.getDefault().sendTextMessage(gsmNumber, null, "#004#", null, null);
                                    Toast.makeText(SettingsActivity.this, "GSM номер отправлен", Toast.LENGTH_SHORT).show();
                                    loadPreference();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(SettingsActivity.this, "Введите номер GSM устройства", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });

                delete.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Editor edGSM = sharedPreferencesGSM.edit();
                        if (gNumber.getText().toString().equals("Номер не задан")) {
                            Toast.makeText(SettingsActivity.this, "Действие не требуется", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        } else {
                            edGSM.putString(SAVED_TEXT_GSM, "Номер не задан");
                            edGSM.apply();
                            Toast.makeText(SettingsActivity.this, "GSM номер удален", Toast.LENGTH_SHORT).show();
                            loadPreference();
                            dialog.dismiss();
                        }
                    }
                });

                cancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                break;

            /*case R.id.textSettingsGroupDeleteAll:
                if (Objects.requireNonNull(sharedPreferencesGSM.getString(SAVED_TEXT_GSM, "Номер не задан")).equals("Номер не задан")) {
                    Toast.makeText(this, "Введите номер GSM устройства", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder alertDialogGroupDeleteAll = new AlertDialog.Builder(SettingsActivity.this);
                    alertDialogGroupDeleteAll.setTitle("Удаление номеров участников группы")
                            .setMessage("Вы уверены?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    edGroupDelete_1 = sharedPreferencesGroup_1.edit();
                                    edGroupDelete_2 = sharedPreferencesGroup_2.edit();
                                    edGroupDelete_3 = sharedPreferencesGroup_3.edit();
                                    if (Objects.requireNonNull(sharedPreferencesGroup_1.getString(SAVED_GROUP_NUMBER_1, "Номер не задан")).equals("Номер не задан") &&
                                            Objects.requireNonNull(sharedPreferencesGroup_2.getString(SAVED_GROUP_NUMBER_2, "Номер не задан")).equals("Номер не задан") &&
                                            Objects.requireNonNull(sharedPreferencesGroup_3.getString(SAVED_GROUP_NUMBER_3, "Номер не задан")).equals("Номер не задан")) {
                                        Toast.makeText(SettingsActivity.this, "Не найдено сохраненных номеров", Toast.LENGTH_SHORT).show();
                                    } else {
                                        edGroupDelete_1.putString(SAVED_GROUP_NUMBER_1, "Номер не задан");
                                        edGroupDelete_1.apply();
                                        edGroupDelete_2.putString(SAVED_GROUP_NUMBER_2, "Номер не задан");
                                        edGroupDelete_2.apply();
                                        edGroupDelete_3.putString(SAVED_GROUP_NUMBER_3, "Номер не задан");
                                        edGroupDelete_3.apply();

                                        SmsManager.getDefault().sendTextMessage(gsmNumber, null, "#003#", null, null);
                                        Toast.makeText(SettingsActivity.this, "Все номера участников группы удалены", Toast.LENGTH_SHORT).show();
                                        loadPreference();

                                        dialog.dismiss();

                                    }
                                }

                            })
                            .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .create()
                            .show();
                }
                break;*/

            case R.id.LinearGroupNumber_1:
                if (Objects.requireNonNull(sharedPreferencesGSM.getString(SAVED_TEXT_GSM, "Номер не задан")).equals("Номер не задан")) {
                    Toast.makeText(this, "Введите номер GSM устройства", Toast.LENGTH_LONG).show();
                } else {
                    setGroupNumber(textGroupNumber_1, SAVED_GROUP_NUMBER_1, sharedPreferencesGroup_1);
                    //smsManager.sendTextMessage(("+" + sharedPreferencesGSM.getString(SAVED_TEXT_GSM, "")), null, "#001#" + "#", null, null);
                }
                break;

            case R.id.LinearGroupNumber_2:
                if (Objects.requireNonNull(sharedPreferencesGSM.getString(SAVED_TEXT_GSM, "Номер не задан")).equals("Номер не задан")) {
                    Toast.makeText(this, "Введите номер GSM устройства", Toast.LENGTH_LONG).show();
                } else {
                    setGroupNumber(textGroupNumber_2, SAVED_GROUP_NUMBER_2, sharedPreferencesGroup_2);
                    //smsManager.sendTextMessage(("+" + sharedPreferencesGSM.getString(SAVED_TEXT_GSM, "")), null, "#001#" + "#", null, null);
                }
                break;

            case R.id.LinearGroupNumber_3:
                if (Objects.requireNonNull(sharedPreferencesGSM.getString(SAVED_TEXT_GSM, "Номер не задан")).equals("Номер не задан")) {
                    Toast.makeText(this, "Введите номер GSM устройства", Toast.LENGTH_LONG).show();
                } else {
                    setGroupNumber(textGroupNumber_3, SAVED_GROUP_NUMBER_3, sharedPreferencesGroup_3);
                    //smsManager.sendTextMessage(("+" + sharedPreferencesGSM.getString(SAVED_TEXT_GSM, "")), null, "#001#" + "#", null, null);
                }
                break;


            case R.id.LinearPass:

                if (Objects.requireNonNull(sharedPreferencesGSM.getString(SAVED_TEXT_GSM, "Номер не задан")).equals("Номер не задан")) {
                    Toast.makeText(this, "Введите номер GSM устройства", Toast.LENGTH_LONG).show();
                } else {

                    AlertDialog.Builder alertDialogPass = new AlertDialog.Builder(SettingsActivity.this);
                    LayoutInflater inflaterPass = this.getLayoutInflater();
                    // inflate the View
                    view = inflaterPass.inflate(R.layout.layout_dialog_pass, null);
                    editPass = view.findViewById(R.id.textPass);
                    if (!textInstallPass.getText().toString().equals("КБРС") | textInstallPass.getText().toString().isEmpty()) {
                        Objects.requireNonNull(editPass.getEditText()).setText(textInstallPass.getText().toString());
                    }
                    alertDialogPass.setTitle("Введите пароль");
                    alertDialogPass.setView(view)
                            .setPositiveButton("Отправить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //editMasterNumber = view.findViewById(R.id.textMasterNumber);
                                    phoneNumber = Objects.requireNonNull(editPass.getEditText()).getText().toString().trim();
                                    edPass = sharedPreferencesPass.edit();
                                    if (phoneNumber.length() != 4) {
                                        editPass.setError("Введите 4-значный пароль.");
                                        Toast.makeText(SettingsActivity.this, "SMS не отправлено! Пароль должен состоять из 4-х символов", Toast.LENGTH_SHORT).show();
                                    } else {
                                        editPass.setError(null);
                                        if ((phoneNumber).equals(textInstallPass.getText().toString())) {
                                            Toast.makeText(SettingsActivity.this, "Введенный пароль уже установлен", Toast.LENGTH_SHORT).show();
                                        } else {
                                            edPass.putString(SAVED_TEXT_PASS, editPass.getEditText().getText().toString().trim());
                                            edPass.apply();
                                            SmsManager.getDefault().sendTextMessage(gsmNumber, null, "#30#" + phoneNumber + "#", null, null);
                                            Toast.makeText(SettingsActivity.this, "Пароль сохранен", Toast.LENGTH_SHORT).show();
                                            loadPreference();
                                            dialog.dismiss();
                                        }
                                    }
                                }

                            })
                            .setNegativeButton("Сброс", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Editor edGSM = sharedPreferencesPass.edit();

                                    if (textInstallPass.getText().toString().equals("КБРС")) {
                                        Toast.makeText(SettingsActivity.this, "Действие не требуется", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    } else {
                                        edGSM.putString(SAVED_TEXT_PASS, "КБРС");
                                        edGSM.apply();
                                        SmsManager.getDefault().sendTextMessage(gsmNumber, null, "#30#КБРС#", null, null);
                                        Toast.makeText(SettingsActivity.this, "Пароль сброшен", Toast.LENGTH_SHORT).show();
                                        loadPreference();
                                        dialog.dismiss();
                                    }


                                }
                            })
                            .setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .create()
                            .show();
                }
                break;

            case R.id.LinearTimeSet:
                if (Objects.requireNonNull(sharedPreferencesGSM.getString(SAVED_TEXT_GSM, "Номер не задан")).equals("Номер не задан")) {
                    Toast.makeText(this, "Введите номер GSM устройства", Toast.LENGTH_LONG).show();
                } else {
                    TimePickerDialog timePicker = new TimePickerDialog(SettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String timeSet;
                            if (minute < 10) {
                                //textTimeSet.setText(hourOfDay + ":" + "0" + minute);
                                timeSet = hourOfDay + ":" + "0" + minute + ":00";
                                SmsManager.getDefault().sendTextMessage(gsmNumber, null, "#20#" + timeSet + "#", null, null);
                                Toast.makeText(SettingsActivity.this, "Время установлено", Toast.LENGTH_SHORT).show();
                            } else {
                                //textTimeSet.setText(hourOfDay + ":" + minute);
                                timeSet = hourOfDay + ":" + minute + ":00";
                                SmsManager.getDefault().sendTextMessage(gsmNumber, null, "#20#" + timeSet + "#", null, null);
                                Toast.makeText(SettingsActivity.this, "Время установлено", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 0, 0, true);
                    timePicker.show();
                }
                break;

            case R.id.LinearDateSet:
                if (Objects.requireNonNull(sharedPreferencesGSM.getString(SAVED_TEXT_GSM, "Номер не задан")).equals("Номер не задан")) {
                    Toast.makeText(this, "Введите номер GSM устройства", Toast.LENGTH_LONG).show();
                } else {
                    Calendar mCurrentDate = Calendar.getInstance();
                    int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);
                    int mMonth = mCurrentDate.get(Calendar.MONTH);
                    int mYear = mCurrentDate.get(Calendar.YEAR);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(SettingsActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            month += 1;
                            String dataSet;
                            if (dayOfMonth < 10) {
                                if (month < 10) {
                                    //textDateSet.setText("" + "0" + dayOfMonth + "." + "0" + month + "." + year);
                                    dataSet = "" + "0" + dayOfMonth + "." + "0" + month + "." + year;
                                    SmsManager.getDefault().sendTextMessage(gsmNumber, null, "#21#" + dataSet + "#", null, null);
                                    Toast.makeText(SettingsActivity.this, "Дата установлена", Toast.LENGTH_SHORT).show();
                                } else {
                                    //textDateSet.setText("" + "0" + dayOfMonth + "." + month + "." + year);
                                    dataSet = "" + "0" + dayOfMonth + "." + month + "." + year;
                                    SmsManager.getDefault().sendTextMessage(gsmNumber, null, "#21#" + dataSet + "#", null, null);
                                    Toast.makeText(SettingsActivity.this, "Дата установлена", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                if (month < 10) {
                                    //textDateSet.setText("" + dayOfMonth + "." + "0" + month + "." + year);
                                    dataSet = "" + dayOfMonth + "." + "0" + month + "." + year;
                                    SmsManager.getDefault().sendTextMessage(gsmNumber, null, "#21#" + dataSet + "#", null, null);
                                    Toast.makeText(SettingsActivity.this, "Дата установлена", Toast.LENGTH_SHORT).show();
                                } else {
                                    //textDateSet.setText("" + dayOfMonth + "." + month + "." + year);
                                    dataSet = "" + dayOfMonth + "." + month + "." + year;
                                    SmsManager.getDefault().sendTextMessage(gsmNumber, null, "#21#" + dataSet + "#", null, null);
                                    Toast.makeText(SettingsActivity.this, "Дата установлена", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
                break;

            case R.id.LinearFactoryReset:
                if (Objects.requireNonNull(sharedPreferencesGSM.getString(SAVED_TEXT_GSM, "Номер не задан")).equals("Номер не задан")) {
                    Toast.makeText(this, "Введите номер GSM устройства", Toast.LENGTH_LONG).show();
                } else {
                    new AlertDialog.Builder(SettingsActivity.this)
                            .setTitle("Сброс до заводских настроек")
                            .setMessage("Потребуется перезапуск приложения. Вы уверены?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SmsManager.getDefault().sendTextMessage(gsmNumber, null, "#12345#", null, null);
                                    Toast.makeText(SettingsActivity.this, "Сброс до заводских настроек", Toast.LENGTH_SHORT).show();

                                    /////////////////////////////////////////////////   удаление всех данных приложения и выход   /////////////////////////////////////////////
                                    ((ActivityManager) Objects.requireNonNull(getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE))).clearApplicationUserData();

                                    /*try {
                                        // clearing app data
                                        String packageName = getApplicationContext().getPackageName();
                                        Runtime runtime = Runtime.getRuntime();
                                        runtime.exec("pm clear "+packageName);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }*/

                                }
                            })
                            .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .create()
                            .show();
                }
                break;
            /*case R.id.switchGroupMode:
                Editor edGroupMode = sharedPreferencesGroupMode.edit();
                if(groupMode.isChecked()){
                    edGroupMode.putBoolean(SAVED_GROUP_MODE, false);
                    edGroupMode.apply();
                    groupMode.setChecked(false);
                    Toast.makeText(SettingsActivity.this, "Режим участника группы выключен", Toast.LENGTH_SHORT).show();
                    loadPreference();
                }else{
                    edGroupMode.putBoolean(SAVED_GROUP_MODE, true);
                    edGroupMode.apply();
                    groupMode.setChecked(true);
                    Toast.makeText(SettingsActivity.this, "Режим участника группы включен", Toast.LENGTH_SHORT).show();
                    loadPreference();
                }*/

            default:
                break;
        }

    }

    void loadPreference() {

        sharedPreferencesMaster = getSharedPreferences(SAVED_TEXT_Master, MODE_PRIVATE);
        sNumber.setText(sharedPreferencesMaster.getString(SAVED_TEXT_Master, "Номер не задан"));

        sharedPreferencesGSM = getSharedPreferences(SAVED_TEXT_GSM, MODE_PRIVATE);
        gNumber.setText(sharedPreferencesGSM.getString(SAVED_TEXT_GSM, "Номер не задан"));

        sharedPreferencesGroup_1 = getSharedPreferences(SAVED_GROUP_NUMBER_1, MODE_PRIVATE);
        textGroupNumber_1.setText(sharedPreferencesGroup_1.getString(SAVED_GROUP_NUMBER_1, "Номер не задан"));

        sharedPreferencesGroup_2 = getSharedPreferences(SAVED_GROUP_NUMBER_2, MODE_PRIVATE);
        textGroupNumber_2.setText(sharedPreferencesGroup_2.getString(SAVED_GROUP_NUMBER_2, "Номер не задан"));

        sharedPreferencesGroup_3 = getSharedPreferences(SAVED_GROUP_NUMBER_3, MODE_PRIVATE);
        textGroupNumber_3.setText(sharedPreferencesGroup_3.getString(SAVED_GROUP_NUMBER_3, "Номер не задан"));

        sharedPreferencesPass = getSharedPreferences(SAVED_TEXT_PASS, MODE_PRIVATE);
        textInstallPass.setText(sharedPreferencesPass.getString(SAVED_TEXT_PASS, "КБРС"));

        sharedPreferencesSilentMode = getSharedPreferences(SAVED_SILENT_MODE, MODE_PRIVATE);
        silentMode.setChecked(sharedPreferencesSilentMode.getBoolean(SAVED_SILENT_MODE, false));

    }

    @SuppressLint("InflateParams")
    void setGroupNumber(final TextView textInstallGroupNumber, final String string,
                        final SharedPreferences sharedPreferencesCustom) {

        AlertDialog.Builder alertDialogGroup = new AlertDialog.Builder(this);
        LayoutInflater inflaterGroup = this.getLayoutInflater();
        // inflate the View
        viewGroup = inflaterGroup.inflate(R.layout.layout_dialog_group_number, null);
        editNumber = viewGroup.findViewById(R.id.textNumberGroup);

        if (!textInstallGroupNumber.getText().toString().equals("Номер не задан")) {
            Objects.requireNonNull(editNumber.getEditText()).setText(textInstallGroupNumber.getText().subSequence(2, textInstallGroupNumber.length()));
        }
        alertDialogGroup.setTitle("Введите номер");
        alertDialogGroup.setView(viewGroup);
        final AlertDialog dialogGroup = alertDialogGroup.create();
        dialogGroup.show();

        Button sendGroup = viewGroup.findViewById(R.id.buttonDialogSendNumberGroup);
        Button deleteGroup = viewGroup.findViewById(R.id.buttonDialogDeleteNumberGroup);
        Button deleteAll = viewGroup.findViewById(R.id.buttonDialogDeleteAll);
        Button cancelGroup = viewGroup.findViewById(R.id.buttonDialogCancelGroup);

        sendGroup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumberGroup = Objects.requireNonNull(editNumber.getEditText()).getText().toString().trim();
                Editor edGroup = sharedPreferencesCustom.edit();
                if (phoneNumberGroup.length() != 10) {
                    editNumber.setError("Введите 10-значный номер");
                } else if (Objects.requireNonNull(sharedPreferencesCustom.getString(string, "Номер не задан")).equals(sharedPreferencesGSM.getString(SAVED_TEXT_GSM, "Номер не задан")) |
                        Objects.requireNonNull(sharedPreferencesCustom.getString(string, "Номер не задан")).equals(sharedPreferencesMaster.getString(SAVED_TEXT_Master, "Номер не задан"))) {
                    Toast.makeText(SettingsActivity.this, "Введенный номер уже используется", Toast.LENGTH_SHORT).show();
                } else {
                    editNumber.setError(null);
                    if ((CountryCode + phoneNumberGroup).equals(textInstallGroupNumber.getText().toString())) {
                        Toast.makeText(SettingsActivity.this, "Введенный номер уже установлен", Toast.LENGTH_SHORT).show();
                    } else {
                        edGroup.putString(string, CountryCode + phoneNumberGroup);
                        edGroup.apply();
                        SmsManager.getDefault().sendTextMessage(gNumber.getText().toString(), null,
                                "#001#" + CountryCode + phoneNumberGroup + "#", null, null);
                        Toast.makeText(SettingsActivity.this, "Номер участника сохранен и отправлен", Toast.LENGTH_SHORT).show();
                        loadPreference();
                        dialogGroup.dismiss();
                    }
                }

            }
        });

        deleteGroup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(sharedPreferencesCustom.getString(string, "Номер не задан")).equals("Номер не задан")) {
                    Toast.makeText(SettingsActivity.this, "Действие не требуется", Toast.LENGTH_SHORT).show();
                } else {
                    Editor edGroup = sharedPreferencesCustom.edit();
                    edGroup.putString(string, "Номер не задан");
                    edGroup.apply();
                    SmsManager.getDefault().sendTextMessage(gNumber.getText().toString(), null,
                            "#002#" + CountryCode + phoneNumberGroup + "#", null, null);
                    Toast.makeText(SettingsActivity.this, "Номер участника удален", Toast.LENGTH_SHORT).show();
                    loadPreference();
                    dialogGroup.dismiss();
                }
            }
        });

        deleteAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Objects.requireNonNull(sharedPreferencesGSM.getString(SAVED_TEXT_GSM, "Номер не задан")).equals("Номер не задан")) {
                    Toast.makeText(getApplicationContext(), "Введите номер GSM устройства", Toast.LENGTH_LONG).show();
                } else {
                    edGroupDelete_1 = sharedPreferencesGroup_1.edit();
                    edGroupDelete_2 = sharedPreferencesGroup_2.edit();
                    edGroupDelete_3 = sharedPreferencesGroup_3.edit();
                    if (Objects.requireNonNull(sharedPreferencesGroup_1.getString(SAVED_GROUP_NUMBER_1, "Номер не задан")).equals("Номер не задан") &&
                            Objects.requireNonNull(sharedPreferencesGroup_2.getString(SAVED_GROUP_NUMBER_2, "Номер не задан")).equals("Номер не задан") &&
                            Objects.requireNonNull(sharedPreferencesGroup_3.getString(SAVED_GROUP_NUMBER_3, "Номер не задан")).equals("Номер не задан")) {
                        Toast.makeText(getApplicationContext(), "Не найдено сохраненных номеров", Toast.LENGTH_SHORT).show();
                    } else {
                        edGroupDelete_1.putString(SAVED_GROUP_NUMBER_1, "Номер не задан");
                        edGroupDelete_1.apply();
                        edGroupDelete_2.putString(SAVED_GROUP_NUMBER_2, "Номер не задан");
                        edGroupDelete_2.apply();
                        edGroupDelete_3.putString(SAVED_GROUP_NUMBER_3, "Номер не задан");
                        edGroupDelete_3.apply();

                        SmsManager.getDefault().sendTextMessage(gNumber.getText().toString(), null, "#003#", null, null);
                        Toast.makeText(getApplicationContext(), "Номера всех участников группы удалены", Toast.LENGTH_SHORT).show();
                        loadPreference();
                        dialogGroup.dismiss();
                    }
                }
            }
        });

        cancelGroup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogGroup.cancel();
            }
        });



                /*.setPositiveButton("Отправить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogGroup, int which) {
                //editMasterNumber = view.findViewById(R.id.textMasterNumber);
                phoneNumberGroup = Objects.requireNonNull(editNumber.getEditText()).getText().toString().trim();
                Editor edGroup = sharedPreferencesCustom.edit();
                if (phoneNumberGroup.length() != 10) {
                    editNumber.setError("Введите 10-значный номер");
                } else {
                    editNumber.setError(null);
                    if ((CountryCode + phoneNumberGroup).equals(textInstallGroupNumber.getText().toString())) {
                        Toast.makeText(SettingsActivity.this, "Введенный номер уже установлен", Toast.LENGTH_SHORT).show();
                    } else {
                        edGroup.putString(string, CountryCode + phoneNumberGroup);
                        edGroup.apply();
                        SmsManager.getDefault().sendTextMessage(gNumber.getText().toString(), null,
                                "#001#" + CountryCode + phoneNumberGroup + "#", null, null);
                        Toast.makeText(SettingsActivity.this, "Номер участника сохранен", Toast.LENGTH_SHORT).show();
                        loadPreference();
                        dialogGroup.dismiss();
                    }
                }
            }

        })
                .setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Objects.requireNonNull(sharedPreferencesCustom.getString(string, "Номер не задан")).equals("Номер не задан")) {
                            Toast.makeText(SettingsActivity.this, "Действие не требуется", Toast.LENGTH_SHORT).show();
                        } else {
                            Editor edGroup = sharedPreferencesCustom.edit();
                            edGroup.putString(string, "Номер не задан");
                            edGroup.apply();
                            SmsManager.getDefault().sendTextMessage(gNumber.getText().toString(), null,
                                    "#002#" + CountryCode + phoneNumberGroup + "#", null, null);
                            Toast.makeText(SettingsActivity.this, "Номер участника удален", Toast.LENGTH_SHORT).show();
                            loadPreference();
                            dialog.dismiss();
                        }
                    }
                })
                .setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })*/


    }


    private void justify(final TextView textView) {

        final AtomicBoolean isJustify = new AtomicBoolean(false);

        final String textString = textView.getText().toString();

        final TextPaint textPaint = textView.getPaint();

        final SpannableStringBuilder builder = new SpannableStringBuilder();

        textView.post(new Runnable() {
            @Override
            public void run() {

                if (!isJustify.get()) {

                    final int lineCount = textView.getLineCount();
                    final int textViewWidth = textView.getWidth();

                    for (int i = 0; i < lineCount; i++) {

                        int lineStart = textView.getLayout().getLineStart(i);
                        int lineEnd = textView.getLayout().getLineEnd(i);

                        String lineString = textString.substring(lineStart, lineEnd);

                        if (i == lineCount - 1) {
                            builder.append(new SpannableString(lineString));
                            break;
                        }

                        String trimSpaceText = lineString.trim();
                        String removeSpaceText = lineString.replaceAll(" ", "");

                        float removeSpaceWidth = textPaint.measureText(removeSpaceText);
                        float spaceCount = trimSpaceText.length() - removeSpaceText.length();

                        float eachSpaceWidth = (textViewWidth - removeSpaceWidth) / spaceCount;

                        SpannableString spannableString = new SpannableString(lineString);
                        for (int j = 0; j < trimSpaceText.length(); j++) {
                            char c = trimSpaceText.charAt(j);
                            if (c == ' ') {
                                Drawable drawable = new ColorDrawable(0x00ffffff);
                                drawable.setBounds(0, 0, (int) eachSpaceWidth, 0);
                                ImageSpan span = new ImageSpan(drawable);
                                spannableString.setSpan(span, j, j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }

                        builder.append(spannableString);
                    }

                    textView.setText(builder);
                    isJustify.set(true);
                }
            }
        });
    }


    @Override
    protected void onUserLeaveHint() {
        //код для перехода к вашей главной активности
        super.onUserLeaveHint();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }


}
