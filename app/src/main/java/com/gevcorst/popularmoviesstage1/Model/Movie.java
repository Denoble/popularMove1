package com.gevcorst.popularmoviesstage1.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;
import java.util.Date;

public class Movie implements Parcelable {
    private String mOriginal_title;
    private int mId;
    private Double mUser_rating;
    private String mSynopsis;
    private String mImage_thumbnail;
    private Double mPopularity;

    public String getSReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.mReleaseDate = releaseDate;
    }

    private String mReleaseDate;
    public Movie(){}
    private Movie(Parcel in) {
        mOriginal_title = in.readString();
        mId = in.readInt();
        mUser_rating = in.readDouble();
        mSynopsis = in.readString();
        mImage_thumbnail = in.readString();
       mReleaseDate = in.readString();
       mPopularity = in.readDouble();
    }

    public String getOriginalTitle() {
        return this.mOriginal_title;
    }

    public void setOriginalTitle(String title) {
        this.mOriginal_title = title;
    }

    public Double getUserRating() {
        return this.mUser_rating;
    }

    public void setUserRating(Double userRating) {
        this.mUser_rating = userRating;
    }

    public String getSynopsis() {
        return this.mSynopsis;
    }

    public void setSynopsis(String synopsis) {
        this.mSynopsis = synopsis;
    }

    public void setImageThumbnail(String thumbnail) {
        this.mImage_thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return this.mImage_thumbnail;
    }

    public void setRelease_date(Date releaseDate) {
        Date mRelease_date = releaseDate;
    }

    public void setMovieId(int id) {
        this.mId = id;
    }

    private Double getPopularity() {
        return mPopularity;
    }

    public void setPopularity(Double mPopularity) {
        this.mPopularity = mPopularity;
    }

    /*Comparator for sorting the Movie List by released date*/
    public static final Comparator<Movie> moviePopularityComparator = new Comparator<Movie>() {

        public int compare(Movie movie1, Movie movie2) {

            //Descending order
            return movie2.getPopularity().compareTo(movie1.getPopularity());


        }


    };
    /*Comparator for sorting the Movie List by Rating*/
    public static final Comparator<Movie> movieRatingComparator = new Comparator<Movie>() {

        public int compare(Movie movie1, Movie movie2) {
            //Descending order
            return movie2.getUserRating().compareTo(movie1.getUserRating());


        }


    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(mOriginal_title);
        dest.writeInt(mId);
        dest.writeDouble(mUser_rating);
        dest.writeString(mSynopsis);
        dest.writeString(mImage_thumbnail);
        dest.writeString(mReleaseDate);
        dest.writeDouble(mPopularity);


    }
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>()
    {
        public Movie createFromParcel(Parcel in)
        {
            return new Movie(in);
        }
        public Movie[] newArray(int size)
        {
            return new Movie[size];
        }
    };
}