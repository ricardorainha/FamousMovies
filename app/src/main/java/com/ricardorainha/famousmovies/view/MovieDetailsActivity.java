package com.ricardorainha.famousmovies.view;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ricardorainha.famousmovies.R;
import com.ricardorainha.famousmovies.adapter.MovieVideosAdapter;
import com.ricardorainha.famousmovies.controllers.MovieDetailsController;
import com.ricardorainha.famousmovies.models.Movie;

import java.util.Observable;
import java.util.Observer;

public class MovieDetailsActivity extends AppCompatActivity implements Observer {

    private ImageView ivPoster;
    private TextView tvTitle;
    private TextView tvReleaseDate;
    private TextView tvAverage;
    private TextView tvOverview;
    private ViewPager videosPager;

    private Movie movie;
    MovieDetailsController detailsController;
    MovieVideosAdapter videosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if ((getIntent() != null) && (getIntent().hasExtra(MainActivity.MOVIE_DETAILS_KEY))) {
            movie = (Movie) getIntent().getExtras().get(MainActivity.MOVIE_DETAILS_KEY);
            configureViews();
            requestTrailersAndReviews();
        }
        else {
            Toast.makeText(this, R.string.details_error, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void configureViews() {
        ivPoster = findViewById(R.id.iv_details_poster);
        Glide.with(this).load(movie.getPosterFullPath()).into(ivPoster);

        tvTitle = findViewById(R.id.tv_details_title);
        tvTitle.setText(movie.getTitle());

        tvReleaseDate = findViewById(R.id.tv_details_release_date);
        tvReleaseDate.setText(movie.getReleaseDate());

        tvAverage = findViewById(R.id.tv_details_average);
        tvAverage.setText(String.valueOf(movie.getVoteAverage()));

        tvOverview = findViewById(R.id.tv_details_overview);
        tvOverview.setText(movie.getOverview());

        videosPager = findViewById(R.id.vp_videos);
    }

    private void requestTrailersAndReviews() {
        detailsController = new MovieDetailsController(movie.getId());
        detailsController.addObserver(this);
        detailsController.requestVideosAndReviews();
    }

    @Override
    public void update(Observable observable, Object o) {
        if (detailsController != null) {
            int notificationId = (int) o;

            switch (notificationId) {
                case MovieDetailsController.VIDEOS_RESPONSE_SUCCESS:
                    configureVideos();
                    break;
                case MovieDetailsController.REVIEWS_RESPONSE_SUCCESS:
                    configureReviews();
                    break;
                case MovieDetailsController.VIDEOS_RESPONSE_FAILED:
                    break;
                case MovieDetailsController.REVIEWS_RESPONSE_FAILED:
                    break;
                case MovieDetailsController.VIDEOS_REQUEST_FAILURE:
                    break;
                case MovieDetailsController.REVIEWS_REQUEST_FAILURE:
                    break;
            }
        }
    }

    private void configureVideos() {
        movie.setVideos(detailsController.getVideoList());

        videosAdapter = new MovieVideosAdapter(movie.getVideos());
        videosPager.setAdapter(videosAdapter);
    }

    private void configureReviews() {
        movie.setReviews(detailsController.getReviewList());
    }
}
