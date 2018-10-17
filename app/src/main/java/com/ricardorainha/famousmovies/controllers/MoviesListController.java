package com.ricardorainha.famousmovies.controllers;

import com.ricardorainha.famousmovies.TheMovieDB;
import com.ricardorainha.famousmovies.models.MoviesList;

import java.util.Observable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesListController extends Observable implements Callback<MoviesList> {

    private TheMovieDB.Endpoints moviesAPI;
    private MoviesList moviesList;

    public static int RESPONSE_SUCCESS = 999;
    public static int RESPONSE_FAILED = 888;

    public MoviesListController() {
        moviesAPI = TheMovieDB.getAPI();
    }

    public void requestPopularMovies() {
        if (moviesAPI != null) {
            Call<MoviesList> moviesCall = moviesAPI.getPopularMovies();
            moviesCall.enqueue(this);
        }
    }

    public void requestTopRatedMovies() {
        if (moviesAPI != null) {
            Call<MoviesList> moviesCall = moviesAPI.getTopRatedMovies();
            moviesCall.enqueue(this);
        }
    }

    public MoviesList getMoviesList() {
        return moviesList;
    }

    @Override
    public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
        if (response.isSuccessful()) {
            moviesList = response.body();
            setChanged();
            notifyObservers(RESPONSE_SUCCESS);
        }
        else {
            moviesList = null;
            setChanged();
            notifyObservers(RESPONSE_FAILED);
        }
    }

    @Override
    public void onFailure(Call<MoviesList> call, Throwable t) {
        t.printStackTrace();
    }
}
