package com.ricardorainha.famousmovies.controllers;

import com.ricardorainha.famousmovies.models.Review;
import com.ricardorainha.famousmovies.models.ReviewsList;
import com.ricardorainha.famousmovies.models.Trailer;
import com.ricardorainha.famousmovies.models.TrailersList;
import com.ricardorainha.famousmovies.network.TheMovieDB;

import java.util.List;
import java.util.Observable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsController extends Observable {

    private TheMovieDB.Endpoints moviesAPI = TheMovieDB.getAPI();
    private int movieId;

    public static final int VIDEOS_RESPONSE_SUCCESS = 654;
    public static final int VIDEOS_RESPONSE_FAILED = 753;
    public static final int VIDEOS_REQUEST_FAILURE = 159;
    public static final int REVIEWS_RESPONSE_SUCCESS = 154;
    public static final int REVIEWS_RESPONSE_FAILED = 741;
    public static final int REVIEWS_REQUEST_FAILURE = 369;

    private List<Trailer> trailerList;
    private List<Review> reviewList;

    public MovieDetailsController(int movieId) {
        this.movieId = movieId;
    }

    public void requestVideosAndReviews() {
        requestVideos();
        requestReviews();
    }

    public void requestVideos() {
        if (moviesAPI != null) {
            Call<TrailersList> trailersCall = moviesAPI.getMovieVideos(movieId);
            trailersCall.enqueue(new Callback<TrailersList>() {
                @Override
                public void onResponse(Call<TrailersList> call, Response<TrailersList> response) {
                    if (response.isSuccessful()) {
                        trailerList = response.body().getTrailers();
                        notifyDetailsObservers(VIDEOS_RESPONSE_SUCCESS);
                    }
                    else {
                        notifyDetailsObservers(VIDEOS_RESPONSE_FAILED);
                    }
                }

                @Override
                public void onFailure(Call<TrailersList> call, Throwable t) {
                    t.printStackTrace();
                    notifyDetailsObservers(VIDEOS_REQUEST_FAILURE);
                }
            });
        }
    }

    public void requestReviews() {
        if (moviesAPI != null) {
            Call<ReviewsList> reviewsCall = moviesAPI.getMovieReviews(movieId);
            reviewsCall.enqueue(new Callback<ReviewsList>() {
                @Override
                public void onResponse(Call<ReviewsList> call, Response<ReviewsList> response) {
                    if (response.isSuccessful()) {
                        reviewList = response.body().getReviews();
                        notifyDetailsObservers(REVIEWS_RESPONSE_SUCCESS);
                    }
                    else {
                        notifyDetailsObservers(REVIEWS_RESPONSE_FAILED);
                    }
                }

                @Override
                public void onFailure(Call<ReviewsList> call, Throwable t) {
                    t.printStackTrace();
                    notifyDetailsObservers(REVIEWS_REQUEST_FAILURE);
                }
            });
        }
    }

    private void notifyDetailsObservers(int notificationId) {
        setChanged();
        notifyObservers(notificationId);
    }

    public List<Trailer> getTrailerList() {
        return trailerList;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }
}
