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
    private RequestType requestType;

    public static int RESPONSE_SUCCESS = 999;
    public static int RESPONSE_FAILED = 888;
    public static int REQUEST_FAILURE = 777;

    public MoviesListController() {
        moviesAPI = TheMovieDB.getAPI();
    }

    public void requestMovies(RequestType type) {
        if (moviesAPI != null) {
            requestType = type;
            Call<MoviesList> moviesCall;
            if (requestType == RequestType.MOST_POPULAR) {
                moviesCall = moviesAPI.getPopularMovies();
            }
            else {
                moviesCall = moviesAPI.getTopRatedMovies();
            }
            moviesCall.enqueue(this);
        }
    }

    public MoviesList getMoviesList() {
        return moviesList;
    }

    public RequestType getRequestType() {
        return requestType;
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
        setChanged();
        notifyObservers(REQUEST_FAILURE);
    }

    public enum RequestType {
        MOST_POPULAR("Most Popular Movies"),
        TOP_RATED("Top Rated Movies");

        private final String title;

        RequestType(String t) {
            title = t;
        }

        public String getTitle() {
            return title;
        }

        public String toString() {
            return getTitle();
        }
    }
}
