package com.ricardorainha.famousmovies.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ricardorainha.famousmovies.R;
import com.ricardorainha.famousmovies.models.Movie;
import com.ricardorainha.famousmovies.view.MainActivity;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView ivPoster;
    private TextView tvTitle;
    private TextView tvReleaseDate;
    private TextView tvAverage;
    private TextView tvOverview;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if ((getIntent() != null) && (getIntent().hasExtra(MainActivity.MOVIE_DETAILS_KEY))) {
            movie = (Movie) getIntent().getExtras().get(MainActivity.MOVIE_DETAILS_KEY);

            configureViews();

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
    }
}