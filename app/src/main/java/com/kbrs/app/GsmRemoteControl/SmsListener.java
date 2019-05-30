package com.kbrs.app.GsmRemoteControl;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class SmsListener extends BroadcastReceiver {

    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    String smsBody;
    String originatinAddress;
    //HashSet<String> msg;

    /*final StringBuilder infoSMS = new StringBuilder();*/

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), "android.provider.Telephony.SMS_RECEIVED")) {
            Bundle extras = intent.getExtras();

            final SharedPreferences gsm = context.getSharedPreferences(SettingsActivity.SAVED_TEXT_GSM, MODE_PRIVATE);
            final String gsmNumber = gsm.getString(SettingsActivity.SAVED_TEXT_GSM, "Номер не задан");

            //StringBuilder infoSMS = new StringBuilder();

            if (extras != null && intent.getAction() != null &&
                    ACTION.compareToIgnoreCase(intent.getAction()) == 0) {

                try {
                    Object[] pdus = (Object[]) extras.get("pdus");
                    final SmsMessage[] messages = new SmsMessage[Objects.requireNonNull(pdus).length];

                    for (int i = 0; i < Objects.requireNonNull(pdus).length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        originatinAddress = messages[i].getOriginatingAddress();
                    }

                        StringBuilder stringBuffer = new StringBuilder();
                    for (SmsMessage message : messages) {
                        stringBuffer.append(message.getMessageBody());
                        smsBody = stringBuffer.toString();
                    }

                        /*if (messages.length == 1){
                            smsBody = stringBuffer.append(messages[0].getMessageBody()).toString();
                            Log.i("messageLength_1", String.valueOf(messages.length));
                        }else if (messages.length == 2){
                            smsBody = stringBuffer.append(messages[0].getMessageBody())
                                    .append(messages[1].getMessageBody()).toString();
                            Log.i("messageLength_2", String.valueOf(messages.length));
                        }else if (messages.length == 3){
                            smsBody = stringBuffer.append(messages[0].getMessageBody())
                                    .append(messages[1].getMessageBody())
                                    .append(messages[2].getMessageBody()).toString();
                            Log.i("messageLength_3", String.valueOf(messages.length));
                        }else if (messages.length == 4){
                            smsBody = stringBuffer.append(messages[0].getMessageBody())
                                    .append(messages[1].getMessageBody())
                                    .append(messages[2].getMessageBody())
                                    .append(messages[3].getMessageBody()).toString();
                            Log.i("messageLength_4", String.valueOf(messages.length));
                        }else if (messages.length == 5){
                            smsBody = stringBuffer.append(messages[0].getMessageBody())
                                    .append(messages[1].getMessageBody())
                                    .append(messages[2].getMessageBody())
                                    .append(messages[3].getMessageBody())
                                    .append(messages[4].getMessageBody()).toString();
                            Log.i("messageLength_5", String.valueOf(messages.length));
                        }*/



                        if (Objects.equals(originatinAddress, gsmNumber)) {
                            //infoSMS.append(smsMessages.getMessageBody());
                            Log.i("sms", smsBody);

                            Log.i("address", originatinAddress);

                            //markMessageRead(context.getApplicationContext(), gsmNumber, infoSMS.toString());

                            String dateStamp = new SimpleDateFormat("dd-MMM-yyyy", new Locale("ru")).format(new Date()).replaceAll("\\.", "");
                            String timeStamp = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(Calendar.getInstance().getTime());

                            Intent intentPassSms = new Intent(context, SmsService.class); //android.intent.action.SMS_RECEIVED

                            intentPassSms.putExtra("smsBody", smsBody/*infoSMS.toString()*/);
                            intentPassSms.putExtra("smsDate", dateStamp);
                            intentPassSms.putExtra("smsTime", timeStamp);
                            //context.sendBroadcast(intentPassSms);
                            context.startService(intentPassSms);
                            //abortBroadcast();

                        //}
                        //infoSMS.append(smsMessages[i].getMessageBody());
                        //infoSMS.append("\n");

                    }

                } catch (Exception e) {
                    Log.e("errorBroadCast","SmsListener error");
                    //SMSCallback.smsReceiveError(e);
                }
            }

        }
    }

    public void markMessageRead(Context context, final String number, final String body) {

        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        try{

            while (Objects.requireNonNull(cursor).moveToNext()) {
                if ((cursor.getString(cursor.getColumnIndex("address")).equals(number)) && (cursor.getInt(cursor.getColumnIndex("read")) == 0)) {
                    if (cursor.getString(cursor.getColumnIndex("body")).startsWith(body)) {
                        String SmsMessageId = cursor.getString(cursor.getColumnIndex("_id"));
                        ContentValues values = new ContentValues();
                        values.put("read", 1);
                        context.getContentResolver().update(Uri.parse("content://sms/inbox"), values, "_id=" + SmsMessageId, null);
                        Log.d("mark", SmsMessageId + " " + values);
                        return;
                    }
                }
            }
        }catch(Exception e)
        {
            Log.e("Mark Read", "Error in Read: " + e.toString());
        }
        finally {
            Objects.requireNonNull(cursor).close();
        }
    }

}
