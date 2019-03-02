package com.gevcorst.popularmoviesstage1.Database;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface FavoriteDao {
    @Query("SELECT * FROM favorite ORDER BY mMovieId")
   LiveData<List<UsersFavorite>> loadAllTasks();

    @Insert
    void insertFavorite(UsersFavorite favorite);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavorite(UsersFavorite taskEntry);

    @Delete
    void deleteFavorite(UsersFavorite favorite);

    @Query("SELECT * FROM favorite WHERE mMovieId = :id")
   LiveData<UsersFavorite> loadFavoriteById(int id);
    @Query("DELETE  FROM favorite WHERE mMovieId = :id")
    void deleteById(int id);
}
