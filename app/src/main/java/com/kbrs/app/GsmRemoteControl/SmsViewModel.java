package com.kbrs.app.GsmRemoteControl;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class SmsViewModel extends AndroidViewModel {

        private SmsListRepository repository;
        private LiveData<List<SmsListTable>> allSms;

        public SmsViewModel(@NonNull Application application) {
            super(application);
            repository = new SmsListRepository(application);
            allSms = repository.getAllSms();
        }

        public void insert(SmsListTable smsListTable) {
            repository.insert(smsListTable);
        }

        public void update(SmsListTable smsListTable) {
            repository.update(smsListTable);
        }

        public void delete(SmsListTable smsListTable) {
            repository.delete(smsListTable);
        }

        public void deleteAllSms() {
        repository.deleteAllSms();
    }

        public LiveData<List<SmsListTable>> getAllSms() {
            return allSms;
        }


}
