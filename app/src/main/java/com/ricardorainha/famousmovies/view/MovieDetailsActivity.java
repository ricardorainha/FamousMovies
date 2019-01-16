package com.ricardorainha.famousmovies.view;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
    private LinearLayout llVideos;
    private LinearLayout llReviews;
    private ProgressBar pbVideos;
    private ProgressBar pbReviews;
    private View viewVideosError;
    private View viewReviewsError;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        changeMenuIcon(menu.findItem(R.id.action_toggle_favorite));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toggle_favorite:
                toggleFavorite(item);
                break;

        }
        return super.onOptionsItemSelected(item);
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

        llVideos = findViewById(R.id.ll_videos);

        pbVideos = findViewById(R.id.pb_videos);

        viewVideosError = LayoutInflater.from(this).inflate(R.layout.detail_error_message, null, false);
        viewVideosError.findViewById(R.id.btn_try_again).setOnClickListener(v -> {
            llVideos.removeView(viewVideosError);
            showVideosLoading();
            detailsController.requestVideos();
        });

        llReviews = findViewById(R.id.ll_reviews);

        pbReviews = findViewById(R.id.pb_reviews);

        viewReviewsError = LayoutInflater.from(this).inflate(R.layout.detail_error_message, null, false);
        viewReviewsError.findViewById(R.id.btn_try_again).setOnClickListener(v -> {
            llReviews.removeView(viewReviewsError);
            showReviewsLoading();
            detailsController.requestReviews();
        });
    }

    private void requestTrailersAndReviews() {
        detailsController = new MovieDetailsController(movie.getId());
        detailsController.addObserver(this);
        detailsController.requestVideosAndReviews();

        showVideosLoading();
        showReviewsLoading();
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
                case MovieDetailsController.REVIEWS_RESPONSE_FAILED:
                    showErrorMessage(R.string.error_message_response, notificationId);
                    break;
                case MovieDetailsController.VIDEOS_REQUEST_FAILURE:
                case MovieDetailsController.REVIEWS_REQUEST_FAILURE:
                    showErrorMessage(R.string.error_message_request, notificationId);
                    break;
            }
        }
    }

    private void configureVideos() {
        movie.setVideos(detailsController.getVideoList());

        videosAdapter = new VideosAdapter(movie.getVideos());
        videosPager.setAdapter(videosAdapter);

        hideVideosLoading();
        videosPager.setVisibility(View.VISIBLE);
    }

    private void configureReviews() {
        movie.setReviews(detailsController.getReviewList());

        hideReviewsLoading();

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

    private void showVideosLoading() {
        pbVideos.setVisibility(View.VISIBLE);
    }

    private void hideVideosLoading() {
        pbVideos.setVisibility(View.GONE);
    }

    private void showReviewsLoading() {
        pbReviews.setVisibility(View.VISIBLE);
    }

    private void hideReviewsLoading() {
        pbReviews.setVisibility(View.GONE);
    }

    private void showErrorMessage(int messageId, int errorId) {
        if (errorId == MovieDetailsController.VIDEOS_RESPONSE_FAILED
            || errorId == MovieDetailsController.VIDEOS_REQUEST_FAILURE) {
            hideVideosLoading();
            ((TextView)viewVideosError.findViewById(R.id.tv_error_message)).setText(messageId);
            llVideos.addView(viewVideosError);
        }
        else {
            hideReviewsLoading();
            ((TextView)viewReviewsError.findViewById(R.id.tv_error_message)).setText(messageId);
            llReviews.addView(viewReviewsError);
        }
    }

    private void toggleFavorite(MenuItem item) {
        movie.setFavorite(!movie.isFavorite());
        changeMenuIcon(item);
    }

    private void changeMenuIcon(MenuItem item) {
        item.setIcon(movie.isFavorite() ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
    }

}
