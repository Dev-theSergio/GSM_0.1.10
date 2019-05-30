package com.kbrs.app.GsmRemoteControl;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class ChannelsRepository {

    private ChannelsDao channelsDao;

    private LiveData<List<Channels>> allChannels;
    private LiveData<List<Channels>> allChannelsOn;

    public ChannelsRepository(Application application) {
        ChannelsDB database = ChannelsDB.getInstance(application);
        channelsDao = database.channelsDataAccessObject();
        allChannels = channelsDao.getAllChannels();
    }

    public void insert(Channels channels) {
        new InsertChannelAsyncTask(channelsDao).execute(channels);
    }

    public void update(Channels channels) {
        new UpdateChannelAsyncTask(channelsDao).execute(channels);
    }

    public void delete(Channels channels) {
        new DeleteChannelAsyncTask(channelsDao).execute(channels);
    }

    public LiveData<List<Channels>> getAllChannels() {
        return allChannels;
    }

    public void getAllChannelsOn() {
        new AllOnChannelAsyncTask(channelsDao).execute();
    }

    public void getAllChannelsOff() {
        new AllOffChannelAsyncTask(channelsDao).execute();
    }


    private static class InsertChannelAsyncTask extends AsyncTask<Channels, Void, Void> {
        private ChannelsDao channelsDao;

        private InsertChannelAsyncTask(ChannelsDao channelsDao) {
            this.channelsDao = channelsDao;
        }

        @Override
        protected Void doInBackground(Channels... channels) {
            channelsDao.insert(channels[0]);
            return null;
        }
    }

    private static class UpdateChannelAsyncTask extends AsyncTask<Channels, Void, Void> {
        private ChannelsDao channelsDao;

        private UpdateChannelAsyncTask(ChannelsDao channelsDao) {
            this.channelsDao = channelsDao;
        }

        @Override
        protected Void doInBackground(Channels... channels) {
            channelsDao.update(channels[0]);
            return null;
        }
    }

    private static class DeleteChannelAsyncTask extends AsyncTask<Channels, Void, Void> {
        private ChannelsDao channelsDao;

        private DeleteChannelAsyncTask(ChannelsDao channelsDao) {
            this.channelsDao = channelsDao;
        }

        @Override
        protected Void doInBackground(Channels... channels) {
            channelsDao.delete(channels[0]);
            return null;
        }
    }

    private static class AllOnChannelAsyncTask extends AsyncTask<Void, Void, Void> {
        private ChannelsDao channelsDao;

        private AllOnChannelAsyncTask(ChannelsDao channelsDao) {
            this.channelsDao = channelsDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            channelsDao.getAllChannelsOn();
            return null;
        }
    }

    private static class AllOffChannelAsyncTask extends AsyncTask<Void, Void, Void> {
        private ChannelsDao channelsDao;

        private AllOffChannelAsyncTask(ChannelsDao channelsDao) {
            this.channelsDao = channelsDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            channelsDao.getAllChannelsOff();
            return null;
        }
    }

    /*private static class LoadPickerChannelAsyncTask extends AsyncTask<Channels, Void, Void> {
        private ChannelsDao channelsDao;

        private LoadPickerChannelAsyncTask(ChannelsDao channelsDao) {
            this.channelsDao = channelsDao;
        }


        @Override
        protected Void doInBackground(Channels... channels) {
            channelsDao.insert(channels[0]);
            return null;
        }
    }*/




}
