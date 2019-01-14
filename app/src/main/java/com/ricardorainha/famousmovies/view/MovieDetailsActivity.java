package com.ricardorainha.famousmovies.view;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ricardorainha.famousmovies.R;
import com.ricardorainha.famousmovies.adapter.VideosAdapter;
import com.ricardorainha.famousmovies.controllers.MovieDetailsController;
import com.ricardorainha.famousmovies.models.Movie;
import com.ricardorainha.famousmovies.models.Review;

import java.util.Observable;
import java.util.Observer;

public class MovieDetailsActivity extends AppCompatActivity implements Observer {

    private ImageView ivPoster;
    private TextView tvTitle;
    private TextView tvReleaseDate;
    private TextView tvAverage;
    private TextView tvOverview;
    private ViewPager videosPager;
    private LinearLayout llReviews;

    private Movie movie;
    MovieDetailsController detailsController;
    VideosAdapter videosAdapter;

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

        llReviews = findViewById(R.id.ll_reviews);
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

        videosAdapter = new VideosAdapter(movie.getVideos());
        videosPager.setAdapter(videosAdapter);
    }

    private void configureReviews() {
        movie.setReviews(detailsController.getReviewList());

        if (movie.getReviews().size() > 0) {
            llReviews.removeAllViews();
            for (Review review : movie.getReviews()) {
                View itemView = LayoutInflater.from(this).inflate(R.layout.review_item, llReviews, false);
                ((TextView) itemView.findViewById(R.id.tv_author)).setText(String.format(getString(R.string.review_author), review.getAuthor()));
                ((TextView) itemView.findViewById(R.id.tv_content)).setText(review.getContent());
                llReviews.addView(itemView);
            }
        }
        else {
            TextView emptyReviews = new TextView(this);
            emptyReviews.setText(getString(R.string.no_reviews));
            llReviews.addView(emptyReviews);
        }
    }

}
