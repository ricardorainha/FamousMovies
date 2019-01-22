package com.ricardorainha.famousmovies.database;

import com.ricardorainha.famousmovies.models.Movie;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MovieDAO {

    @Query("SELECT * FROM " + MovieDatabase.FAVORITES_TABLE_NAME)
    LiveData<List<Movie>> getAllFavorites();

    @Insert
    void addToFavorites(Movie favorite);

    @Delete
    void removeFromFavorites(Movie favorite);

    @Query("SELECT * FROM " + MovieDatabase.FAVORITES_TABLE_NAME + " WHERE id = :id")
    Movie getFavoriteById(int id);
}
