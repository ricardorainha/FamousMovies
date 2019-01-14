package com.ricardorainha.famousmovies.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ricardorainha.famousmovies.models.Video;

import java.util.List;

public class MovieVideosAdapter extends PagerAdapter {

    private List<Video> videos;
    private static final int visiblePages = 2;

    public MovieVideosAdapter(List<Video> videos) {
        this.videos = videos;
    }

    @Override
    public int getCount() {
        return (videos != null) ? videos.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = null;
        if (position < getCount()) {
            imageView = new ImageView(container.getContext());
            Glide.with(container.getContext()).load(videos.get(position).getThumbnailURL()).into(imageView);
            imageView.setOnClickListener(view -> view.getContext().startActivity(videos.get(position).getOpenVideoIntent()));
            container.addView(imageView);
        }

        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public float getPageWidth(int position) {
        return ((float)1 / (float)visiblePages);
    }
}
