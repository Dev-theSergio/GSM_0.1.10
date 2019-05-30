package com.kbrs.app.GsmRemoteControl;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "sms_list_table"/*, indices = @Index(value = {"smsBody"}, unique = true)*/)
//"chName" unique - для исключения повторений номеров каналов


public class SmsListTable {

    @PrimaryKey(autoGenerate = true)

    private int id;

    @ColumnInfo(name = "smsBody")
    private String smsBody;

    @ColumnInfo(name = "smsDate")
    private String smsDate;

    @ColumnInfo(name = "smsTime")
    private String smsTime;

    /*@ColumnInfo(name = "smsCheck")
    private boolean smsCheck;*/


    public SmsListTable(String smsBody, String smsDate, String smsTime/*, boolean smsCheck*/) {
        this.smsBody = smsBody;
        this.smsDate = smsDate;
        this.smsTime = smsTime;
        /*this.smsCheck = smsCheck;*/
    }


    public int getId() {
        return id;

    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSmsBody() {
        return smsBody;
    }

    public void setSmsBody(String smsBody) {
        this.smsBody = smsBody;
    }

    public String getSmsDate() {
        return smsDate;
    }

    public void setSmsDate(String smsDate) {
        this.smsDate = smsDate;
    }

    public String getSmsTime() {
        return smsTime;
    }

    public void setSmsTime(String smsTime) {
        this.smsTime = smsTime;
    }

    /*public void setSmsCheck(boolean smsCheck) {
        this.smsCheck = smsCheck;
    }

    public boolean isSmsCheck() {
        return smsCheck;
    }*/

}


