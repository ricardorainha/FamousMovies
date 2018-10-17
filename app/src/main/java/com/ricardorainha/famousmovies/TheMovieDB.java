package com.ricardorainha.famousmovies;

import com.ricardorainha.famousmovies.models.MoviesList;

import retrofit2.Call;
import retrofit2.http.GET;

public class TheMovieDB {

    public static final String API_BASE_URL = "https://api.themoviedb.org/3/";
    public static final String API_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    public static final String DEFAULT_POSTER_SIZE = "w185";
    public static final String API_KEY_PARAMETER = "?api_key=" + BuildConfig.TMDB_API_KEY;


    public interface Endpoints {
        @GET("movie/popular" + API_KEY_PARAMETER)
        Call<MoviesList> getPopularMovies();

        @GET("movie/top_rated" + API_KEY_PARAMETER)
        Call<MoviesList> getTopRatedMovies();
    }

}
