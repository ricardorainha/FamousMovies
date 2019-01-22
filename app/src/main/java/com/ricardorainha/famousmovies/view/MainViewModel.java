package com.ricardorainha.famousmovies.view;

import android.app.Application;
import android.util.Log;

import com.ricardorainha.famousmovies.database.MovieDatabase;
import com.ricardorainha.famousmovies.models.Movie;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> favorites;

    public MainViewModel(@NonNull Application application) {
        super(application);

        MovieDatabase db = MovieDatabase.getInstance(this.getApplication());
        Log.d("MainViewModel", "Loading favorites from database");
        favorites = db.movieDAO().getAllFavorites();
    }

    public LiveData<List<Movie>> getFavorites() {
        return favorites;
    }
}
