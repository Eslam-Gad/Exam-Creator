package com.EX.examcreator.Database_OFF_Line;

import android.content.Context;
import android.util.Log;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Model_Favourite.class}, version = 2, exportSchema = false)
public abstract class Database_Favourite extends RoomDatabase {

    private static final String LOG_TAG = Database_Favourite.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "Favourite";
    private static Database_Favourite sInstance;

    public static Database_Favourite getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        Database_Favourite.class, Database_Favourite.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract Dao_Favourite taskDao();
}
