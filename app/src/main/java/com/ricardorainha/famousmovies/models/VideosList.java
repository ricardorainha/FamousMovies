package com.ricardorainha.famousmovies.models;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideosList implements Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Video> videos = new ArrayList<Video>();
    public final static Parcelable.Creator<VideosList> CREATOR = new Creator<VideosList>() {
        @SuppressWarnings({"unchecked"})
        public VideosList createFromParcel(Parcel in) {
            return new VideosList(in);
        }

        public VideosList[] newArray(int size) {
            return (new VideosList[size]);
        }
    };

    protected VideosList(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.videos, (Video.class.getClassLoader()));
    }

    public VideosList() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeList(videos);
    }

    public int describeContents() {
        return 0;
    }

}
