package com.kbrs.app.GsmRemoteControl;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Channels.class}, version = 3, exportSchema = false)

public abstract class ChannelsDB extends RoomDatabase {

    private static ChannelsDB instance;

    public abstract ChannelsDao channelsDataAccessObject();

    public static synchronized ChannelsDB getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ChannelsDB.class,
                    "channels_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomcallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomcallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask <Void, Void, Void>{

        private ChannelsDao channelsDao;

        private PopulateDBAsyncTask(ChannelsDB db){
            channelsDao = db.channelsDataAccessObject();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            channelsDao.insert(new Channels(1, "...", false));
            channelsDao.insert(new Channels(2, "...", false));
            channelsDao.insert(new Channels(3, "...", false));
            return null;
        }
    }
}
