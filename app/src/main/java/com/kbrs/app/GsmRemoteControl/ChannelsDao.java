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
public interface ChannelsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE) // OnConflictStrategy.IGNORE для исключения повторений номеров каналов
    void insert(Channels... channels);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Channels channels);

    @Delete
    void delete(Channels channels);

    @Query("SELECT * FROM channels_table ORDER BY chName")
    LiveData<List<Channels>> getAllChannels();

    @Query("UPDATE channels_table SET chSwitch = 1 WHERE chSwitch = 0")
    void getAllChannelsOn();

    @Query("UPDATE channels_table SET chSwitch = 0 WHERE chSwitch = 1")
    void getAllChannelsOff();

    @Query("SELECT chName, chDesign FROM channels_table ORDER BY chName")
    LiveData<List<Picker>> loadPickerChannels();

}
