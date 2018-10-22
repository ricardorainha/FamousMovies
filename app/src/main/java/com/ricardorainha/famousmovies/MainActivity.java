package com.ricardorainha.famousmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ricardorainha.famousmovies.adapter.MoviesAdapter;
import com.ricardorainha.famousmovies.controllers.MoviesListController;
import com.ricardorainha.famousmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer, MoviesAdapter.MoviesListItemClickListener {

    private MoviesListController controller;
    private RecyclerView rvMovies;
    private ProgressBar pbLoading;
    private FrameLayout flWarning;
    private TextView tvMoviesType;
    private MoviesAdapter adapter;
    private SwipeRefreshLayout srlRefresh;

    private List<Movie> currentMovies;
    private MoviesListController.RequestType currentMovieListType;

    private static final String SAVED_MOVIES_LIST_KEY = "moviesList";
    private static final String SAVED_MOVIES_TYPE_KEY = "moviesType";
    public static final String MOVIE_DETAILS_KEY = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureViews();

        controller = new MoviesListController();
        controller.addObserver(this);

        if ((savedInstanceState != null)
            && (savedInstanceState.containsKey(SAVED_MOVIES_LIST_KEY))) {
            currentMovies = savedInstanceState.getParcelableArrayList(SAVED_MOVIES_LIST_KEY);
            currentMovieListType = MoviesListController.RequestType.fromValue(savedInstanceState.getInt(SAVED_MOVIES_TYPE_KEY));
            setupMovieAdapter(currentMovies, currentMovieListType);
        }
        else {
            requestMovies(MoviesListController.RequestType.MOST_POPULAR);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if ((currentMovies != null)
                && (currentMovieListType != null)) {
            outState.putParcelableArrayList(SAVED_MOVIES_LIST_KEY, (ArrayList<Movie>)currentMovies);
            outState.putInt(SAVED_MOVIES_TYPE_KEY, currentMovieListType.getResourceId());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (observable instanceof MoviesListController) {
            int result = (int) arg;

            if (result == MoviesListController.RESPONSE_SUCCESS) {
                setupMovieAdapter(controller.getMoviesList().getResults(), controller.getRequestType());
            } else if ((result == MoviesListController.RESPONSE_FAILED) || (result == MoviesListController.REQUEST_FAILURE)) {
                Toast.makeText(this, R.string.error_request_movies_list, Toast.LENGTH_LONG).show();
            }

            showWarningMessage(result != MoviesListController.RESPONSE_SUCCESS);
            showProgressBar(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_sort_most_popular:
                requestMovies(MoviesListController.RequestType.MOST_POPULAR);
                return true;
            case R.id.action_sort_top_rated:
                requestMovies(MoviesListController.RequestType.TOP_RATED);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void configureViews() {
        rvMovies = (RecyclerView) findViewById(R.id.rv_movies);
        int numberOfColumns = 2;
        rvMovies.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        flWarning = (FrameLayout) findViewById(R.id.fl_warning);
        tvMoviesType = (TextView) findViewById(R.id.tv_movies_list_type);

        srlRefresh = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        srlRefresh.setOnRefreshListener(() -> {
            srlRefresh.setRefreshing(false); // explicitly disabling swipe progress indicator since we have our own
            requestMovies(currentMovieListType);
        });
    }

    private void requestMovies(MoviesListController.RequestType requestType) {
        showMoviesViews(false);
        showWarningMessage(false);
        showProgressBar(true);
        controller.requestMovies(requestType);
    }

    private void showMoviesViews(boolean show) {
        rvMovies.setVisibility(show ? View.VISIBLE : View.GONE);
        tvMoviesType.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showProgressBar(boolean show) {
        pbLoading.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showWarningMessage(boolean show) {
        flWarning.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void setupMovieAdapter(List<Movie> movies, MoviesListController.RequestType moviesType) {
        currentMovies = movies;
        currentMovieListType = moviesType;
        adapter = new MoviesAdapter(movies, this);
        rvMovies.setAdapter(adapter);
        tvMoviesType.setText(getResources().getString(moviesType.getResourceId()));
        showMoviesViews(true);
    }

    @Override
    public void onMoviesListItemClick(int itemIndex) {
        if (currentMovies != null && itemIndex < currentMovies.size()) {
            Intent movieDetailsIntent = new Intent();
            movieDetailsIntent.setClass(this, MovieDetailsActivity.class);
            movieDetailsIntent.putExtra(MOVIE_DETAILS_KEY, currentMovies.get(itemIndex));
            startActivity(movieDetailsIntent);
        }
    }
}
