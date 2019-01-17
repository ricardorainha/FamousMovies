package com.ricardorainha.famousmovies.database;

import com.ricardorainha.famousmovies.models.Movie;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MovieDAO {

    @Query("SELECT * FROM " + MovieDatabase.FAVORITES_TABLE_NAME)
    List<Movie> getAllFavorites();

    @Insert
    void addToFavorites(Movie favorite);

    @Delete
    void removeFromFavorites(Movie favorite);
}
