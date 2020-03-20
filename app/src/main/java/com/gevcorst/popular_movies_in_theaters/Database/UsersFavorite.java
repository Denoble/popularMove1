package com.gevcorst.popular_movies_in_theaters.Database;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite")
public class UsersFavorite {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String mMoveTitle;
    private int mMovieId;
    @Ignore
    public UsersFavorite(String mMoveTitle, int mMovieId){
        this.mMoveTitle = mMoveTitle;
        this.mMovieId = mMovieId;
    }
    public UsersFavorite(int id, int mMovieId, String mMoveTitle){
        this.id = id;
        this.mMovieId = mMovieId;
        this.mMoveTitle = mMoveTitle;
    }
    public int getId() {
        return id;
    }

    public String getMoveTitle() {
        return mMoveTitle;
    }

    public int getMovieId() {
        return mMovieId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMoveTitle(String mMoveTitle) {
        this.mMoveTitle = mMoveTitle;
    }

    public void setMovieId(int mMovieId) {
        this.mMovieId = mMovieId;
    }
}
