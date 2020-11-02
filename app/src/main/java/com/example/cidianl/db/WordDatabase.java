package com.example.cidianl.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.cidianl.bean.Word;

@Database(entities = {Word.class}, version = 2, exportSchema = false)
public abstract class WordDatabase extends RoomDatabase {
    private static WordDatabase INSTANCE;

    public static synchronized WordDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), WordDatabase.class, "word_database")
                    //.allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
    public abstract WordDao getWordDao();
}
