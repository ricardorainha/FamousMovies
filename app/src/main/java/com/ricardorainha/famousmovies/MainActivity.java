package com.ricardorainha.famousmovies;

import android.os.Bundle;
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

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {

    private MoviesListController controller;
    private RecyclerView rvMovies;
    private ProgressBar pbLoading;
    private FrameLayout flWarning;
    private TextView tvMoviesType;
    private MoviesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureViews();

        controller = new MoviesListController();
        controller.addObserver(this);

        requestMovies(MoviesListController.RequestType.MOST_POPULAR);
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (observable instanceof MoviesListController) {
            int result = (int) arg;

            if (result == MoviesListController.RESPONSE_SUCCESS) {
                setupMovieAdapter();
            } else if (result == MoviesListController.RESPONSE_FAILED) {
                Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
            } else if (result == MoviesListController.REQUEST_FAILURE) {
                Toast.makeText(this, "There was a problem with your request. Please check your internet connection.", Toast.LENGTH_LONG).show();
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

    private void setupMovieAdapter() {
        adapter = new MoviesAdapter(controller.getMoviesList().getResults());
        rvMovies.setAdapter(adapter);
        tvMoviesType.setText(controller.getRequestType().getTitle());
        showMoviesViews(true);
    }

}
