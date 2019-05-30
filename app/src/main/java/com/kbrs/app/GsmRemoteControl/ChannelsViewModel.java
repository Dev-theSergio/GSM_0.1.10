package com.kbrs.app.GsmRemoteControl;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class ChannelsViewModel extends AndroidViewModel {

    private ChannelsRepository repository;
    private LiveData<List<Channels>> allChannels;
    private LiveData<List<Channels>> allChannelsOn;

    public ChannelsViewModel(@NonNull Application application) {
        super(application);
        repository = new ChannelsRepository(application);
        allChannels = repository.getAllChannels();
    }

    public void insert(Channels channels) {
        repository.insert(channels);
    }

    public void update(Channels channels) {
        repository.update(channels);
    }

    public void delete(Channels channels) {
        repository.delete(channels);
    }

    public LiveData<List<Channels>> getAllChannels() {
        return allChannels;
    }

    public void getAllChannelsOn() {
        repository.getAllChannelsOn();
    }

    public void getAllChannelsOff() {
        repository.getAllChannelsOff();
    }

}
