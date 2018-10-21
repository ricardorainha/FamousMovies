package com.ricardorainha.famousmovies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ricardorainha.famousmovies.R;
import com.ricardorainha.famousmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    ArrayList<Movie> movies;

    public MoviesAdapter(List<Movie> movies) {
        this.movies = (ArrayList<Movie>) movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_list_item, viewGroup, false);
        MovieViewHolder viewHolder = new MovieViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int position) {
        if (position < movies.size()) {
            Movie movie = movies.get(position);
            Glide.with(movieViewHolder.itemView.getContext()).load(movie.getPosterFullPath()).into(movieViewHolder.mPoster);
            movieViewHolder.mName.setText(movie.getTitle() + " " + movie.getReleaseYear());
        }
    }

    @Override
    public int getItemCount() {
        if (movies == null) return 0;
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView mPoster;
        TextView mName;
        TextView mVotePopularity;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            mPoster = (ImageView) itemView.findViewById(R.id.iv_poster);
            mName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
