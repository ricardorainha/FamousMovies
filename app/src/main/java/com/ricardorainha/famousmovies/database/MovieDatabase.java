package com.ricardorainha.famousmovies.database;


import android.content.Context;

import com.ricardorainha.famousmovies.models.Movie;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    public static final String FAVORITES_TABLE_NAME = "favorites";

    private static final String DATABASE_NAME = "movies";
    private static final Object LOCK  = new Object();

    private static MovieDatabase instance;

    public static MovieDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        MovieDatabase.class,
                        DATABASE_NAME)
                        .build();
            }
        }

        return instance;
    }

    public abstract MovieDAO movieDAO();


}
