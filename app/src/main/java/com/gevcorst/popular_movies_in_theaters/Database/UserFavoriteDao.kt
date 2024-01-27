package com.gevcorst.popular_movies_in_theaters.Database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gevcorst.popular_movies_in_theaters.Model.Converters
import com.gevcorst.popular_movies_in_theaters.Model.Movie
import kotlinx.coroutines.Deferred

@Dao
interface UserFaveDAO {
    @Query("SELECT * FROM favorite ORDER BY movieId")
    fun loadAllTasks(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Movie)
    @Query("SELECT * FROM favorite WHERE movieId = :id")
    fun loadFavoriteById(id: Int): Movie?

    @Query("DELETE  FROM favorite WHERE movieId = :id")
    suspend fun deleteById(id: Int)
}

@Database(entities = arrayOf(Movie::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        @JvmStatic
        fun getInstance(appcontext: Context): AppDatabase {
            return Room.databaseBuilder(
                appcontext,
                AppDatabase::class.java,
                "movieDatabase.db"
            ).build()
        }
    }

    abstract fun userFavObjectDao(): UserFaveDAO
}









