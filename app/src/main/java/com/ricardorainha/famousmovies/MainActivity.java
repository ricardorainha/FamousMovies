package com.ricardorainha.famousmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.ricardorainha.famousmovies.controllers.MoviesListController;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MoviesListController controller = new MoviesListController();
        controller.addObserver(this);
        controller.requestPopularMovies();
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (observable instanceof MoviesListController) {
            int result = (int) arg;
            
            if (result == MoviesListController.RESPONSE_SUCCESS) {
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
            } else if (result == MoviesListController.RESPONSE_FAILED) {
                Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}
