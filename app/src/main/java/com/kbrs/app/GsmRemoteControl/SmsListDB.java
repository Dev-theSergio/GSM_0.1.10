package com.kbrs.app.GsmRemoteControl;

        import android.arch.persistence.db.SupportSQLiteDatabase;
        import android.arch.persistence.room.Database;
        import android.arch.persistence.room.Room;
        import android.arch.persistence.room.RoomDatabase;
        import android.content.Context;
        import android.os.AsyncTask;
        import android.support.annotation.NonNull;

@Database(entities = {SmsListTable.class}, version = 1, exportSchema = false)

public abstract class SmsListDB extends RoomDatabase {

    private static SmsListDB instance;

    public abstract SmsDao SmsDao();

    public static synchronized SmsListDB getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    SmsListDB.class,
                    "sms_list_database")
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

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {

        private SmsDao SmsDao;

        private PopulateDBAsyncTask(SmsListDB db){
            SmsDao = db.SmsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //SmsDao.insert(new SmsListTable("E.g. sms body", "01–01–2019", "11:00"));
            return null;
        }
    }
}

