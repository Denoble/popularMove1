package com.gevcorst.popularmoviesstage1.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

// Specifying our entity class
@Database(entities = {UsersFavorite.class}, version = 1, exportSchema = false)
public abstract class UserFavoriteDataBase extends RoomDatabase {
    private  static final String  LOG_TAG = UserFavoriteDataBase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String databaseName = "usersFavoriteMovies";
    private static UserFavoriteDataBase userFavoriteDataBaseInstance;


    /**
     *
     * @param context
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
