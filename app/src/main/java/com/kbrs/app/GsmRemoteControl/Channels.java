package com.kbrs.app.GsmRemoteControl;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "channels_table", indices = @Index(value = {"chName"}, unique = true))  //"chName" unique - для исключения повторений номеров каналов
public class Channels {

    @PrimaryKey(autoGenerate = true)

    private int id;
    @ColumnInfo(name = "chName") private int chName;
    @ColumnInfo(name = "chDesign") private String chDesign;
    @ColumnInfo(name = "chSwitch") private boolean chSwitch;

    public Channels(int chName, String chDesign, boolean chSwitch) {
        this.chName = chName;
        this.chDesign = chDesign;
        this.chSwitch = chSwitch;
    }

    public void setChSwitch(boolean chSwitch) {
        this.chSwitch = chSwitch;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getChName() {
        return chName;
    }

    public String getChDesign() {
        return chDesign;
    }

    public boolean isChSwitch(){return chSwitch;}

}

