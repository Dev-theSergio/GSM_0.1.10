package com.kbrs.app.GsmRemoteControl;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class SmsListRepository {

    private SmsDao smsDao;

    private LiveData<List<SmsListTable>> AllSms;

    public SmsListRepository(Application application) {
        SmsListDB smsDatabase = SmsListDB.getInstance(application);
        smsDao = smsDatabase.SmsDao();
        AllSms = smsDao.getAllSms();

    }

    public void insert(SmsListTable smsListTable) {
        new InsertSmsAsyncTask(smsDao).execute(smsListTable);
    }

    public void update(SmsListTable smsListTable) {
        new UpdateSmsAsyncTask(smsDao).execute(smsListTable);
    }

    public void delete(SmsListTable smsListTable) {
        new DeleteSmsAsyncTask(smsDao).execute(smsListTable);
    }

    public void deleteAllSms() {
        new DeleteAllSmsAsyncTask(smsDao).execute();
    }



    public LiveData<List<SmsListTable>> getAllSms() {
        return AllSms;
    }


    private static class InsertSmsAsyncTask extends AsyncTask<SmsListTable, Void, Void> {
        private SmsDao smsDao;

        private InsertSmsAsyncTask(SmsDao smsDao) {
            this.smsDao = smsDao;
        }


        @Override
        protected Void doInBackground(SmsListTable... smsListTable) {
            smsDao.insert(smsListTable[0]);
            return null;
        }
    }

    private static class UpdateSmsAsyncTask extends AsyncTask<SmsListTable, Void, Void> {
        private SmsDao smsDao;

        private UpdateSmsAsyncTask(SmsDao smsDao) {
            this.smsDao = smsDao;
        }


        @Override
        protected Void doInBackground(SmsListTable... smsListTable) {
            smsDao.update(smsListTable[0]);
            return null;
        }
    }

    private static class DeleteSmsAsyncTask extends AsyncTask<SmsListTable, Void, Void> {
        private SmsDao smsDao;

        private DeleteSmsAsyncTask(SmsDao smsDao) {
            this.smsDao = smsDao;
        }


        @Override
        protected Void doInBackground(SmsListTable... smsListTable) {
            smsDao.delete(smsListTable[0]);
            return null;
        }
    }

    private static class DeleteAllSmsAsyncTask extends AsyncTask<Void, Void, Void> {
        private SmsDao smsDao;

        private DeleteAllSmsAsyncTask(SmsDao smsDao) {
            this.smsDao = smsDao;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            smsDao.deleteAllSms();
            return null;
        }
    }


}
