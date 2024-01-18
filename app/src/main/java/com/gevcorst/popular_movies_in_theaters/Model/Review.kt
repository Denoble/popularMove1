package com.gevcorst.popular_movies_in_theaters.Model


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Review(
    @Json(name = "id")
    val id: Int,
    @Json(name = "page")
    val page: Int,
    @Json(name = "results")
    val movieReviews: List<MovieReview>,
    @Json(name = "total_pages")
    val totalPages: Int,
    @Json(name = "total_results")
    val totalResults: Int
):Parcelable