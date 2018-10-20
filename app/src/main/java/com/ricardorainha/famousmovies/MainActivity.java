package com.ricardorainha.famousmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ricardorainha.famousmovies.controllers.MoviesListController;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {

    private MoviesListController controller;
    private RecyclerView rvMovies;
    private ProgressBar pbLoading;
    private FrameLayout flWarning;

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
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
            } else if (result == MoviesListController.RESPONSE_FAILED) {
                Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
            } else if (result == MoviesListController.REQUEST_FAILURE) {
                Toast.makeText(this, "There was a problem with your request. Please check your internet connection.", Toast.LENGTH_LONG).show();
            }

            showWarningMessage(result != MoviesListController.RESPONSE_SUCCESS);
            showProgressBar(false);
        }
    }

    private void configureViews() {
        rvMovies = (RecyclerView) findViewById(R.id.rv_movies);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        flWarning = (FrameLayout) findViewById(R.id.fl_warning);
    }

    private void requestMovies(MoviesListController.RequestType requestType) {
        showProgressBar(true);
        controller.requestMovies(requestType);
    }

    private void showProgressBar(boolean show) {
        pbLoading.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showWarningMessage(boolean show) {
        flWarning.setVisibility(show ? View.VISIBLE : View.GONE);
        rvMovies.setVisibility(show ? View.GONE : View.VISIBLE);
    }

}
