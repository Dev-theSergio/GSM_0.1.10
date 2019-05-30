package com.kbrs.app.GsmRemoteControl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Objects;

public class SmsService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String sms_body = Objects.requireNonNull(intent.getExtras()).getString("smsBody");
        String sms_date = Objects.requireNonNull(intent.getExtras()).getString("smsDate");
        String sms_time = Objects.requireNonNull(intent.getExtras()).getString("smsTime");
        //Toast.makeText(this, sms_body, Toast.LENGTH_SHORT).show();

        SmsListRepository smsListRepository = new SmsListRepository(getApplication());
        smsListRepository.insert(new SmsListTable(sms_body,sms_date,sms_time));

        //showNotification(sms_body);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent("com.kbrs.app.GsmRemoteControl.SmsService");
        intent.putExtra("yourValue", "s");
        sendBroadcast(intent);
    }
}