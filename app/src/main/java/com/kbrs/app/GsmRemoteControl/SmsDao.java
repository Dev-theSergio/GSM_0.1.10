package com.kbrs.app.GsmRemoteControl;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface SmsDao {

    @Insert//(onConflict = OnConflictStrategy.IGNORE) // OnConflictStrategy.IGNORE для исключения повторений номеров каналов
    void insert(SmsListTable... smsListTables);

    @Update//(onConflict = OnConflictStrategy.IGNORE)
    void update(SmsListTable smsListTables);

    @Delete
    void delete(SmsListTable smsListTables);

    @Query("SELECT * FROM sms_list_table ORDER BY id DESC")
    LiveData<List<SmsListTable>> getAllSms();

    @Query("DELETE  FROM sms_list_table")
    void deleteAllSms();

    /*@Query("SELECT chName, chDesign FROM channels_table")
    LiveData<List<Picker>> loadPickerChannels();*/
}

