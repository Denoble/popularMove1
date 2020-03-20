package com.gevcorst.popular_movies_in_theaters.Database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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
