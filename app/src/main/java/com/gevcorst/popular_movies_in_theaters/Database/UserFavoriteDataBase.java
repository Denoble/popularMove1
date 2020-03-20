package com.gevcorst.popular_movies_in_theaters.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// Specifying our entity class
@Database(entities = {UsersFavorite.class}, version = 1, exportSchema = false)
public abstract class UserFavoriteDataBase extends RoomDatabase {
    private  static final String  LOG_TAG = UserFavoriteDataBase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String databaseName = "usersFavoriteMovies";
    private static UserFavoriteDataBase userFavoriteDataBaseInstance;


    /**
     *
     * @param context  The application context
     * @return An instance of SQLITE DATABASE
     */
    public static  UserFavoriteDataBase getInstance(Context context){
        if(userFavoriteDataBaseInstance == null){
            synchronized (LOCK){
                userFavoriteDataBaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                        UserFavoriteDataBase.class, UserFavoriteDataBase.databaseName)
                        //.allowMainThreadQueries()
                        .build();
            }
        }
        return userFavoriteDataBaseInstance;
    }
    public abstract FavoriteDao favoriteDao();
}
