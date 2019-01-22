package com.ricardorainha.famousmovies.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.ricardorainha.famousmovies.R;
import com.ricardorainha.famousmovies.adapter.MoviesAdapter;
import com.ricardorainha.famousmovies.controllers.MoviesListController;
import com.ricardorainha.famousmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer, MoviesAdapter.MoviesListItemClickListener {

    private MoviesListController controller;
    private FrameLayout flMainRoot;
    private RecyclerView rvMovies;
    private ProgressBar pbLoading;
    private TextView tvWarningMessage;
    private TextView tvNoItems;
    private TextView tvMoviesType;
    private MoviesAdapter adapter;
    private SwipeRefreshLayout srlRefresh;

    private List<Movie> currentMovies;
    private MoviesListController.RequestType currentMovieListType;

    private MainViewModel viewModel;

    private static final MoviesListController.RequestType DEFAULT_REQUEST_TYPE = MoviesListController.RequestType.MOST_POPULAR;
    private static final String SAVED_MOVIES_LIST_KEY = "moviesList";
    private static final String SAVED_MOVIES_TYPE_KEY = "moviesType";
    public static final String MOVIE_DETAILS_KEY = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFavorites().observe(this, favorites ->  {
            if (currentMovieListType == MoviesListController.RequestType.FAVORITES) {
                Log.d("MainActivity", "Updating favorites list");
                showFavorites();
            }
        });

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
            requestMovies(DEFAULT_REQUEST_TYPE);
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
                setupMovieAdapter(controller.getMoviesList(), controller.getRequestType());
            } else if ((result == MoviesListController.RESPONSE_FAILED) || (result == MoviesListController.REQUEST_FAILURE)) {
                Snackbar.make(flMainRoot, R.string.error_request_movies_list, Snackbar.LENGTH_LONG)
                        .setAction(R.string.show, v -> requestMovies(MoviesListController.RequestType.FAVORITES))
                        .show();
            }

            showWarningMessage(result != MoviesListController.RESPONSE_SUCCESS);
            showNoItemsView(false);
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
            case R.id.action_show_favorites:
                requestMovies(MoviesListController.RequestType.FAVORITES);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void configureViews() {
        flMainRoot = findViewById(R.id.fl_main_root);
        rvMovies = findViewById(R.id.rv_movies);
        int numberOfColumns = 2;
        rvMovies.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        pbLoading = findViewById(R.id.pb_loading);
        tvWarningMessage = findViewById(R.id.tv_warning_message);
        tvNoItems = findViewById(R.id.tv_no_items);
        tvMoviesType = findViewById(R.id.tv_movies_list_type);

        srlRefresh = findViewById(R.id.srl_refresh);
        srlRefresh.setOnRefreshListener(() -> {
            srlRefresh.setRefreshing(false); // explicitly disabling swipe progress indicator since we have our own
            requestMovies((currentMovieListType != null) ? currentMovieListType : DEFAULT_REQUEST_TYPE);
        });
    }

    private void requestMovies(MoviesListController.RequestType requestType) {
        showMoviesViews(false);
        showWarningMessage(false);
        showNoItemsView(false);
        showProgressBar(true);
        currentMovieListType = requestType;
        tvMoviesType.setText(getResources().getString(requestType.getResourceId()));
        if (requestType == MoviesListController.RequestType.FAVORITES) {
            srlRefresh.setEnabled(false);
            showFavorites();
        }
        else {
            srlRefresh.setEnabled(true);
            controller.requestMovies(requestType);
        }
    }

    private void showMoviesViews(boolean show) {
        rvMovies.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showProgressBar(boolean show) {
        pbLoading.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showWarningMessage(boolean show) {
        tvWarningMessage.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showNoItemsView(boolean show) {
        tvNoItems.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void setupMovieAdapter(List<Movie> movies, MoviesListController.RequestType moviesType) {
        currentMovies = movies;
        currentMovieListType = moviesType;
        adapter = new MoviesAdapter(movies, this);
        rvMovies.setAdapter(adapter);
        showMoviesViews(movies.size() > 0);
        showNoItemsView(movies.size() <= 0);
    }

    private void showFavorites() {
        showWarningMessage(false);
        showNoItemsView(false);
        showProgressBar(false);
        setupMovieAdapter(viewModel.getFavorites().getValue(), MoviesListController.RequestType.FAVORITES);
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
