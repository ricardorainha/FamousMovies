package com.ricardorainha.famousmovies.controllers;

import android.content.Context;

import com.ricardorainha.famousmovies.R;
import com.ricardorainha.famousmovies.database.MovieDatabase;
import com.ricardorainha.famousmovies.models.Movie;
import com.ricardorainha.famousmovies.network.TheMovieDB;
import com.ricardorainha.famousmovies.models.MoviesList;

import java.util.List;
import java.util.Observable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesListController extends Observable implements Callback<MoviesList> {

    private TheMovieDB.Endpoints moviesAPI;
    private List<Movie> moviesList;
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

    public List<Movie> getMoviesList() {
        return moviesList;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    @Override
    public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
        if (response.isSuccessful()) {
            moviesList = response.body().getResults();
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
        MOST_POPULAR(R.string.most_popular_movies),
        TOP_RATED(R.string.top_rated_movies),
        FAVORITES(R.string.favorites);

        private final int resourceId;

        RequestType(int id) {
            resourceId = id;
        }

        public static RequestType fromValue(int id) {
            for (RequestType reqType : RequestType.values())
                if (reqType.resourceId == id)
                    return reqType;

            throw new IllegalArgumentException();
        }

        public int getResourceId() {
            return resourceId;
        }
    }
}
