package com.gevcorst.popular_movies_in_theaters.Model


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite")
@Parcelize
@JsonClass(generateAdapter = true)
data class Movie(
    @PrimaryKey(autoGenerate = true)
    @Json(name = "adult")
    val adult: Boolean,
    @Json(name = "backdrop_path")
    val backdropPath: String,
    @Json(name = "genre_ids")
    val genreIds: List<Int>,
    @Json(name = "id")
    val movieId: Int,
    @Json(name = "original_language")
    val originalLanguage: String,
    @Json(name = "original_title")
    val originalTitle: String,
    @Json(name = "overview")
    val overview: String,
    @Json(name = "popularity")
    val popularity: Double,
    @Json(name = "poster_path")
    val posterPath: String,
    @Json(name = "release_date")
    val releaseDate: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "video")
    val video: Boolean,
    @Json(name = "vote_average")
    val voteAverage: Double,
    @Json(name = "vote_count")
    val voteCount: Int
):Parcelable


class Converters {
    @TypeConverter
    fun genreid(value: List<Int>) = Gson().toJson(value)

    @TypeConverter
    fun genreid(data: String): List<Int> {
        val gson = Gson()
        val list = object: TypeToken<List<Int>>(){}.type
        return gson.fromJson(data,list)
    }
}

