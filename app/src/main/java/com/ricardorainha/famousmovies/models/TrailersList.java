package com.ricardorainha.famousmovies.models;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrailersList implements Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Trailer> trailers = new ArrayList<Trailer>();
    public final static Parcelable.Creator<TrailersList> CREATOR = new Creator<TrailersList>() {
        @SuppressWarnings({"unchecked"})
        public TrailersList createFromParcel(Parcel in) {
            return new TrailersList(in);
        }

        public TrailersList[] newArray(int size) {
            return (new TrailersList[size]);
        }
    };

    protected TrailersList(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.trailers, (com.ricardorainha.famousmovies.models.Trailer.class.getClassLoader()));
    }

    public TrailersList() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> Trailers) {
        this.trailers = Trailers;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeList(trailers);
    }

    public int describeContents() {
        return 0;
    }

}
