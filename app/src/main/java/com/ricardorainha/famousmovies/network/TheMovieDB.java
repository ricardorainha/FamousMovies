package com.ricardorainha.famousmovies.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ricardorainha.famousmovies.BuildConfig;
import com.ricardorainha.famousmovies.models.MoviesList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class TheMovieDB {

    public static final String API_BASE_URL = "https://api.themoviedb.org/3/";
    public static final String API_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    public static final String DEFAULT_POSTER_SIZE = "w185";
    public static final String API_KEY_PARAMETER = "?api_key=" + BuildConfig.TMDB_API_KEY;
    private static TheMovieDB.Endpoints moviesAPI;

    public interface Endpoints {
        @GET("movie/popular" + API_KEY_PARAMETER)
        Call<MoviesList> getPopularMovies();

        @GET("movie/top_rated" + API_KEY_PARAMETER)
        Call<MoviesList> getTopRatedMovies();
    }

    public static TheMovieDB.Endpoints getAPI() {
        if (moviesAPI == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TheMovieDB.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            moviesAPI = retrofit.create(TheMovieDB.Endpoints.class);
        }
        return moviesAPI;
    }

}
