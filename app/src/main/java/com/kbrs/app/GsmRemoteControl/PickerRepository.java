package com.kbrs.app.GsmRemoteControl;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class PickerRepository {

    private ChannelsDao pickerDataAccessObject;

    private LiveData<List<Picker>> listChannels;////

    public PickerRepository(Application application) {
        ChannelsDB database = ChannelsDB.getInstance(application);
        pickerDataAccessObject = database.channelsDataAccessObject();
        listChannels = pickerDataAccessObject.loadPickerChannels();
    }

    public LiveData<List<Picker>> loadPickerChannels(){return listChannels;}

}
