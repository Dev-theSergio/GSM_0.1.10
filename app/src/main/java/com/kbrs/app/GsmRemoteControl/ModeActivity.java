package com.kbrs.app.GsmRemoteControl;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.kbrs.app.GsmRemoteControl.SettingsActivity.SAVED_TEXT_GSM;


public class ModeActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter; // было private

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager; // было private

    //PlaceholderFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        mViewPager = findViewById(R.id.container);             //(ViewPager)
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }


    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    //   getMenuInflater().inflate(R.menu.menu_mode, menu);
    //  return true;
    // }

    //@Override
    //public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    //int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    //if (id == R.id.action_settings) {
    //   return true;
    //}

    //return super.onOptionsItemSelected(item);
    //}

    /**
     * A placeholder fragment containing a simple view.
     */


    public static class PlaceholderFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        View view;

        TextView textDelayTime;
        Button buttonDelaySend;
        RadioButton radioDelayButtonOn;
        RadioButton radioDelayButtonOff;
        Spinner spinnerDelay;

        RadioButton radioCycButtonOn;
        RadioButton radioCycButtonOff;
        Button buttonCycSend;
        ScrollableNumberPicker numberPickerCycReplay;
        TextView textCycWorkingTime;
        TextView textCycIntervalTime;
        Spinner spinnerCyc;


        TextView textScheduleTimeOn;
        TextView textScheduleTimeOff;
        Button buttonSchedule;
        Button buttonScheduleMode;
        Spinner spinnerSchedule;

        Switch ScheduleMode;
        SharedPreferences.Editor edScheduleMode;
        SharedPreferences sharedPreferencesScheduleMode;
        TextView textScheduleWeekDay;
        String[] WeekDays;
        String[] WeekDaysNumber;
        String sWeekDaysNumber = "";
        boolean[] checkedItems;
        final ArrayList<Integer> mUserItems = new ArrayList<>();

        Button buttonVoltage;
        ScrollableNumberPicker numberPickerVoltageTime;
        ScrollableNumberPicker numberPickerVoltageHighLevel;
        ScrollableNumberPicker numberPickerVoltageLowLevel;
        ScrollableNumberPicker numberPickerVoltagePower;
        TextView textVoltageHighLevel;
        TextView textVoltageLowLevel;
        TextView textVoltagePower;
        Spinner spinnerVoltage;
        String selected;
        Spinner spinnerVoltageChannel;

        Switch TempMode;
        SharedPreferences.Editor edTempMode;
        SharedPreferences sharedPreferencesTempMode;
        ScrollableNumberPicker numberPickerTempHigh;
        ScrollableNumberPicker numberPickerTempLow;
        Button buttonTempSend;
        Button buttonTempMode;
        Spinner spinnerTemp;

        SharedPreferences sharedPreferencesGsm;
        String gsmNumber;

        final SmsManager smsManagerDelay = SmsManager.getDefault();

        final String SAVED_SCHEDULE_MODE = "saved_schedule_mode";
        final String SAVED_TEMP_MODE = "saved_temp_mode";

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            sharedPreferencesGsm = Objects.requireNonNull(this.getActivity()).getSharedPreferences(SAVED_TEXT_GSM, MODE_PRIVATE);
            gsmNumber = sharedPreferencesGsm.getString(SAVED_TEXT_GSM, "Номер не задан");

            //////////////////////////////////////////////////////////////      BottomNavigationView        ///////////////////////////////////////////////////////////////

            BottomNavigationView bottomNav = Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_navigation_mode);
            BottomNavigationViewHelper.disableShiftMode(bottomNav);
            Menu menu = bottomNav.getMenu();
            MenuItem menuItem = menu.getItem(2);
            menuItem.setChecked(true);

            bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.Home:
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            Objects.requireNonNull(getActivity()).overridePendingTransition(0, 0);
                            break;

                        case R.id.Settings:
                            startActivity(new Intent(getActivity(), SettingsActivity.class));
                            Objects.requireNonNull(getActivity()).overridePendingTransition(0, 0);
                            break;

                        case R.id.Information:
                            startActivity(new Intent(getActivity(), InformationActivity.class));
                            Objects.requireNonNull(getActivity()).overridePendingTransition(0, 0);
                            break;

                        case R.id.Event:
                            startActivity(new Intent(getActivity(), SmsEventActivity.class));
                            Objects.requireNonNull(getActivity()).overridePendingTransition(0, 0);
                            break;

                        default:
                            break;
                    }

                    return false;
                }
            });
        }

        public PlaceholderFragment() {

        }


        ///////////////////////////////////// обработчик нажатий в фрагментах  \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

        public void onClick(View v) {

            String inputDelaySwitcher = null;
            String inputDelayChannel;
            String inputDelayTime;

            String inputScheduleChannel;
            String inputScheduleDay;
            String[] inputScheduleTimeOn;
            String[] inputScheduleTimeOff;

            String inputCycChannel;
            String inputCycWorkingTime;
            String inputCycInterval;
            int inputCycReplay;
            String inputCycSwitcher = null;

            String inputVoltageChannel;
            int inputVoltageTime;
            int inputVoltageHighLevel;
            int inputVoltageLowLevel;
            int inputVoltagePower;

            String inputTempChannel;
            int inputTempHighLevel;
            int inputTempLowLevel;


            if (gsmNumber.equals("Номер не задан")) {
                customToastLong(getContext(), "Номер GSM устройства не задан,\nперейдите в \"Настройки\"");
            } else {

                switch (v.getId()) {

/////////////////////////////////////////////// Delay Mode  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    case R.id.buttonDelaySend:

                        inputDelayTime = textDelayTime.getText().toString();
                        inputDelayChannel = spinnerDelay.getSelectedItem().toString();

                        if (radioDelayButtonOn.isChecked()) {
                            inputDelaySwitcher = "1";
                        } else if (radioDelayButtonOff.isChecked()) {
                            inputDelaySwitcher = "0";
                        }

                        switch (inputDelayTime) {
                            case "Нажмите для ввода…":
                                customToastLong(getContext(), "Введите время задержки");
                                break;
                            case "00:00:00":
                                customToastLong(getContext(), "Введите время задержки больше нуля");
                                break;
                            default:
                                smsManagerDelay.sendTextMessage(gsmNumber, null,
                                        "#13#" + inputDelayChannel + "#"
                                                + inputDelayTime + "#" + inputDelaySwitcher + "#",
                                        null, null);
                                customToastLong(getContext(), "SMS отправлено");
                                break;
                        }

                        break;

/////////////////////////////////////////////// Cycle Mode  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    case R.id.buttonCycSend:
                        inputCycChannel = spinnerCyc.getSelectedItem().toString();
                        inputCycWorkingTime = textCycWorkingTime.getText().toString();
                        inputCycReplay = numberPickerCycReplay.getValue();
                        inputCycInterval = textCycIntervalTime.getText().toString();

                        if (radioCycButtonOn.isChecked()) {
                            inputCycSwitcher = "1";
                        } else if (radioCycButtonOff.isChecked()) {
                            inputCycSwitcher = "0";
                        }


                        if (inputCycWorkingTime.equals("Нажмите для ввода…")) {
                            customToastLong(getContext(), "Введите время работы");
                        } else if (inputCycWorkingTime.equals("00:00:00")) {
                            customToastLong(getContext(), "Введите время работы больше нуля");
                        } else if (inputCycInterval.equals("00:00:00")) {
                            customToastLong(getContext(), "Введите время интервала больше нуля");
                        } else if (inputCycInterval.equals("Нажмите для ввода…")) {
                            customToastLong(getContext(), "Введите время интервала");
                        } else {
                            smsManagerDelay.sendTextMessage(gsmNumber, null, "#15#" +
                                    inputCycChannel + "#" + inputCycWorkingTime + "#" +
                                    inputCycInterval + "#" + (inputCycReplay) + "#" +
                                    inputCycSwitcher + "#", null, null);
                            customToastLong(getContext(), "SMS отправлено");
                            /*Toast.makeText(getContext(), "#15#" +
                                    inputCycChannel + "#" + inputCycWorkingTime + "#" +
                                    inputCycInterval + "#" + (Integer.toString(inputCycReplay)) + "#" +
                                    inputCycSwitcher + "#", Toast.LENGTH_SHORT).show();*/
                        }

                        break;

/////////////////////////////////////////////// Schedule Mode  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    case R.id.switchScheduleMode:
                        if (Objects.equals(gsmNumber, "Номер не задан")) {
                            Toast.makeText(getContext(), "Введите номер GSM устройства", Toast.LENGTH_LONG).show();
                        } else {
                            edScheduleMode = sharedPreferencesScheduleMode.edit();
                            if (ScheduleMode.isChecked()) {
                                ScheduleMode.setChecked(true);
                                edScheduleMode.putBoolean(SAVED_SCHEDULE_MODE, true);
                                edScheduleMode.apply();
                                SmsManager.getDefault().sendTextMessage(gsmNumber, null, "#111#1#", null, null);
                                Toast.makeText(getContext(), "SMS отправлено", Toast.LENGTH_SHORT).show();

                            } else {
                                ScheduleMode.setChecked(false);
                                edScheduleMode.putBoolean(SAVED_SCHEDULE_MODE, false);
                                edScheduleMode.apply();
                                SmsManager.getDefault().sendTextMessage(gsmNumber, null, "#111#0#", null, null);
                                Toast.makeText(getContext(), "SMS отправлено", Toast.LENGTH_SHORT).show();
                            }
                            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        }
                        break;

                    case R.id.buttonSchedule:
                        inputScheduleChannel = spinnerSchedule.getSelectedItem().toString();
                        inputScheduleDay = sWeekDaysNumber;
                        inputScheduleTimeOn = (textScheduleTimeOn.getText().toString()).split(":");
                        inputScheduleTimeOff = (textScheduleTimeOff.getText().toString()).split(":");

                        if (sWeekDaysNumber.equals("")) {
                            customToastLong(getContext(), "Выбирите день недели");

                        } else if (inputScheduleTimeOff[0].equals("Нажмите для ввода…")) {
                            customToastLong(getContext(), "Введите время выключения");
                        } else if (inputScheduleTimeOn[0].equals("Нажмите для ввода…")) {
                            customToastLong(getContext(), "Введите время включения");
                        }

                        //////////////////////////////////////////////////////////////////  Условие разницы времени включения и выключения  //////////////////////////////////////////////////////////////////////////////////

                        /*else if ((Integer.valueOf(inputScheduleTimeOn[0]) + Integer.valueOf(inputScheduleTimeOn[1])) == (Integer.valueOf(inputScheduleTimeOff[0]) + Integer.valueOf(inputScheduleTimeOff[1]))) {
                            customToastLong(getContext(), "Время выключения должно отличаться от времени включения.\nВведите корректное время выключения");
                        }*/

                        ////////////////////////////////////////////////////////////////    Отправка СМС при корректно введенных данных ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                        else {
                            smsManagerDelay.sendTextMessage(gsmNumber, null, "#111#" + inputScheduleChannel +
                                    "#" + inputScheduleDay + "#" + inputScheduleTimeOn[0] + ":" + inputScheduleTimeOn[1] + ":00" +
                                    "#" + inputScheduleTimeOff[0] + ":" + inputScheduleTimeOff[1] + "#", null, null);
                            customToastLong(getContext(), "SMS отправлено");
                        }

                        break;


                    case R.id.buttonScheduleMode:
                        /*if (Objects.requireNonNull(ScheduleModeOnOff.getString(SAVE_SCHEDULE_MODE, "Включить")).equals("Включить")) {

                            edSchedule = ScheduleModeOnOff.edit();
                            edSchedule.putString(SAVE_SCHEDULE_MODE, "Выключить");
                            edSchedule.apply();
                            buttonScheduleMode.setText(ScheduleModeOnOff.getString(SAVE_SCHEDULE_MODE, "Включить"));
                            smsManagerDelay.sendTextMessage(gsmNumber, null,
                                    "#111#1#", null, null);
                            customToastLong(getContext(), "SMS отправлено!");
                        } else {

                            edSchedule = ScheduleModeOnOff.edit();
                            edSchedule.putString(SAVE_SCHEDULE_MODE, "Включить");
                            edSchedule.apply();
                            buttonScheduleMode.setText(ScheduleModeOnOff.getString(SAVE_SCHEDULE_MODE, "Включить"));
                            smsManagerDelay.sendTextMessage(gsmNumber, null,
                                    "#111#0#", null, null);
                            customToastLong(getContext(), "SMS отправлено!");
                        }*/
                        break;

//////////////////////////////////////////////////////////////////////////  Voltage Limit   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    case R.id.buttonVoltage:
                        inputVoltageChannel = spinnerVoltageChannel.getSelectedItem().toString();
                        inputVoltageTime = numberPickerVoltageTime.getValue();
                        inputVoltageHighLevel = numberPickerVoltageHighLevel.getValue();
                        inputVoltageLowLevel = numberPickerVoltageLowLevel.getValue();
                        inputVoltagePower = numberPickerVoltagePower.getValue();
                        selected = spinnerVoltage.getSelectedItem().toString();


                        switch (selected) {

                            case ("Верхний порог напряжения"):
                                if (inputVoltageTime == 0) {
                                    smsManagerDelay.sendTextMessage(gsmNumber, null, "#222#" + inputVoltageChannel + "#1#" + inputVoltageHighLevel + "#", null, null);
                                    customToastLong(getContext(), "SMS отправлено");
                                } else {
                                    smsManagerDelay.sendTextMessage(gsmNumber, null, "#222#" + inputVoltageChannel + "#1#" + inputVoltageHighLevel + "#" + inputVoltageTime + "#", null, null);
                                    customToastLong(getContext(), "SMS отправлено");
                                }
                                break;

                            case ("Нижний порог напряжения"):
                                if (inputVoltageTime == 0) {
                                    smsManagerDelay.sendTextMessage(gsmNumber, null, "#222#" + inputVoltageChannel + "#2#" + inputVoltageLowLevel + "#", null, null);
                                    customToastLong(getContext(), "SMS отправлено");
                                } else {
                                    smsManagerDelay.sendTextMessage(gsmNumber, null, "#222#" + inputVoltageChannel + "#2#" + inputVoltageLowLevel + "#" + inputVoltageTime + "#", null, null);
                                    customToastLong(getContext(), "SMS отправлено");
                                }
                                break;

                            case ("Порог мощности"):
                                if (inputVoltageTime == 0) {
                                    smsManagerDelay.sendTextMessage(gsmNumber, null, "#222#" + inputVoltageChannel + "#3#" + inputVoltagePower + "#", null, null);
                                    customToastLong(getContext(), "SMS отправлено");
                                } else {
                                    smsManagerDelay.sendTextMessage(gsmNumber, null, "#222#" + inputVoltageChannel + "#3#" + inputVoltagePower + "#" + inputVoltageTime + "#", null, null);
                                    customToastLong(getContext(), "SMS отправлено");
                                }
                                break;
                        }

                        break;

////////////////////////////////////////////////////////////////////////////    Термореле   ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    case R.id.switchTempMode:
                        if (Objects.equals(gsmNumber, "Номер не задан")) {
                            Toast.makeText(getContext(), "Введите номер GSM устройства", Toast.LENGTH_LONG).show();
                        } else {
                            edTempMode = sharedPreferencesTempMode.edit();
                            if (TempMode.isChecked()) {
                                TempMode.setChecked(true);
                                edTempMode.putBoolean(SAVED_TEMP_MODE, true);
                                edTempMode.apply();
                                //SmsManager.getDefault().sendTextMessage(gsmNumber, null, "#111#1#", null, null);
                                //Toast.makeText(getContext(), "SMS отправлено", Toast.LENGTH_SHORT).show();

                            } else {
                                TempMode.setChecked(false);
                                edTempMode.putBoolean(SAVED_TEMP_MODE, false);
                                edTempMode.apply();
                                //SmsManager.getDefault().sendTextMessage(gsmNumber, null, "#111#0#", null, null);
                                //Toast.makeText(getContext(), "SMS отправлено", Toast.LENGTH_SHORT).show();
                            }
                            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        }
                        break;

                    case R.id.buttonTemperature:

                        inputTempChannel = spinnerTemp.getSelectedItem().toString();
                        inputTempHighLevel = numberPickerTempHigh.getValue();
                        inputTempLowLevel = numberPickerTempLow.getValue();
                        String signTempHighLevel;
                        String signTempLowLevel;

                        if (Math.abs(inputTempHighLevel - inputTempLowLevel) <= 2) {
                            customToastLong(getContext(), "Разница значений температур должна\nбыть не менее 2 градусов");
                        } else {
                            signTempHighLevel = (inputTempHighLevel > 0) ? "+" : "";
                            signTempLowLevel = (inputTempLowLevel > 0) ? "+" : "";
                            smsManagerDelay.sendTextMessage(gsmNumber, null, "#333#" + inputTempChannel + "#" + signTempHighLevel + inputTempHighLevel + "#" + signTempLowLevel + inputTempLowLevel + "#", null, null);
                            customToastLong(getContext(), "SMS отправлено");
                        }
                        break;


                    case R.id.buttonTemperatureMode:

                        /*inputTempChannel = spinnerTemp.getSelectedItem().toString();

                         *//*if (TempModeOnOff.getString(SAVE_TEMP_MODE, "Включить").equals("Включить")) {
                            edTemp = TempModeOnOff.edit();
                            edTemp.putString(SAVE_TEMP_MODE, "Выключить");
                            edTemp.apply();
                            buttonTempMode.setText("Выключить");

                            smsManagerDelay.sendTextMessage(gsmNumber, null,
                                    "#333#" + inputTempChannel + "#1#", null, null);
                            customToast(getContext(), "SMS отправлено!");
                        } else {
                            edTemp = TempModeOnOff.edit();
                            edTemp.putString(SAVE_TEMP_MODE, "Включить");
                            edTemp.apply();
                            buttonTempMode.setText("Включить");

                            smsManagerDelay.sendTextMessage(gsmNumber, null,
                                    "#333#" + inputTempChannel + "#0#", null, null);
                            customToast(getContext(), "SMS отправлено!");
                        }
                        break;*//*

                        smsManagerDelay.sendTextMessage(gsmNumber, null, "#333#" +
                                inputTempChannel + "#1#", null, null);
                        buttonTempMode.setText(R.string.TurnOff);
                        customToastLong(getContext(), "SMS отправлено!");*/
                        break;
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    default:
                        break;

                }
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
        }

        @Override
        public boolean onLongClick(View v) {

            if (gsmNumber.equals("Номер не задан")) {
                customToastLong(getContext(), "Номер GSM устройства не задан,\nперейдите в \"Настройки\"");
            } else {
                switch (v.getId()) {

                    case R.id.buttonTemperatureMode:
                        String inputTempChannel = spinnerTemp.getSelectedItem().toString();
                        smsManagerDelay.sendTextMessage(gsmNumber, null, "#333#" +
                                inputTempChannel + "#0#", null, null);
                        buttonTempMode.setText(R.string.TurnOn);
                        customToastLong(getContext(), "SMS отправлено");
                        break;

                }
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
            return true;
        }


///////////////////////////////////////////////////////////////////////////////////////////////////


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            assert getArguments() != null;
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {

                case 1:
                    final View view = inflater.inflate(R.layout.fragment_fragment_1, container, false);
                    buttonDelaySend = view.findViewById(R.id.buttonDelaySend);
                    radioDelayButtonOn = view.findViewById(R.id.radioDelayOn);
                    radioDelayButtonOff = view.findViewById(R.id.radioDelayOff);
                    textDelayTime = view.findViewById(R.id.textDelayTime);
                    spinnerDelay = view.findViewById(R.id.spinnerDelayChannel);

                    ChannelNumberToSpinner(spinnerDelay);

                    customTimePickerDialog(textDelayTime, false, 23, 59);

                    /*textDelayTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            TimePickerDialog timePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    if (minute < 10) {
                                        textDelayTime.setText(hourOfDay + ":" + "0" + minute);
                                    } else {
                                        textDelayTime.setText(hourOfDay + ":" + minute);
                                    }
                                }
                            }, 0, 0, true);
                            timePicker.show();
                        }
                    });*/

                    buttonDelaySend.setOnClickListener(this);

                    return view;

                case 2:
                    View viewCyc = inflater.inflate(R.layout.fragment_fragment_2, container, false);

                    textCycWorkingTime = viewCyc.findViewById(R.id.textCycWorkingTime);
                    textCycIntervalTime = viewCyc.findViewById(R.id.textCycIntervalTime);

                    numberPickerCycReplay = viewCyc.findViewById(R.id.numberPickerCycReplay);

                    radioCycButtonOn = viewCyc.findViewById(R.id.radioCycOn);
                    radioCycButtonOff = viewCyc.findViewById(R.id.radioCycOff);

                    buttonCycSend = viewCyc.findViewById(R.id.buttonCycSend);
                    spinnerCyc = viewCyc.findViewById(R.id.spinnerCycChannel);

                    ChannelNumberToSpinner(spinnerCyc);

                    customTimePickerDialog(textCycIntervalTime, true, 17, 59);
                    customTimePickerDialog(textCycWorkingTime, true, 17, 59);

                    /*textCycIntervalTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            TimePickerDialog timePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    String hourLess_10;
                                    String minuteLess_10;

                                    hourLess_10 = (hourOfDay < 10) ? "0" : "";
                                    minuteLess_10 = (minute < 10) ? "0" : "";

                                    if (hourOfDay > 17){
                                        Toast.makeText(getContext(), "Максимальное время интервала 17ч 59мин", Toast.LENGTH_SHORT).show();
                                    }else {
                                        textCycIntervalTime.setText(hourLess_10 + hourOfDay + ":" + minuteLess_10 + minute);
                                    }



                                    *//*if (minute < 10) {
                                        textCycIntervalTime.setText(hourOfDay + ":" + "0" + minute);
                                    } else {
                                        textCycIntervalTime.setText(hourOfDay + ":" + minute);
                                    }*//*
                                }
                            }, 0, 0, true);
                            timePicker.show();
                        }
                    });

                    textCycWorkingTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            TimePickerDialog timePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    String hourLess_10;
                                    String minuteLess_10;

                                    hourLess_10 = (hourOfDay < 10) ? "0" : "";
                                    minuteLess_10 = (minute < 10) ? "0" : "";

                                    if (hourOfDay > 17){
                                        Toast.makeText(getContext(), "Максимальное время работы 17ч 59мин", Toast.LENGTH_SHORT).show();
                                    }else {
                                        textCycWorkingTime.setText(hourLess_10 + hourOfDay + ":" + minuteLess_10 + minute);
                                    }
                                    *//*if (minute < 10) {
                                        textCycWorkingTime.setText(hourOfDay + ":" + "0" + minute);
                                    } else {
                                        textCycWorkingTime.setText(hourOfDay + ":" + minute);
                                    }*//*
                                }
                            }, 0, 0, true);
                            timePicker.show();
                        }
                    });*/

                    buttonCycSend.setOnClickListener(this);

                    return viewCyc;

                case 3:
                    View viewList = inflater.inflate(R.layout.fragment_fragment_3, container, false);
                    buttonSchedule = viewList.findViewById(R.id.buttonSchedule);
                    buttonScheduleMode = viewList.findViewById(R.id.buttonScheduleMode);
                    textScheduleTimeOn = viewList.findViewById(R.id.textScheduleTimeOn);
                    textScheduleTimeOff = viewList.findViewById(R.id.textScheduleTimeOff);

                    ScheduleMode = viewList.findViewById(R.id.switchScheduleMode);

                    textScheduleWeekDay = viewList.findViewById(R.id.textScheduleWeekDay);

                    WeekDays = getResources().getStringArray(R.array.WeekDays);
                    WeekDaysNumber = getResources().getStringArray(R.array.WeekDaysNumber);
                    checkedItems = new boolean[WeekDays.length];
                    spinnerSchedule = viewList.findViewById(R.id.spinnerSchedule);

                    sharedPreferencesScheduleMode = Objects.requireNonNull(getActivity()).getSharedPreferences(SAVED_SCHEDULE_MODE, MODE_PRIVATE);
                    ScheduleMode.setChecked(sharedPreferencesScheduleMode.getBoolean(SAVED_SCHEDULE_MODE, false));

                    //ScheduleModeOnOff = Objects.requireNonNull(getActivity()).getSharedPreferences(SAVE_SCHEDULE_MODE, MODE_PRIVATE);
                    //buttonScheduleMode.setText(ScheduleModeOnOff.getString(SAVE_SCHEDULE_MODE, "Включить"));

                    ChannelNumberToSpinner(spinnerSchedule);

                    textScheduleWeekDay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                            mBuilder.setTitle("Выбор дней недели");
                            mBuilder.setMultiChoiceItems(WeekDays, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int Day, boolean isChecked) {

                                    if (isChecked) {

                                        if (!mUserItems.contains(Day)) {
                                            mUserItems.add(Day);
                                        }
                                    } else {
                                        mUserItems.remove(Integer.valueOf(Day));     // без Integer.valueOf() вылетает ошибка при изменении состояния checkbox-ОВ
                                    }
                                }
                            });

                            mBuilder.setCancelable(true);
                            mBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    textScheduleWeekDay.setText("Нажмите для ввода…");
                                }
                            });

                            mBuilder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                int cnt = 0;

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    StringBuilder Day = new StringBuilder();
                                    StringBuilder DayNumber = new StringBuilder();


                                    for (int i = 0; i < mUserItems.size(); i++) {
                                        if (!mUserItems.get(i).toString().isEmpty()) {
                                            cnt++;
                                        }
                                    }
                                    if (cnt > 0 & cnt != 7) {
                                        for (int j = 0; j < mUserItems.size(); j++) {
                                            Day.append(WeekDays[mUserItems.get(j)]);
                                            DayNumber.append(WeekDaysNumber[mUserItems.get(j)]);
                                            if (j != mUserItems.size() - 1) {                                //  если день не последний в списке то добавляем запятую
                                                Day.append(", ");

                                            }
                                        }
                                        sWeekDaysNumber = (DayNumber.toString());
                                        textScheduleWeekDay.setText(Day.toString());
                                        dialog.dismiss();
                                    } else if (cnt == 7) {
                                        textScheduleWeekDay.setText("Выбраны все дни недели");
                                        dialog.dismiss();
                                    } else {
                                        textScheduleWeekDay.setText("Нажмите для ввода…");
                                        dialog.dismiss();
                                    }
                                }

                            });

                            mBuilder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            /*mBuilder.setNeutralButton("Выбрать все", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for (int i = 0; i < checkedItems.length; i++) {

                                        checkedItems[i] = true;


                                    }
                                    textScheduleWeekDay.setText("Выбраны все дни недели");
                                }
                            });*/
                            AlertDialog mDialog = mBuilder.create();
                            mDialog.show();
                        }
                    });

                    customTimePickerDialog(textScheduleTimeOn, false, 23, 59);
                    customTimePickerDialog(textScheduleTimeOff, false, 23, 59);

                    /*textScheduleTimeOn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            TimePickerDialog timePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    if (minute < 10) {
                                        textScheduleTimeOn.setText(hourOfDay + ":" + "0" + minute);
                                    } else {
                                        textScheduleTimeOn.setText(hourOfDay + ":" + minute);
                                    }
                                }
                            }, 0, 0, true);
                            timePicker.show();
                        }
                    });

                    textScheduleTimeOff.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            TimePickerDialog timePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    if (minute < 10) {
                                        textScheduleTimeOff.setText(hourOfDay + ":" + "0" + minute);
                                    } else {
                                        textScheduleTimeOff.setText(hourOfDay + ":" + minute);
                                    }
                                }
                            }, 0, 0, true);
                            timePicker.show();
                        }
                    });*/

                    buttonSchedule.setOnClickListener(this);
                    buttonScheduleMode.setOnClickListener(this);
                    ScheduleMode.setOnClickListener(this);

                    return viewList;

                case 4:
                    View viewVoltage = inflater.inflate(R.layout.fragment_fragment_4, container, false);
                    numberPickerVoltageTime = viewVoltage.findViewById(R.id.numberPickerVoltageTime);
                    numberPickerVoltageHighLevel = viewVoltage.findViewById(R.id.numberPickerVoltageHighLevel);
                    numberPickerVoltageLowLevel = viewVoltage.findViewById(R.id.numberPickerVoltageLowLevel);
                    numberPickerVoltagePower = viewVoltage.findViewById(R.id.numberPickerVoltagePower);
                    textVoltageHighLevel = viewVoltage.findViewById(R.id.labelVoltageHighLevel);
                    textVoltageLowLevel = viewVoltage.findViewById(R.id.labelVoltageLowLevel);
                    textVoltagePower = viewVoltage.findViewById(R.id.labelVoltagePower);
                    buttonVoltage = viewVoltage.findViewById(R.id.buttonVoltage);
                    spinnerVoltageChannel = viewVoltage.findViewById(R.id.spinnerVoltageChannel);

                    spinnerVoltage = viewVoltage.findViewById(R.id.spinnerVoltage);

                    selected = spinnerVoltage.getSelectedItem().toString();

                    ChannelNumberToSpinner(spinnerVoltageChannel);

                    spinnerVoltage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String[] choose = getResources().getStringArray(R.array.ProtectMode);

                            switch (choose[position]) {

                                case ("Верхний порог напряжения"):
                                    numberPickerVoltageHighLevel.setEnabled(true);
                                    numberPickerVoltageHighLevel.setVisibility(View.VISIBLE);
                                    textVoltageHighLevel.setEnabled(true);
                                    textVoltageHighLevel.setVisibility(View.VISIBLE);
                                    numberPickerVoltageLowLevel.setEnabled(false);
                                    numberPickerVoltageLowLevel.setVisibility(View.GONE);
                                    textVoltageLowLevel.setEnabled(false);
                                    textVoltageLowLevel.setVisibility(View.GONE);
                                    numberPickerVoltagePower.setEnabled(false);
                                    numberPickerVoltagePower.setVisibility(View.GONE);
                                    textVoltagePower.setEnabled(false);
                                    textVoltagePower.setVisibility(View.GONE);
                                    break;

                                case ("Нижний порог напряжения"):
                                    numberPickerVoltageHighLevel.setEnabled(false);
                                    numberPickerVoltageHighLevel.setVisibility(View.GONE);
                                    textVoltageHighLevel.setEnabled(false);
                                    textVoltageHighLevel.setVisibility(View.GONE);
                                    numberPickerVoltageLowLevel.setEnabled(true);
                                    numberPickerVoltageLowLevel.setVisibility(View.VISIBLE);
                                    textVoltageLowLevel.setEnabled(true);
                                    textVoltageLowLevel.setVisibility(View.VISIBLE);
                                    numberPickerVoltagePower.setEnabled(false);
                                    numberPickerVoltagePower.setVisibility(View.GONE);
                                    textVoltagePower.setEnabled(false);
                                    textVoltagePower.setVisibility(View.GONE);
                                    break;

                                case ("Порог мощности"):
                                    numberPickerVoltageHighLevel.setEnabled(false);
                                    numberPickerVoltageHighLevel.setVisibility(View.GONE);
                                    textVoltageHighLevel.setEnabled(false);
                                    textVoltageHighLevel.setVisibility(View.GONE);
                                    numberPickerVoltageLowLevel.setEnabled(false);
                                    numberPickerVoltageLowLevel.setVisibility(View.GONE);
                                    textVoltageLowLevel.setEnabled(false);
                                    textVoltageLowLevel.setVisibility(View.GONE);
                                    numberPickerVoltagePower.setEnabled(true);
                                    numberPickerVoltagePower.setVisibility(View.VISIBLE);
                                    textVoltagePower.setEnabled(true);
                                    textVoltagePower.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }

                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                    buttonVoltage.setOnClickListener(this);

                    return viewVoltage;


                case 5:

                    final View viewTemp = inflater.inflate(R.layout.fragment_fragment_5, container, false);

                    numberPickerTempHigh = viewTemp.findViewById(R.id.numberPickerHighTemp);
                    numberPickerTempLow = viewTemp.findViewById(R.id.numberPickerLowTemp);
                    buttonTempSend = viewTemp.findViewById(R.id.buttonTemperature);
                    buttonTempMode = viewTemp.findViewById(R.id.buttonTemperatureMode);
                    spinnerTemp = viewTemp.findViewById(R.id.spinnerTempChannel);

                    TempMode = viewTemp.findViewById(R.id.switchTempMode);

                    ChannelNumberToSpinner(spinnerTemp);

                    sharedPreferencesTempMode = Objects.requireNonNull(getActivity()).getSharedPreferences(SAVED_TEMP_MODE, MODE_PRIVATE);
                    TempMode.setChecked(sharedPreferencesTempMode.getBoolean(SAVED_TEMP_MODE, false));

                    buttonTempSend.setOnClickListener(this);
                    buttonTempMode.setOnClickListener(this);
                    buttonTempMode.setOnLongClickListener(this);
                    TempMode.setOnClickListener(this);

                    return viewTemp;

                default:
                    return inflater.inflate(R.layout.fragment_mode, container, false);
            }

        }

        private void ChannelNumberToSpinner(final Spinner spinner) {
            PickerViewModel pickerViewModel;
            final LinkedHashSet<String> spinnerList;
            spinnerList = new LinkedHashSet<>();

            pickerViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PickerViewModel.class);
            pickerViewModel.loadPickerChannels().observe(getViewLifecycleOwner(), new Observer<List<Picker>>() {
                @SuppressLint("ResourceType")
                @Override
                public void onChanged(@Nullable final List<Picker> pickers) {
                    Log.d("Size_pickers=", String.valueOf(Objects.requireNonNull(pickers).size()));
                    for (int i = 0; i < Objects.requireNonNull(pickers).size(); i++) {
                        if (Objects.requireNonNull(pickers).get(i).chDesign.equals("...")) {
                            spinnerList.add(String.valueOf(pickers.get(i).chName));
                            Log.d("Добавлен канал ", String.valueOf(pickers.get(i).chName));
                        } else {
                            spinnerList.add(Objects.requireNonNull(pickers).get(i).chDesign.substring(0, 1).toUpperCase() + Objects.requireNonNull(pickers).get(i).chDesign.substring(1).toLowerCase());
                            Log.d("Добавлен канал ", String.valueOf(pickers.get(i).chDesign));
                        }
                        Log.d("Iteration=", String.valueOf(i));
                    }
                    final ArrayAdapter adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.spinner_item, R.id.spinnerCustom1, spinnerList.toArray());
                    spinner.setAdapter(adapter);
                }
            });
        }

        /*private void customTimePickerDialog(@NonNull final TextView textView, final boolean hasLimit, final int hourLimit, final int minuteLimit){
            textView.setOnClickListener(new View.OnClickListener() {
                TimePickerDialog timePicker;

                @Override
                public void onClick(View view) {
                    timePicker = new TimePickerDialog( new ContextThemeWrapper(getActivity(),  android.R.style.Theme_Holo_Light_Dialog),  new TimePickerDialog.OnTimeSetListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String hourLess_10;
                            String minuteLess_10;

                            hourLess_10 = (hourOfDay < 10) ? "0" : "";
                            minuteLess_10 = (minute < 10) ? "0" : "";

                            if (hasLimit){

                                if (hourOfDay > hourLimit){
                                    textView.setText("Нажмите для ввода…");
                                    Toast.makeText(view.getContext(), "Максимальное время работы " + hourLimit +"ч "+ minuteLimit + "мин", Toast.LENGTH_SHORT).show();
                                }else {
                                    textView.setText(hourLess_10 + hourOfDay + ":" + minuteLess_10 + minute);
                                }
                            }
                            else {
                                textView.setText(hourLess_10 + hourOfDay + ":" + minuteLess_10 + minute);
                            }
                        }
                    }, 0, 0, true);
                    timePicker.show();


                }

            });

        }
    }*/

        private void customTimePickerDialog(@NonNull final TextView textView, final boolean hasLimit, final int hourLimit, final int minuteLimit) {
            textView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder timePickerBuilder = new AlertDialog.Builder(getActivity());
                    LayoutInflater layoutInflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
                    view = layoutInflater.inflate(R.layout.custom_time_picker, null);
                    final NumberPicker hour = view.findViewById(R.id.HourPicker);
                    final NumberPicker minute = view.findViewById(R.id.MinutePicker);
                    final NumberPicker second = view.findViewById(R.id.SecondPicker);

                    hour.setMinValue(0);
                    hour.setMaxValue(23);
                    minute.setMinValue(0);
                    minute.setMaxValue(59);
                    second.setMinValue(0);
                    second.setMaxValue(59);

                    hour.setWrapSelectorWheel(true);
                    minute.setWrapSelectorWheel(true);
                    second.setWrapSelectorWheel(true);

                    hour.setFormatter(new NumberPicker.Formatter() {
                        @Override
                        public String format(int value) {
                            return String.format(Locale.getDefault(),"%02d", value);
                        }
                    });

                    minute.setFormatter(new NumberPicker.Formatter() {
                        @Override
                        public String format(int value) {
                            return String.format(Locale.getDefault(),"%02d", value);
                        }
                    });

                    second.setFormatter(new NumberPicker.Formatter() {
                        @Override
                        public String format(int value) {
                            return String.format(Locale.getDefault(),"%02d", value);
                        }
                    });



                    timePickerBuilder.setTitle("Введите время")
                            .setView(view)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (hasLimit) {
                                if (hour.getValue() > hourLimit) {
                                    textView.setText("Нажмите для ввода…");
                                    Toast.makeText(getContext(), "Максимальное время работы " + hourLimit + "ч " + minuteLimit + "мин", Toast.LENGTH_SHORT).show();
                                } else if (hour.getValue() == 0 && minute.getValue() == 0 && second.getValue() == 0) {
                                    Toast.makeText(getContext(), "Время работы должно быть больше нуля", Toast.LENGTH_SHORT).show();
                                } else {
                                    textView.setText(String.format(Locale.getDefault(),"%02d", hour.getValue())
                                            + ":" + String.format(Locale.getDefault(),"%02d", minute.getValue())
                                            + ":" + String.format(Locale.getDefault(),"%02d", second.getValue()));
                                }
                            } else {
                                textView.setText(String.format(Locale.getDefault(),"%02d", hour.getValue())
                                        + ":" + String.format(Locale.getDefault(),"%02d", minute.getValue())
                                        + ":" + String.format(Locale.getDefault(),"%02d", second.getValue()));
                            }
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
                            .show();


                        /*public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String hourLess_10;
                            String minuteLess_10;

                            hourLess_10 = (hourOfDay < 10) ? "0" : "";
                            minuteLess_10 = (minute < 10) ? "0" : "";

                            if (hasLimit){

                                if (hourOfDay > hourLimit){
                                    textView.setText("Нажмите для ввода…");
                                    Toast.makeText(view.getContext(), "Максимальное время работы " + hourLimit +"ч "+ minuteLimit + "мин", Toast.LENGTH_SHORT).show();
                                }else {
                                    textView.setText(hourLess_10 + hourOfDay + ":" + minuteLess_10 + minute);
                                }
                            }
                            else {
                                textView.setText(hourLess_10 + hourOfDay + ":" + minuteLess_10 + minute);
                            }
                        }
                    }, 0, 0, true);
                    timePicker.show();*/


                }

            });

        }
    }


    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 5; // Show 5 total pages.
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Коммутация с задержкой";
                case 1:
                    return "Циклическая коммутация";
                case 2:
                    return "Управление по расписанию";
                case 3:
                    return "Параметры защиты";
                case 4:
                    return "Термореле";

            }
            return null;
        }
    }

    public static void customToastLong(Context context, String message) {
        Toast toastMarket = Toast.makeText(context, message, Toast.LENGTH_LONG);
        //toastMarket.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 150);
        toastMarket.show();
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
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

}
