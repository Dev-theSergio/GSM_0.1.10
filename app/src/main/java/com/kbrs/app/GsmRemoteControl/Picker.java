package com.kbrs.app.GsmRemoteControl;

import android.arch.persistence.room.ColumnInfo;

public class Picker{

    @ColumnInfo(name = "chName") public int chName;
    @ColumnInfo(name = "chDesign") public String chDesign;
}
