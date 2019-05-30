package com.kbrs.app.GsmRemoteControl;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class PickerViewModel extends AndroidViewModel {

    private PickerRepository repository;
    private LiveData<List<Picker>> PickerChannels;

    public PickerViewModel(@NonNull Application application) {
        super(application);
        repository = new PickerRepository(application);
        PickerChannels = repository.loadPickerChannels();
    }
    public LiveData<List<Picker>> loadPickerChannels(){ return  PickerChannels;}


}
