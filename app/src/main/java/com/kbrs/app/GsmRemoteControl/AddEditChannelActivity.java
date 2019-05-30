package com.kbrs.app.GsmRemoteControl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;


public class AddEditChannelActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "saved_add_id";
    public static final String EXTRA_NUMBER = "saved_add_number";
    public static final String EXTRA_TITLE = "saved_add_text";
    public static final String EXTRA_CHECKED = "saved_add_switch";

    private TextInputLayout editChannelNumber;
    private TextInputLayout editChannelDescription;
    TextInputEditText addEditChannelDescription;
    Button buttonAddChannelSave;
    private String switchChecked;

    String number;
    String description;
    String extraDescription;
    String extraNumber;

    private String phoneNumber;
    SharedPreferences phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_channel);

        editChannelDescription = findViewById(R.id.editAddChannelDescription);
        editChannelNumber = findViewById(R.id.editAddChannelNumber);
        buttonAddChannelSave = findViewById(R.id.buttonAddChannelSave);
        addEditChannelDescription = findViewById(R.id.addEditChannelDescription);

        Typeface customFont = Typeface.createFromAsset(getAssets(), "font/Circe-Regular.ttf");
        editChannelNumber.setTypeface(customFont);
        editChannelDescription.setTypeface(customFont);

        //addEditChannelDescription.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES); //!!!!!!!!!!!!!!!!!!

        phone = this.getSharedPreferences(SettingsActivity.SAVED_TEXT_GSM, MODE_PRIVATE);
        phoneNumber = phone.getString(SettingsActivity.SAVED_TEXT_GSM, "Номер не задан");

        final Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Редактирование канала");
            extraDescription = intent.getStringExtra(EXTRA_TITLE);
            extraNumber = intent.getStringExtra(EXTRA_NUMBER);
            switchChecked = intent.getStringExtra(EXTRA_CHECKED);

            if(extraDescription.equals("...")){
                Objects.requireNonNull(editChannelNumber.getEditText()).setText(intent.getStringExtra(EXTRA_NUMBER));
                Objects.requireNonNull(editChannelDescription.getEditText()).setText("");
            }else {
                Objects.requireNonNull(editChannelNumber.getEditText()).setText(intent.getStringExtra(EXTRA_NUMBER));
                Objects.requireNonNull(editChannelDescription.getEditText()).setText(intent.getStringExtra(EXTRA_TITLE));
            }
            editChannelNumber.setEnabled(false);
        } else {
            setTitle("Добавление канала");
        }

        buttonAddChannelSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                if ((phoneNumber.equals("Номер не задан")
                        & !(editChannelDescription.getEditText().getText().toString().equals("...")
                        | editChannelDescription.getEditText().getText().toString().equals("")
                        | editChannelDescription.getEditText().getText().toString().equals(intent.getStringExtra(EXTRA_TITLE))))
                        | (phoneNumber.equals("Номер не задан")
                        & !editChannelNumber.getEditText().getText().toString().equals(intent.getStringExtra(EXTRA_NUMBER)))) {
                    Toast.makeText(AddEditChannelActivity.this,
                            "Номер GSM устройства не задан,\nперейдите в \"Настройки\"", Toast.LENGTH_SHORT).show();

                } else {
                    saveAddEditChannel();
                    Log.d("add","success");
                }
            }
        });


    }

    private void saveAddEditChannel() {


        number = Objects.requireNonNull(editChannelNumber.getEditText()).getText().toString().trim();
        description = Objects.requireNonNull(editChannelDescription.getEditText()).getText().toString().trim();

        Intent data = new Intent();

        if (number.isEmpty() & editChannelNumber.isEnabled()) {                                     // если поле ввода номера канала включено и пустое
            editChannelNumber.setError("Поле не может быть пустым.");
        } else {
            if (number.equals(".")) {                                                               // если . в поле ввода
                editChannelNumber.setError("Введите число.");
            } else {
                if (number.equals("1") | number.equals("2") | number.equals("3")) {                 // если выбраны мастер каналы

                    if (description.isEmpty()) {                                                    // если описание мастер канала пустое
                        if (!editChannelNumber.isEnabled()) {                                       // если поле ввода номера slave канала неактивно (== режим редактирования)
                            if (!extraDescription.equals("...")) {                                 // и если установленное значение идентификатора не равно значению по умолчанию
                                data.putExtra(EXTRA_TITLE, "...");                            // установить описание по умочанию
                                data.putExtra(EXTRA_NUMBER, number);                                // передать данные номера канала
                                data.putExtra(EXTRA_CHECKED, switchChecked);                        // передать данные состояния
                                int id = getIntent().getIntExtra(EXTRA_ID, -1);

                                if (id != -1) {
                                    data.putExtra(EXTRA_ID, id);
                                }
                                setResult(RESULT_FIRST_USER, data);
                                finish();
                            } else {
                                setResult(RESULT_CANCELED);
                                finish();
                            }

                        } else {
                            data.putExtra(EXTRA_TITLE, "...");                                // установить описание по умочанию
                            data.putExtra(EXTRA_NUMBER, number);                                    // передать данные номера канала
                            data.putExtra(EXTRA_CHECKED, switchChecked);                            // передать данные состояния
                            int id = getIntent().getIntExtra(EXTRA_ID, -1);

                            if (id != -1) {
                                data.putExtra(EXTRA_ID, id);
                            }

                            setResult(RESULT_OK, data);
                            finish();
                        }
                    } else if (editChannelDescription.getEditText().getText().
                            toString().trim().equals(getIntent().getStringExtra(EXTRA_TITLE))) {    // если поле описания мастер канала не изменно
                        setResult(RESULT_CANCELED);                                                 // ничего не делать - отмена обновления
                        finish();
                    } else {
                        editChannelNumber.setError(null);                                           // сброс ошибки
                        data.putExtra(EXTRA_NUMBER, getIntent().getStringExtra(EXTRA_NUMBER));      // номер мастер канала неизменен
                        data.putExtra(EXTRA_TITLE, description);                                    // обновляем описание
                        data.putExtra(EXTRA_CHECKED, switchChecked);

                        int id = getIntent().getIntExtra(EXTRA_ID, -1);
                        if (id != -1) {
                            data.putExtra(EXTRA_ID, id);
                        }

                        setResult(RESULT_OK, data);
                        finish();
                    }

                } else if (Integer.valueOf(number) < 4 | Integer.valueOf(number) > 19) {            // если значение slave каналов не в диапазоне
                    editChannelNumber.setError("Введите число от 4 до 19.");                        // ошибка ввода

                } else if (editChannelNumber.getEditText().getText().                               // если номер slave канала не изменился
                        toString().trim().equals(getIntent().getStringExtra(EXTRA_NUMBER))
                        & editChannelDescription.getEditText().getText().                           // и описание slave канала не изменилось
                        toString().trim().equals(getIntent().getStringExtra(EXTRA_TITLE))) {
                    setResult(RESULT_CANCELED);                                                     // ничего не делать - отмена обновления
                    finish();

                } else {
                    if (description.isEmpty()) {                                                    // если описание slave канала пустое
                        editChannelNumber.setError(null);                                           // сбросить ошибку
                        if (!editChannelNumber.isEnabled()) {                                       // если поле ввода номера slave канала неактивно (== режим редактирования)
                            if (!extraDescription.equals("...")) {                                 // и если установленное значение идентификатора не равно значению по умолчанию
                                data.putExtra(EXTRA_TITLE, "...");                            // установить описание по умочанию
                                data.putExtra(EXTRA_NUMBER, number);                                // передать данные номера канала
                                data.putExtra(EXTRA_CHECKED, switchChecked);                        // передать данные состояния
                                int id = getIntent().getIntExtra(EXTRA_ID, -1);

                                if (id != -1) {
                                    data.putExtra(EXTRA_ID, id);
                                }
                                setResult(RESULT_FIRST_USER, data);
                                finish();
                            } else {
                                setResult(RESULT_CANCELED);
                                finish();
                            }

                        } else {
                            data.putExtra(EXTRA_TITLE, "...");                                // установить описание по умочанию
                            data.putExtra(EXTRA_NUMBER, number);                                    // передать данные номера канала
                            data.putExtra(EXTRA_CHECKED, switchChecked);                            // передать данные состояния
                            int id = getIntent().getIntExtra(EXTRA_ID, -1);

                            if (id != -1) {
                                data.putExtra(EXTRA_ID, id);
                            }

                            setResult(RESULT_OK, data);
                            finish();
                        }
                    } else {
                        editChannelNumber.setError(null);
                        data.putExtra(EXTRA_TITLE, description);
                        data.putExtra(EXTRA_NUMBER, number);
                        data.putExtra(EXTRA_CHECKED, switchChecked);
                        int id = getIntent().getIntExtra(EXTRA_ID, -1);

                        if (id != -1) {
                            data.putExtra(EXTRA_ID, id);
                        }

                        setResult(RESULT_OK, data);
                        finish();

                    }
                }
            }
        }
    }
}
